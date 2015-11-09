package com.synaptix.gitlab.api.services;

import com.synaptix.gitlab.api.GitLabAPI;
import com.synaptix.gitlab.api.http.Query;
import com.synaptix.gitlab.api.models.projects.GitLabProject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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
     * List projects
     * <p>
     * Get a list of projects accessible by the authenticated user.
     * <p>
     * GET /projects
     *
     * @param archived       (optional) - if passed, limit by archived status
     * @param orderBy        (optional) - Return requests ordered by id, name, path, created_at, updated_at or last_activity_at fields. Default is created_at
     * @param sort           (optional) - Return requests sorted in asc or desc order. Default is desc
     * @param search         (optional) - Return list of authorized projects according to a search criteria
     * @param ciEnabledFirst (optional) - Return projects ordered by ci_enabled flag. Projects with enabled GitLab CI go first
     * @return
     * @throws IOException
     */
    public List<GitLabProject> getProjects(Boolean archived, String orderBy, String sort, String search, Boolean ciEnabledFirst) throws IOException {
        return getProjects(null, archived, orderBy, sort, search, ciEnabledFirst);
    }

    /**
     * List owned projects
     * <p>
     * Get a list of projects which are owned by the authenticated user.
     * <p>
     * GET /projects/owned
     *
     * @param archived       (optional) - if passed, limit by archived status
     * @param orderBy        (optional) - Return requests ordered by id, name, path, created_at, updated_at or last_activity_at fields. Default is created_at
     * @param sort           (optional) - Return requests sorted in asc or desc order. Default is desc
     * @param search         (optional) - Return list of authorized projects according to a search criteria
     * @param ciEnabledFirst (optional) - Return projects ordered by ci_enabled flag. Projects with enabled GitLab CI go first
     * @return
     * @throws IOException
     */
    public List<GitLabProject> getProjectOwneds(Boolean archived, String orderBy, String sort, String search, Boolean ciEnabledFirst) throws IOException {
        return getProjects("/owned", archived, orderBy, sort, search, ciEnabledFirst);
    }

    /**
     * List ALL projects
     * <p>
     * Get a list of all GitLab projects (admin only).
     * <p>
     * GET /projects/all
     *
     * @param archived       (optional) - if passed, limit by archived status
     * @param orderBy        (optional) - Return requests ordered by id, name, path, created_at, updated_at or last_activity_at fields. Default is created_at
     * @param sort           (optional) - Return requests sorted in asc or desc order. Default is desc
     * @param search         (optional) - Return list of authorized projects according to a search criteria
     * @param ciEnabledFirst (optional) - Return projects ordered by ci_enabled flag. Projects with enabled GitLab CI go first
     * @return
     * @throws IOException
     */
    public List<GitLabProject> getProjectAlls(Boolean archived, String orderBy, String sort, String search, Boolean ciEnabledFirst) throws IOException {
        return getProjects("/all", archived, orderBy, sort, search, ciEnabledFirst);
    }

    private List<GitLabProject> getProjects(String prefix, Boolean archived, String orderBy, String sort, String search, Boolean ciEnabledFirst) throws IOException {
        String parameters = new Query().appendIf("archived", archived).appendIf("order_by", orderBy).appendIf("sort", sort).appendIf("search", gitLabAPI.sanitize(search))
                .appendIf("ciEnabledFirst", ciEnabledFirst).toString();

        String tailUrl = "/projects";
        if (prefix != null) {
            tailUrl += prefix;
        }
        tailUrl += parameters;
        return gitLabAPI.retrieve().getAll(tailUrl, GitLabProject[].class);
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
    public GitLabProject getProject(Serializable projectId) throws IOException {
        String tailUrl = String.format("/projects/%s", gitLabAPI.sanitize(projectId));
        return gitLabAPI.retrieve().to(tailUrl, GitLabProject.class);
    }

    /**
     * Search for projects by name
     * <p>
     * Search for projects by name which are accessible to the authenticated user.
     * <p>
     * GET /projects/search/:query
     *
     * @param query   (required) - A string contained in the project name
     * @param perPage (optional) - number of projects to return per page
     * @param page    (optional) - the page to retrieve
     * @param orderBy (optional) - Return requests ordered by id, name, created_at or last_activity_at fields
     * @param sort    (optional) - Return requests sorted in asc or desc order
     * @return
     * @throws IOException
     */
    public List<GitLabProject> getProjectsSearchByName(String query, Integer perPage, Integer page, String orderBy, String sort) throws IOException {
        String parameters = new Query().appendIf("per_page", perPage).appendIf("page", page).appendIf("orderBy", orderBy).appendIf("sort", sort).toString();
        String tailUrl = String.format("/projects/search/%s%s", gitLabAPI.sanitize(query), parameters);
        return gitLabAPI.retrieve().getAll(tailUrl, GitLabProject[].class);
    }
}
