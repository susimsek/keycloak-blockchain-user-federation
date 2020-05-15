package org.keycloak.fabric.network;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NetworkGenerator {

	public static void main(String[] args) {
		String domain = "example.com";
		String my_ip_adress = "192.168.1.35";
		String[] clients = { "Org1", "Org2" };

		File root = new File("").getAbsoluteFile();

		for (int i = 0; i < clients.length; i++) {
			try {

				JsonObject json = new NetworkBuilder(domain)
						.name("first-network").clientOrg(clients[i])
						.ordererOrg("orderer")
						.orderers("orderer")
						.peerOrgs(clients).peers("peer0", "peer1")
						.channels("mychannel").root(new File(root, "first-network/crypto-config"))
						.url("Org1", "peer0", my_ip_adress)
						.url("Org1", "peer1", my_ip_adress)
						.url("Org2", "peer0", my_ip_adress)
						.url("Org2", "peer1", my_ip_adress)
						.url("Org1", "ca", my_ip_adress)
						.url("Org2", "ca", my_ip_adress)
						.url("orderer", "*", my_ip_adress)
						.port("Org1", "peer0", 7051)
						.port("Org1", "peer1", 8051)
						.port("Org2", "peer0", 9051)
						.port("Org2", "peer1", 10051)
						.build();

				Gson gson = new Gson();
				String value = gson.toJson(json);

				Yaml yaml = new Yaml();
				Object map = yaml.load(value);
				String yvalue = yaml.dump(map);

				File file = new File(root, "src/main/resources/network/connection-" + clients[i] + ".yml");
				if (!file.exists()) {
					Files.write(file.toPath(), yvalue.getBytes(), StandardOpenOption.CREATE_NEW,
							StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
				} else {
					Files.write(file.toPath(), yvalue.getBytes(), StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.WRITE);
				}

			} catch (NetworkBuilderException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < clients.length; i++) {
			try {

				JsonObject json = new NetworkBuilder(domain)
						.name("first-network").clientOrg(clients[i])
						.ordererOrg("orderer")
						.orderers("orderer")
						.peerOrgs(clients).peers("peer0", "peer1")
						.channels("mychannel").root(new File(root, "first-network/crypto-config"))
						.url("Org1", "peer0", "peer0.Org1.example.com")
						.url("Org1", "peer1", "peer1.Org1.example.com")
						.url("Org2", "peer0", "peer0.Org2.example.com")
						.url("Org2", "peer1", "peer1.Org2.example.com")
						.url("Org1", "ca", "ca0")
						.url("Org2", "ca", "ca1")
						.url("orderer", "*", "orderer.example.com")
						.port("Org1", "peer0", 7051)
						.port("Org1", "peer1", 8051)
						.port("Org2", "peer0", 9051)
						.port("Org2", "peer1", 10051)
						.build();

				Gson gson = new Gson();
				String value = gson.toJson(json);

				Yaml yaml = new Yaml();
				Object map = yaml.load(value);
				String yvalue = yaml.dump(map);

				File file = new File(root, "src/main/resources/network/connection-prod-" + clients[i] + ".yml");
				if (!file.exists()) {
					Files.write(file.toPath(), yvalue.getBytes(), StandardOpenOption.CREATE_NEW,
							StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
				} else {
					Files.write(file.toPath(), yvalue.getBytes(), StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.WRITE);
				}

			} catch (NetworkBuilderException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
