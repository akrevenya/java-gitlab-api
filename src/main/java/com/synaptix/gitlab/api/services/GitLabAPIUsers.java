package com.synaptix.gitlab.api.services;

import com.synaptix.gitlab.api.GitLabAPI;
import com.synaptix.gitlab.api.models.GitlabUser;

import java.io.IOException;

/**
 * Users API
 * <p>
 * http://doc.gitlab.com/ce/api/users.html
 */
public class GitLabAPIUsers {

    private final GitLabAPI gitLabAPI;

    public GitLabAPIUsers(GitLabAPI gitLabAPI) {
        super();
        this.gitLabAPI = gitLabAPI;
    }

    /**
     * Current user
     * <p>
     * Gets currently authenticated user.
     * <p>
     * GET /user
     *
     * @return
     * @throws IOException
     */
    public GitlabUser getUser() throws IOException {
        String tailUrl = String.format("/user");
        return gitLabAPI.retrieve().to(tailUrl, GitlabUser.class);
    }
}
