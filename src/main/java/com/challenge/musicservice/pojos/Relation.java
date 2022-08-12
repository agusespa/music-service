package com.challenge.musicservice.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Relation {

    private String type;

    private RelationUrl url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RelationUrl getUrl() {
        return url;
    }

    public void setUrl(RelationUrl url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(type, relation.type) && Objects.equals(url, relation.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url);
    }

    @Override
    public String toString() {
        return "Relation{" +
                "type='" + type + '\'' +
                ", url=" + url +
                '}';
    }
}
