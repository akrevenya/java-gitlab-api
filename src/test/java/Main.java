import com.synaptix.gitlab.api.GitLabAPI2;
import com.synaptix.gitlab.api.models.GitlabCommitDiff;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        GitLabAPI2 gitLabAPI2 = GitLabAPI2.connect("https://gitlab.synaptix-labs.com", "quzzczpqNx3dQxFBCFyt");

        // Comments
        //gitlabAPI.createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test\n\nEncore un test !!!", null, null, null);
        //gitlabAPI.createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test sur ligne\n\nEncore un test !!!", ".gitlab-ci.yml", 45, "new");

        // Statuses
        //gitlabAPI.createCommitStatus(49,"8056d2dc42680efe887367f163e851570493b8b8","running",null,"qa/sonarqube",null,null);
        //Thread.sleep(20000);
        //gitlabAPI.createCommitStatus(49,"8056d2dc42680efe887367f163e851570493b8b8","failed",null,"qa/sonarqube",null,"Quality gate fail");

        // Commit Diff
        List<GitlabCommitDiff> diffs = gitLabAPI2.getCommitDiffs(49, "8056d2dc42680efe887367f163e851570493b8b8");
        diffs.stream().map(diff -> diff.getDiff()).forEach(System.out::println);
    }
}
