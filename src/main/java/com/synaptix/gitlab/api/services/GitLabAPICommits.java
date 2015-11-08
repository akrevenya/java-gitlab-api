package com.synaptix.gitlab.api.services;

import com.synaptix.gitlab.api.GitLabAPI;
import com.synaptix.gitlab.api.models.commits.GitlabCommit2;
import com.synaptix.gitlab.api.models.commits.GitlabCommitComments2;
import com.synaptix.gitlab.api.models.commits.GitlabCommitDiff2;
import com.synaptix.gitlab.api.models.commits.GitlabCommitStatus2;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Commits API
 * <p>
 * http://doc.gitlab.com/ce/api/commits.html
 */
public class GitLabAPICommits {

    private final GitLabAPI gitLabAPI;

    public GitLabAPICommits(GitLabAPI gitLabAPI) {
        super();
        this.gitLabAPI = gitLabAPI;
    }

    /**
     * List repository commits
     * <p>
     * Get a list of repository commits in a project.
     * <p>
     * GET /projects/:id/repository/commits
     *
     * @param projectId (required) - The ID of a project
     * @return
     * @throws IOException
     */
    public List<GitlabCommit2> getAllCommits(Serializable projectId) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits", gitLabAPI.sanitizeProjectId(projectId));
        return gitLabAPI.retrieve().getAll(tailUrl, GitlabCommit2[].class);
    }

    /**
     * Get a single commit
     * <p>
     * Get a specific commit identified by the commit hash or name of a branch or tag.
     * <p>
     * GET /projects/:id/repository/commits/:sha
     *
     * @param projectId  (required) - The ID of a project
     * @param commitHash (required) - The commit SHA
     * @return
     * @throws IOException
     */
    public GitlabCommit2 getCommit(Serializable projectId, String commitHash) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits/%s", gitLabAPI.sanitizeProjectId(projectId), commitHash);
        return gitLabAPI.retrieve().to(tailUrl, GitlabCommit2.class);
    }

    /**
     * Get the diff of a commit
     * <p>
     * Get the diff of a commit in a project.
     * <p>
     * GET /projects/:id/repository/commits/:sha/diff
     *
     * @param projectId  (required) - The ID of a project
     * @param commitHash (required) - The commit SHA
     * @return
     * @throws IOException
     */
    public List<GitlabCommitDiff2> getCommitDiffs(Serializable projectId, String commitHash) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits/%s/diff", gitLabAPI.sanitizeProjectId(projectId), commitHash);
        GitlabCommitDiff2[] diffs = gitLabAPI.retrieve().to(tailUrl, GitlabCommitDiff2[].class);
        return Arrays.asList(diffs);
    }

    /**
     * Get the comments of a commit
     * <p>
     * Get the comments of a commit in a project.
     * <p>
     * GET /projects/:id/repository/commits/:sha/comments
     *
     * @param projectId  (required) - The ID of a project
     * @param commitHash (required) - The commit SHA
     * @return
     * @throws IOException
     */
    public List<GitlabCommitComments2> getCommitComments(Serializable projectId, String commitHash) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits/%s/comments", gitLabAPI.sanitizeProjectId(projectId), commitHash);
        GitlabCommitComments2[] diffs = gitLabAPI.retrieve().to(tailUrl, GitlabCommitComments2[].class);
        return Arrays.asList(diffs);
    }

    /**
     * Post comment to commit
     * <p>
     * Adds a comment to a commit. Optionally you can post comments on a specific line of a commit. Therefor both path, line_new and line_old are required.
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
    public GitlabCommitComments2 postCommitComments(Serializable projectId, String commitHash, String note, String path, Integer line, String lineType) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits/%s/comments", gitLabAPI.sanitizeProjectId(projectId), commitHash);
        return gitLabAPI.dispatch().with("note", note).with("path", path).with("line", line).with("line_type", lineType).to(tailUrl, GitlabCommitComments2.class);
    }

    /**
     * Get the status of a commit
     * <p>
     * Get the statuses of a commit in a project.
     * <p>
     * GET /projects/:id/repository/commits/:sha/statuses
     *
     * @param projectId  (required) - The ID of a project
     * @param commitHash (required) - The commit SHA
     * @return
     * @throws IOException
     */
    public List<GitlabCommitStatus2> getCommitStatuses(Serializable projectId, String commitHash) throws IOException {
        String tailUrl = String.format("/projects/%s/repository/commits/%s/statuses", gitLabAPI.sanitizeProjectId(projectId), commitHash);
        GitlabCommitStatus2[] statuses = gitLabAPI.retrieve().to(tailUrl, GitlabCommitStatus2[].class);
        return Arrays.asList(statuses);
    }

    /**
     * Post the status to commit
     * <p>
     * Adds or updates a status of a commit.
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
    public GitlabCommitStatus2 postCommitStatus(Serializable projectId, String commitHash, String state, String ref, String name, String targetUrl, String description) throws IOException {
        String tailUrl = String.format("/projects/%s/statuses/%s", gitLabAPI.sanitizeProjectId(projectId), commitHash);
        return gitLabAPI.dispatch().with("state", state).with("ref", ref).with("name", name).with("target_url", targetUrl).with("description", description).to(tailUrl, GitlabCommitStatus2.class);
    }
}
