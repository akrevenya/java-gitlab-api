package com.synaptix.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabCommitComments {

    public final static String URL = "/comments";

    private GitlabUser author;
    private String path;
    private String note;
    private Integer name;

    @JsonProperty("line_type")
    private String lineType;

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public GitlabUser getAuthor() {
        return author;
    }

    public void setAuthor(GitlabUser author) {
        this.author = author;
    }

}
