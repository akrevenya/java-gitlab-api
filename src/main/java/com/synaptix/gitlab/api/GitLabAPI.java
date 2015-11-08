package com.synaptix.gitlab.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synaptix.gitlab.api.http.GitLab2HTTPRequestor;
import com.synaptix.gitlab.api.services.GitLabAPICommits;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Gitlab API Wrapper class
 *
 * @author &#064;timols (Tim O)
 */
public class GitLabAPI {

    public static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final String API_NAMESPACE = "/api/v3";

    private final String hostUrl;
    private final String apiToken;
    private final TokenType tokenType;
    private final AuthMethod authMethod;

    private final GitLabAPICommits gitLabAPICommits;

    private boolean ignoreCertificateErrors = false;

    private GitLabAPI(String hostUrl, String apiToken, TokenType tokenType, AuthMethod method) {
        this.hostUrl = hostUrl.endsWith("/") ? hostUrl.replaceAll("/$", "") : hostUrl;
        this.apiToken = apiToken;
        this.tokenType = tokenType;
        this.authMethod = method;

        this.gitLabAPICommits = new GitLabAPICommits(this);
    }

    public static GitLabAPI connect(String hostUrl, String apiToken) {
        return new GitLabAPI(hostUrl, apiToken, TokenType.PRIVATE_TOKEN, AuthMethod.HEADER);
    }

    public static GitLabAPI connect(String hostUrl, String apiToken, TokenType tokenType) {
        return new GitLabAPI(hostUrl, apiToken, tokenType, AuthMethod.HEADER);
    }

    public static GitLabAPI connect(String hostUrl, String apiToken, TokenType tokenType, AuthMethod method) {
        return new GitLabAPI(hostUrl, apiToken, tokenType, method);
    }

    public GitLabAPI ignoreCertificateErrors(boolean ignoreCertificateErrors) {
        this.ignoreCertificateErrors = ignoreCertificateErrors;
        return this;
    }

    public GitLab2HTTPRequestor retrieve() {
        return new GitLab2HTTPRequestor(this).authenticate(apiToken, tokenType, authMethod);
    }

    public GitLab2HTTPRequestor dispatch() {
        return new GitLab2HTTPRequestor(this).authenticate(apiToken, tokenType, authMethod).method("POST");
    }

    public boolean isIgnoreCertificateErrors() {
        return ignoreCertificateErrors;
    }

    public URL getAPIUrl(String tailAPIUrl) throws IOException {
        if (!tailAPIUrl.startsWith("/")) {
            tailAPIUrl = "/" + tailAPIUrl;
        }
        return new URL(hostUrl + API_NAMESPACE + tailAPIUrl);
    }

    public URL getUrl(String tailAPIUrl) throws IOException {
        if (!tailAPIUrl.startsWith("/")) {
            tailAPIUrl = "/" + tailAPIUrl;
        }

        return new URL(hostUrl + tailAPIUrl);
    }

    public String sanitizeProjectId(Serializable projectId) {
        if (!(projectId instanceof String) && !(projectId instanceof Integer)) {
            throw new IllegalArgumentException();
        }

        try {
            return URLEncoder.encode(String.valueOf(projectId), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException((e));
        }
    }

    public GitLabAPICommits getGitLabAPICommits() {
        return gitLabAPICommits;
    }
}
