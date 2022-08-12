package com.challenge.musicservice.pojos;

public class RelationUrl {

    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "RelationUrl{" +
                "resource='" + resource + '\'' +
                '}';
    }
}
