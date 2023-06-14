package top.sudk.config.mybatis;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.SqlCommandType;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.sudk.bean.MybatisMethod;
import top.sudk.config.mybatis.plugin.MybatisResourcesConfig;
import top.sudk.util.GitUtil;
import top.sudk.util.MybatisUtil;

/**
 * @author we
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Listener {


    @Value("${git.repositoryUrl}")
    private String repositoryUrl;


    @Value("${git.localPath}")
    private String localPath;


    private final MybatisResourcesConfig mybatisResourcesConfig;

    @Async
    @EventListener
    public void event(MybatisMethod mybatisMethod) {
        try {
            String namespace = mybatisMethod.getNamespace();
            String id = mybatisMethod.getId();
            SqlCommandType sqlCommandType = mybatisMethod.getSqlCommandType();
            String xmlContent = mybatisResourcesConfig.getSqlResource(namespace, id, sqlCommandType);
            String xmlPath = mybatisResourcesConfig.getSqlResourcePatch(namespace, id, sqlCommandType);
            if (xmlContent == null || xmlPath == null) {
                return;
            }

            MybatisUtil.setLine(mybatisMethod, xmlContent);


            // 使用 git 插件获取 git 提交记录
            for (RevCommit commit : GitUtil.getCommitHistoryForLines(localPath, repositoryUrl, "src/main/resources/" + xmlPath.split("/classes/|/classes!/")[1], mybatisMethod.getStartLine(), mybatisMethod.getEndLine())) {
                String msg = "Commit Id: " + commit.getName() + "\n" +
                        "Author: " + commit.getAuthorIdent().getName() + "\n" +
                        "Date: " + DateUtil.formatDate(commit.getAuthorIdent().getWhen()) + "\n" +
                        "Commit Message: " + commit.getFullMessage();
                log.info(msg);
            }
        } catch (Exception e) {
            log.error("获取 git 提交记录失败", e);
        }
    }


}
