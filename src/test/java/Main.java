import com.synaptix.gitlab.api.GitlabAPI;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        GitlabAPI gitlabAPI = GitlabAPI.connect("https://gitlab.synaptix-labs.com", "quzzczpqNx3dQxFBCFyt");

        // Comments
        //gitlabAPI.createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test\n\nEncore un test !!!", null, null, null);
        //gitlabAPI.createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test sur ligne\n\nEncore un test !!!", ".gitlab-ci.yml", 45, "new");

        // Statuses
        gitlabAPI.createCommitStatus(49,"8056d2dc42680efe887367f163e851570493b8b8","running",null,"qa/sonarqube",null,null);
        Thread.sleep(20000);
        gitlabAPI.createCommitStatus(49,"8056d2dc42680efe887367f163e851570493b8b8","failed",null,"qa/sonarqube",null,"Quality gate fail");
    }
}
