package com.synaptix.gitlab.api.services;

import com.synaptix.gitlab.api.GitLabAPI;
import com.synaptix.gitlab.api.models.projects.GitLabProject2;

import java.io.IOException;
import java.io.Serializable;

/**
 * Commits API
 * <p>
 * http://doc.gitlab.com/ce/api/commits.html
 */
public class GitLabAPIProjects {

    private final GitLabAPI gitLabAPI;

    public GitLabAPIProjects(GitLabAPI gitLabAPI) {
        super();
        this.gitLabAPI = gitLabAPI;
    }

    /**
     * Get single project
     * <p>
     * Get a specific project, identified by project ID or NAMESPACE/PROJECT_NAME, which is owned by the authenticated user. If using namespaced projects call make sure that the NAMESPACE/PROJECT_NAME is URL-encoded, eg. /api/v3/projects/diaspora%2Fdiaspora (where / is represented by %2F).
     * <p>
     * GET /projects/:id
     *
     * @param projectId (required) - The ID of a project
     * @return
     * @throws IOException
     */
    public GitLabProject2 getProject(Serializable projectId) throws IOException {
        String tailUrl = String.format("/projects/%s", gitLabAPI.sanitizeProjectId(projectId));
        return gitLabAPI.retrieve().to(tailUrl, GitLabProject2.class);
    }
}
