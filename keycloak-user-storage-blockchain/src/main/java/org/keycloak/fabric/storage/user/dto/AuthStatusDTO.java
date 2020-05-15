package org.keycloak.fabric.storage.user.dto;




public class AuthStatusDTO {

    public AuthStatusDTO() {
    }

    private boolean authenticated;

    public boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public String toString() {
        return "AuthStatusDTO{" +
                "authenticated=" + authenticated +
                '}';
    }
}
