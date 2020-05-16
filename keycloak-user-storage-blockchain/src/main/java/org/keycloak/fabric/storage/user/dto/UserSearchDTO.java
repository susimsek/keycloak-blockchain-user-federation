package org.keycloak.fabric.storage.user.dto;


public class UserSearchDTO {

    public UserSearchDTO() {
    }

    private String search;
    private int firstResult;
    private int maxResults;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    @Override
    public String toString() {
        return "UserSearchDTO{" +
                "search='" + search + '\'' +
                ", firstResult=" + firstResult +
                ", maxResults=" + maxResults +
                '}';
    }
}
