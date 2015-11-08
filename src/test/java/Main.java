import com.synaptix.gitlab.api.GitLabAPI;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        GitLabAPI gitLabAPI = GitLabAPI.connect("https://gitlab.synaptix-labs.com", "quzzczpqNx3dQxFBCFyt");

        // Comments
        //gitlabAPI.getGitLabAPICommits().createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test\n\nEncore un test !!!", null, null, null);
        //gitlabAPI.getGitLabAPICommits().createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test sur ligne\n\nEncore un test !!!", ".gitlab-ci.yml", 45, "new");
        //gitlabAPI.getGitLabAPICommits().createCommitComments(49, "8056d2dc42680efe887367f163e851570493b8b8", "# Test sur ligne\n\nEncore un test !!!", ".gitlab-ci.yml", 45, "new");

        // Statuses
        //gitlabAPI.getGitLabAPICommits().createCommitStatus(49,"8056d2dc42680efe887367f163e851570493b8b8","running",null,"qa/sonarqube",null,null);
        //Thread.sleep(20000);
        //gitlabAPI.getGitLabAPICommits().createCommitStatus(49,"8056d2dc42680efe887367f163e851570493b8b8","failed",null,"qa/sonarqube",null,"Quality gate fail");

        // Commit Diff
        /*List<GitLabCommitDiff> diffs = gitLabAPI.getGitLabAPICommits().getCommitDiffs(49, "2738e4e45b4bbfc3cdd10376f99d60cf91c45258");
        diffs.stream().forEach(diff -> {
            if ("task-manager-jpa/src/main/java/com/synaptix/taskmanager/jpa/JPATaskManagerReaderWriter.java".equals(diff.getNewPath())) {
                Map<Integer, Integer> patchLocationMapping = new HashMap<>();
                try {
                    processPatch(patchLocationMapping, diff.getDiff());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(patchLocationMapping);
            }
        });*/

        // Project
        System.out.println(gitLabAPI.getGitLabAPIProjects().getProject(49));
    }

    private static void processPatch(Map<Integer, Integer> patchLocationMapping, String patch) throws IOException {
        int currentLine = -1;
        int patchLocation = 0;
        for (String line : IOUtils.readLines(new StringReader(patch))) {
            if (line.startsWith("@")) {
                // http://en.wikipedia.org/wiki/Diff_utility#Unified_format
                Matcher matcher = Pattern.compile("@@\\p{Space}-[0-9]+(?:,[0-9]+)?\\p{Space}\\+([0-9]+)(?:,[0-9]+)?\\p{Space}@@.*").matcher(line);
                if (!matcher.matches()) {
                    throw new IllegalStateException("Unable to parse patch line " + line + "\nFull patch: \n" + patch);
                }
                currentLine = Integer.parseInt(matcher.group(1));
            } else if (line.startsWith("-")) {
                // Skip removed lines
            } else if (line.startsWith("+") || line.startsWith(" ")) {
                // Count added and unmodified lines
                patchLocationMapping.put(currentLine, patchLocation);
                currentLine++;
            } else if (line.startsWith("\\")) {
                // I'm only aware of \ No newline at end of file
                // Ignore
            }
            patchLocation++;
        }
    }
}
