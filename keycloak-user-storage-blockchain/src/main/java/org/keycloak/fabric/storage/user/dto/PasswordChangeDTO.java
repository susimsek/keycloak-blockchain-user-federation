package org.keycloak.fabric.storage.user.dto;




public class PasswordChangeDTO {

    public PasswordChangeDTO() {
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordChangeDTO{" +
                "password='" + password + '\'' +
                '}';
    }
}
