package org.keycloak.fabric.network;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NetworkBuilder {

	private String domain;

	private String name;

	private String clientOrg;

	private String ordererOrg;

	private String[] orderers;

	private String[] peerOrgs;

	private String[] peers;

	private String[] channels;

	private Map<String, Map<String, String>> urls = new HashMap<>();
	private Map<String, Map<String, Integer>> ports = new HashMap<>();
	private File root;

	public NetworkBuilder(String domain) {
		this.domain = domain;
	}

	public NetworkBuilder root(File root) {
		this.root = root;
		return this;
	}

	public NetworkBuilder name(String name) {
		this.name = name;
		return this;
	}

	public NetworkBuilder url(String org, String peer, String url) {
		Map<String, String> value = urls.get(org);
		if (value == null) {
			value = new HashMap<>();
			urls.put(org, value);
		}
		if (peer == null) {
			peer = org;
		}
		value.put(peer, url);
		return this;
	}

	public NetworkBuilder port(String org, String peer, int port) {
		Map<String, Integer> value = ports.get(org);
		if (value == null) {
			value = new HashMap<>();
			ports.put(org, value);
		}
		if (peer == null) {
			peer = org;
		}
		value.put(peer, port);
		return this;
	}

	public NetworkBuilder clientOrg(String clientOrg) {
		this.clientOrg = clientOrg;
		return this;
	}

	public NetworkBuilder ordererOrg(String ordererOrg) {
		this.ordererOrg = ordererOrg;
		return this;
	}

	public NetworkBuilder peerOrgs(String... peerOrgs) {
		this.peerOrgs = peerOrgs;
		return this;
	}

	public NetworkBuilder orderers(String... orderers) {
		this.orderers = orderers;
		return this;
	}

	public NetworkBuilder peers(String... peers) {
		this.peers = peers;
		return this;
	}

	public NetworkBuilder channels(String... channels) {
		this.channels = channels;
		return this;
	}

	public JsonObject build() throws NetworkBuilderException {
		if (name == null) {
			throw new NetworkBuilderException("The network name is not specified.");
		}
		if (ordererOrg == null) {
			throw new NetworkBuilderException("The network ordererOrg is not specified.");
		}
		if (clientOrg == null) {
			throw new NetworkBuilderException("The client organization is not specified.");
		}

		if (channels == null || channels.length == 0) {
			throw new NetworkBuilderException("The network channels is not specified.");
		}

		if (peerOrgs == null || peerOrgs.length == 0) {
			throw new NetworkBuilderException("The network peerOrgs is not specified.");
		}

		if (peers == null || peers.length == 0) {
			throw new NetworkBuilderException("The network peers is not specified.");
		}
		if (orderers == null || orderers.length == 0) {
			throw new NetworkBuilderException("The network orderers is not specified.");
		}

		if (root == null || !root.exists()) {
			System.out.println(root.getAbsolutePath());
			throw new NetworkBuilderException("The network root directory is not existed.");
		}
		JsonObject root = new JsonObject();

		root.addProperty("name", name);
		root.addProperty("version", "1.0.0");
		root.addProperty("x-type", "hlfv1");

		// client
		JsonObject client = buildClient();
		root.add("client", client);

		// channels
		JsonObject channels = buildChannels();
		root.add("channels", channels);

		// organizations
		JsonObject organizations = buildOrganizations();
		root.add("organizations", organizations);

		// orderers
		JsonObject orderers = buildOrderers();
		root.add("orderers", orderers);

		// peers
		JsonObject peers = buildPeers();
		root.add("peers", peers);

		// certificateAuthorities
		JsonObject certificateAuthorities = buildCertificateAuthorities();
		root.add("certificateAuthorities", certificateAuthorities);
		return root;
	}

	private JsonObject buildCertificateAuthorities() throws NetworkBuilderException {
		JsonObject root = new JsonObject();

//		JsonObject caroot = buildCertificateAuthorityNode(ordererOrg);
//		root.add("ca." + ordererOrg + "." + domain, caroot);

		for (String org : peerOrgs) {
			JsonObject node = buildCertificateAuthorityNode(org);
			root.add("ca." + org + "." + domain, node);
		}

		return root;
	}

	private JsonObject buildCertificateAuthorityNode(String org) throws NetworkBuilderException {
		JsonObject node = new JsonObject();

		node.addProperty("url", "https://" + getUrl(org, "ca") + ":7054");

		JsonObject grpcOptions = new JsonObject();
		grpcOptions.addProperty("ssl-target-name-override", "ca." + org + "." + domain);
		grpcOptions.addProperty("allow-insecure", 0);
		grpcOptions.addProperty("trustServerCertificate", true);
		grpcOptions.addProperty("hostnameOverride", "ca." + org + "." + domain);
		node.add("grpcOptions", grpcOptions);

		JsonObject httpOptions = new JsonObject();
		httpOptions.addProperty("verify", false);
		node.add("httpOptions", httpOptions);

		JsonArray registrar = new JsonArray();

		JsonObject admin = new JsonObject();
		admin.addProperty("enrollId", "admin");
		admin.addProperty("enrollSecret", "adminpw");
		registrar.add(admin);

		node.add("registrar", registrar);
		JsonObject tlsCACerts = new JsonObject();
//		tlsCACerts.addProperty("path", getCaCertPath(org));
		tlsCACerts.addProperty("pem", getCaCertPem(org));
		node.add("tlsCACerts", tlsCACerts);

		return node;
	}

	private String getUrl(String org, String peer) throws NetworkBuilderException {
		Map<String, String> value = urls.get(org);
		if (value == null || value.isEmpty()) {
			throw new NetworkBuilderException("Unnable to find URL for org: " + org);
		}
		if (peer == null) {
			peer = "*";
		}
		if (value.containsKey(peer)) {
			return value.get(peer);
		} else if (value.containsKey(org)) {
			return value.get(org);
		} else if (value.containsKey("*")) {
			return value.get("*");
		}
		throw new NetworkBuilderException("Unnable to find URL for peer: " + peer + " in org: " + org);
	}

	private Integer getPort(String org, String peer, int defaultValue) {
		Map<String, Integer> value = ports.get(org);
		if (value == null) {
			return defaultValue;
		}
		if (peer == null) {
			peer = org;
		}
		if (value.containsKey(peer)) {
			return value.get(peer);
		}
		return defaultValue;
	}

	private JsonObject buildPeers() throws NetworkBuilderException {
		JsonObject root = new JsonObject();

		for (String o : peerOrgs) {
			for (String p : peers) {
				String name = p + "." + o + "." + domain;
				JsonObject node = new JsonObject();

				node.addProperty("url", "grpcs://" + getUrl(o, p) + ":" + getPort(o, p, 7051));
//				node.addProperty("eventUrl", "grpcs://" + getUrl(o, "*") + ":" + getPort("*", "event_" + p, 7053));

				JsonObject grpcOptions = new JsonObject();
				grpcOptions.addProperty("ssl-target-name-override", name);
				grpcOptions.addProperty("grpc.http2.keepalive_time", 15);
				grpcOptions.addProperty("request-timeout", 120001);
//				grpcOptions.addProperty("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);
				grpcOptions.addProperty("hostnameOverride", name);
				node.add("grpcOptions", grpcOptions);

				JsonObject tlsCACerts = new JsonObject();
//				tlsCACerts.addProperty("path", getPeerCertPath(o, p));
				tlsCACerts.addProperty("pem", getPeerCertPem(o, p));
				node.add("tlsCACerts", tlsCACerts);
				root.add(name, node);
			}
		}
		return root;
	}

	private JsonObject buildOrderers() throws NetworkBuilderException {
		JsonObject node = new JsonObject();
		List<String> allOrderers = new ArrayList<>(Arrays.asList(orderers));
//		allOrderers.add(0, ordererOrg);
		for (String org : allOrderers) {
			JsonObject orgNode = new JsonObject();
			orgNode.addProperty("url", "grpcs://" + getUrl(org, null) + ":7050");

			JsonObject grpcOptions = new JsonObject();
			grpcOptions.addProperty("grpc-max-send-message-length", 15);
			grpcOptions.addProperty("grpc.keepalive_time_ms", 360000);
			grpcOptions.addProperty("grpc.keepalive_timeout_ms", 180000);
			grpcOptions.addProperty("hostnameOverride", org + "." + domain);
			orgNode.add("grpcOptions", grpcOptions);

			JsonObject tlsCACerts = new JsonObject();
//			tlsCACerts.addProperty("path", getOrdererCertPath(org));
			tlsCACerts.addProperty("pem", getOrdererCertPem(org));
			orgNode.add("tlsCACerts", tlsCACerts);
			node.add(org + "." + domain, orgNode);
		}
		return node;
	}

	private JsonObject buildOrganizations() throws NetworkBuilderException {
		JsonObject node = new JsonObject();
//		JsonObject ordererOrgNode = buildOrgNode(ordererOrg, 0);
//		node.add(ordererOrg, ordererOrgNode);

		for (String org : peerOrgs) {
			JsonObject child = buildOrgNode(org, peers.length);
			node.add(org, child);
		}
		return node;
	}

	private JsonObject buildOrgNode(String org, int numOfPeers) throws NetworkBuilderException {
		JsonObject ordererOrgNode = new JsonObject();
		ordererOrgNode.addProperty("mspid", org + "MSP");

		JsonArray certificateAuthorities = new JsonArray();
		certificateAuthorities.add("ca." + org + "." + domain);
		ordererOrgNode.add("certificateAuthorities", certificateAuthorities);

		JsonObject adminPrivateKey = new JsonObject();
//		adminPrivateKey.addProperty("path", getAdminPrivateKeyPath(org));
		adminPrivateKey.addProperty("pem", getAdminPrivateKeyPem(org));
		ordererOrgNode.add("adminPrivateKey", adminPrivateKey);

		JsonObject signedCert = new JsonObject();
//		signedCert.addProperty("path", getAdminCertPath(org));
		signedCert.addProperty("pem", getAdminCertPem(org));
		ordererOrgNode.add("signedCert", signedCert);
		if (numOfPeers > 0) {
			JsonArray peers = new JsonArray();
			for (int i = 0; i < numOfPeers; i++) {
				peers.add("peer" + i + "." + org + "." + domain);
			}
			ordererOrgNode.add("peers", peers);
		}
		return ordererOrgNode;
	}

	private String getCaCertPath(String org) throws NetworkBuilderException {
		File file = new File(root,
				"peerOrganizations/" + org + "." + domain + "/ca/ca." + org + "." + domain + "-cert.pem");
		return getRelativePath(file);
	}

	private String getCaCertPem(String org) throws NetworkBuilderException {
		File file = new File(root,
				"peerOrganizations/" + org + "." + domain + "/ca/ca." + org + "." + domain + "-cert.pem");
		try {
			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
//			throw new NetworkBuilderException(e);
			return null;
		}
	}

	private String getOrdererCertPath(String org) throws NetworkBuilderException {
		File file = new File(root, "ordererOrganizations/" + domain + "/orderers/" + org + "." + domain
				+ "/msp/tlscacerts/tlsca." + domain + "-cert.pem");
		return getRelativePath(file);
	}

	private String getOrdererCertPem(String org) throws NetworkBuilderException {
		File file = new File(root, "ordererOrganizations/" + domain + "/orderers/" + org + "." + domain
				+ "/msp/tlscacerts/tlsca." + domain + "-cert.pem");
		try {
			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			throw new NetworkBuilderException(e);
		}
	}

	private String getPeerCertPath(String org, String peer) throws NetworkBuilderException {
		File file = new File(root, "peerOrganizations/" + org + "." + domain + "/peers/" + peer + "." + org + "."
				+ domain + "/msp/tlscacerts/tlsca." + org + "." + domain + "-cert.pem");
		return getRelativePath(file);
	}

	private String getPeerCertPem(String org, String peer) throws NetworkBuilderException {
		File file = new File(root, "peerOrganizations/" + org + "." + domain + "/peers/" + peer + "." + org + "."
				+ domain + "/msp/tlscacerts/tlsca." + org + "." + domain + "-cert.pem");
		try {
			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			throw new NetworkBuilderException(e);
		}
	}

	private String getRelativePath(File file) {
		if (!file.exists()) {
			return null;
//			throw new NetworkBuilderException("Can not find certificate for " + org);
		}

		URI path = file.toURI();
		String absolutePath = root.getAbsolutePath();

		int index = absolutePath.indexOf("/network");
		if (index != -1) {
			String srcParent = absolutePath.substring(0, index);
			path = new File(srcParent).toURI().relativize(path);
		} else {
			path = root.toURI().relativize(path);
		}

		return path.getPath();
	}

	private File getAdminCertPath(String org) throws NetworkBuilderException {
		File file = null;
		if (org.equals(ordererOrg)) {
			file = new File(root, "ordererOrganizations/" + domain + "/" + "users/Admin@" + domain
					+ "/msp/admincerts/Admin@" + domain + "-cert.pem");

		} else {
			file = new File(root, "peerOrganizations/" + org + "." + domain + "/users/Admin@" + org + "." + domain
					+ "/msp/admincerts/Admin@" + org + "." + domain + "-cert.pem");
		}
		if (!file.exists()) {
			if (org.equals(ordererOrg)) {
				file = new File(root, "ordererOrganizations/" + domain + "/" + "users/Admin@" + domain
						+ "/msp/signcerts/Admin@" + domain + "-cert.pem");

			} else {
				file = new File(root, "peerOrganizations/" + org + "." + domain + "/users/Admin@" + org + "." + domain
						+ "/msp/signcerts/Admin@" + org + "." + domain + "-cert.pem");
			}
		}

		return file;
	}

	private String getAdminCertPem(String org) throws NetworkBuilderException {
		File file = getAdminCertPath(org);
		if (!file.exists()) {
			return null;
		}
		try {
			return new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
//			throw new NetworkBuilderException(e);
			return null;
		}
	}

	private String getAdminPrivateKeyPath(String org) throws NetworkBuilderException {
		File dir = null;
		if (org.equals(ordererOrg)) {
			dir = new File(root, "ordererOrganizations/" + domain + "/" + "users/Admin@" + domain + "/msp/keystore");
		} else {
			dir = new File(root,
					"peerOrganizations/" + org + "." + domain + "/users/Admin@" + org + "." + domain + "/msp/keystore");
		}

		if (!dir.exists()) {
			return null;
//			throw new NetworkBuilderException("Can not find private key for " + org);
		}
		File[] listFiles = dir.listFiles();
		if (listFiles.length == 0) {
			return null;
		}
		File keyFile = listFiles[0];
		return getRelativePath(keyFile);
	}

	private String getAdminPrivateKeyPem(String org) throws NetworkBuilderException {
		File dir = null;
		if (org.equals(ordererOrg)) {
			dir = new File(root, "ordererOrganizations/" + domain + "/" + "users/Admin@" + domain + "/msp/keystore");
		} else {
			dir = new File(root,
					"peerOrganizations/" + org + "." + domain + "/users/Admin@" + org + "." + domain + "/msp/keystore");
		}

		if (!dir.exists()) {
			return null;
//			throw new NetworkBuilderException("Can not find private key for " + org);
		}
		File[] listFiles = dir.listFiles();
		if (listFiles.length == 0) {
			return null;
		}
		File keyFile = listFiles[0];
		try {
			return new String(Files.readAllBytes(keyFile.toPath()));
		} catch (IOException e) {
			throw new NetworkBuilderException(e);
		}
	}

	private JsonObject buildChannels() {
		JsonObject channelsNode = new JsonObject();

		for (String channel : channels) {
			JsonObject node = new JsonObject();
			// orderers
			JsonArray orderers = new JsonArray();
//			orderers.add(ordererOrg + "." + domain);
			for (String org : this.orderers) {
				orderers.add(org + "." + domain);
			}
			node.add("orderers", orderers);

			// peers
			JsonObject peersNode = new JsonObject();
			for (String org : peerOrgs) {
				for (String peer : peers) {

					JsonObject o = new JsonObject();
					if (peer.equals("peer0")) {
						o.addProperty("endorsingPeer", true);
						o.addProperty("chaincodeQuery", true);
						o.addProperty("ledgerQuery", true);
						o.addProperty("eventSource", true);
					} else {
						o.addProperty("endorsingPeer", false);
						o.addProperty("chaincodeQuery", true);
						o.addProperty("ledgerQuery", true);
						o.addProperty("eventSource", false);
					}
					peersNode.add(peer + "." + org + "." + domain, o);
				}

			}
			node.add("peers", peersNode);

			// policies
			JsonObject policies = new JsonObject();
			JsonObject queryChannelConfig = new JsonObject();
			queryChannelConfig.addProperty("minResponses", 1);
			queryChannelConfig.addProperty("maxTargets", 1);

			JsonObject retryOpts = new JsonObject();
			retryOpts.addProperty("attempts", 5);
			retryOpts.addProperty("initialBackoff", "500ms");
			retryOpts.addProperty("maxBackoff", "5s");
			retryOpts.addProperty("backoffFactor", "2.0");
			queryChannelConfig.add("retryOpts", retryOpts);
			node.add("policies", policies);
			channelsNode.add(channel, node);
		}

		return channelsNode;
	}

	private JsonObject buildClient() {
		JsonObject client = new JsonObject();

		JsonObject logging = new JsonObject();
		logging.addProperty("level", "debug");
		client.add("logging", logging);

		JsonObject connection = new JsonObject();
		JsonObject timeout = new JsonObject();
		JsonObject peer = new JsonObject();
		peer.addProperty("endorser", 30000);
		peer.addProperty("eventHub", 30000);
		peer.addProperty("eventReg", 30000);
		timeout.add("peer", peer);
		timeout.addProperty("orderer", 30000);

		connection.add("timeout", timeout);
		client.add("connection", connection);

		client.addProperty("organization", clientOrg);

		JsonObject credentialStore = new JsonObject();
		credentialStore.addProperty("path", "tmp/hfc-kvs");

		JsonObject cryptoStore = new JsonObject();
		cryptoStore.addProperty("path", "tmp/hfc-cvs");
		credentialStore.add("cryptoStore", cryptoStore);

		credentialStore.addProperty("wallet", "admin");
		client.add("credentialStore", credentialStore);
		return client;
	}

}
