Java Gitlab API
===============

[![build status](https://gitlab.synaptix-labs.com/ci/projects/11/status.png?ref=master)](https://gitlab.synaptix-labs.com/ci/projects/11?ref=master)

Fork de https://github.com/timols/java-gitlab-api

# Usage

``` xml
<dependency>
   <groupId>com.synaptix</groupId>
   <artifactId>java-gitlab-api</artifactId>
   <version>1.3.0</version>
</dependency>
```

``` java
GitLabAPI gitLabAPI = GitLabAPI.connect(GITLAB_URL, USER_TOKEN);

List<GitLabCommitDiff> diffs = gitLabAPI.getGitLabAPICommits().getCommitDiffs(PROJECT_ID, COMMIT_SHA);
```