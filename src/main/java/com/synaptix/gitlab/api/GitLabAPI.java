package com.synaptix.gitlab.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synaptix.gitlab.api.http.GitlabHTTPRequestor;
import com.synaptix.gitlab.api.models.GitlabCommitComments;
import com.synaptix.gitlab.api.models.GitlabCommitDiff;
import com.synaptix.gitlab.api.models.GitlabCommitStatus;
import com.synaptix.gitlab.api.models.GitlabProject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

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
    private AuthMethod authMethod;
    private boolean ignoreCertificateErrors = false;

    private GitLabAPI(String hostUrl, String apiToken, TokenType tokenType, AuthMethod method) {
        this.hostUrl = hostUrl.endsWith("/") ? hostUrl.replaceAll("/$", "") : hostUrl;
        this.apiToken = apiToken;
        this.tokenType = tokenType;
        this.authMethod = method;
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

    public GitlabHTTPRequestor retrieve() {
        return new GitlabHTTPRequestor(this).authenticate(apiToken, tokenType, authMethod);
    }

    public GitlabHTTPRequestor dispatch() {
        return new GitlabHTTPRequestor(this).authenticate(apiToken, tokenType, authMethod).method("POST");
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

    // List commit diffs for a project ID and commit hash
    // GET /projects/:id/repository/commits/:sha/diff

    /**
     * Get the diff of a commit
     * <p>
     * GET /projects/:id/repository/commits/:sha/diff
     *
     * @param projectId  (required) - The ID of a project
     * @param commitHash (required) - The commit SHA
     * @return
     * @throws IOException
     */
    public List<GitlabCommitDiff> getCommitDiffs(Serializable projectId, String commitHash) throws IOException {
        String tailUrl = GitlabProject.URL + "/" + sanitizeProjectId(projectId) + "/repository/commits/" + commitHash + GitlabCommitDiff.URL;
        GitlabCommitDiff[] diffs = retrieve().to(tailUrl, GitlabCommitDiff[].class);
        return Arrays.asList(diffs);
    }

    /**
     * Post comment to commit
     * <p>
     * POST /projects/:id/repository/commits/:sha/comments
     *
     * @param projectId  (required) - The ID of a project
     * @param commitHash (required) - The commit SHA
     * @param note       (required) - Text of comment
     * @param path       (optional) - The file path
     * @param line       (optional) - The line number
     * @param lineType   (optional) - The line type (new or old)
     * @return
     * @throws IOException
     */
    public GitlabCommitComments createCommitComments(Serializable projectId, String commitHash, String note, String path, Integer line, String lineType) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits/%s/comments", sanitizeProjectId(projectId), commitHash);
        return dispatch().with("note", note).with("path", path).with("line", line).with("line_type", lineType).to(tailUrl, GitlabCommitComments.class);
    }

    /**
     * Post the status to commit
     * <p>
     * POST /projects/:id/statuses/:sha
     *
     * @param projectId   (required) - The ID of a project
     * @param commitHash  (required) - The commit SHA
     * @param state       (required) - The state of the status. Can be: pending, running, success, failed, canceled
     * @param ref         (optional) - The ref (branch or tag) to which the status refers
     * @param name        (optional) - The label to differentiate this status from the status of other systems. Default: "default"
     * @param targetUrl   (optional) - The target URL to associate with this status
     * @param description (optional) - The short description of the status
     * @return
     * @throws IOException
     */
    public GitlabCommitStatus createCommitStatus(Serializable projectId, String commitHash, String state, String ref, String name, String targetUrl, String description) throws IOException {
        String tailUrl = String.format("/projects/%s/statuses/%s", sanitizeProjectId(projectId), commitHash);
        return dispatch().with("state", state).with("ref", ref).with("name", name).with("target_url", targetUrl).with("description", description).to(tailUrl, GitlabCommitStatus.class);
    }

    private String sanitizeProjectId(Serializable projectId) {
        if (!(projectId instanceof String) && !(projectId instanceof Integer)) {
            throw new IllegalArgumentException();
        }

        try {
            return URLEncoder.encode(String.valueOf(projectId), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException((e));
        }
    }
}
