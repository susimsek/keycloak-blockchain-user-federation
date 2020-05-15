package org.keycloak.fabric.storage.user.dto;




public class CountDTO {

    public CountDTO() {
    }

    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CountDTO{" +
                "count=" + count +
                '}';
    }
}
