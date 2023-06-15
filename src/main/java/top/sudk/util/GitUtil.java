package top.sudk.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.setting.dialect.Props;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.python.google.common.collect.Sets;

import java.io.File;
import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public class GitUtil {

    private static String repositoryUrl;

    private static String localPath;

    public static void init() {
        Props props = new Props("application-dev.yml");
        repositoryUrl = props.getStr("repositoryUrl");
        localPath = props.getStr("localPath");
        openOrCreateRepository(localPath, repositoryUrl);
    }


    @SneakyThrows
    public static Repository openOrCreateRepository(String localPath, String remoteUrl) {
        try {
            File localDir = new File(localPath);

            // 检查本地目录是否存在仓库
            if (localDir.exists()) {
                return Git.open(localDir).getRepository();

            } else {
//                CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("PRIVATE-TOKEN", "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");

                // 克隆远程仓库
                return Git.cloneRepository()
//                        .setCredentialsProvider(credentialsProvider)
                        .setURI(remoteUrl)
                        .setDirectory(localDir)
                        .setBranch("main")
                        .setBare(true)
                        .call()
                        .pull()
                        .getRepository();
            }
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }

        return null;
    }


    public static Repository getRepository(String gitDir) {
        try {
            return new RepositoryBuilder().setGitDir(new File(gitDir)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SneakyThrows
    public static List<RevCommit> getFileCommitHistory(Repository repository, String filePath) {
        ObjectId lastCommitId = repository.resolve("HEAD");
        LogCommand logCommand = new Git(repository).log().add(lastCommitId);
        Iterable<RevCommit> commits = logCommand.addPath(filePath).call();
        return ListUtil.toList(commits);
    }


    @SneakyThrows
    public static RevCommit getFileLineCommitHistory(BlameResult blameResult, int lineNumber) {
        return blameResult.getSourceCommit(lineNumber - 1);

    }

    @SneakyThrows
    public static Set<RevCommit> getCommitHistoryForLines(String filePath, int startLine, int endLine) {
        return getCommitHistoryForLines(localPath, repositoryUrl, filePath, startLine, endLine);

    }


    @SneakyThrows
    public static Set<RevCommit> getCommitHistoryForLines(String localPath, String repositoryURI, String filePath, int startLine, int endLine) {
        Repository repository = openOrCreateRepository(localPath, repositoryURI);
        BlameResult blameResult = getBlameResultForFile(repository, filePath);

        return getCommitHistoryForLines(blameResult, startLine, endLine);

    }

    @SneakyThrows
    public static Set<RevCommit> getCommitHistoryForLines(BlameResult blameResult, int startLine, int endLine) {

        Set<RevCommit> commits = Sets.newHashSet();

        for (int i = startLine; i <= endLine; i++) {
            RevCommit commit = getFileLineCommitHistory(blameResult, i);
            commits.add(commit);
        }

        return commits;

    }


    private static BlameResult getBlameResultForFile(Repository repository, String filePath) throws Exception {
        BlameCommand blameCommand = new Git(repository).blame();
        blameCommand.setFilePath(filePath);
        return blameCommand.call();
    }

    private static int findMethodLineNumber(BlameResult blameResult, String methodName) {
        for (int lineNumber = 0; lineNumber < blameResult.getResultContents().size(); lineNumber++) {
            String line = blameResult.getResultContents().getString(lineNumber);
            if (line.contains(methodName)) {
                return lineNumber + 1; // Line numbers are 1-based
            }
        }
        return -1; // Method not found
    }

}
