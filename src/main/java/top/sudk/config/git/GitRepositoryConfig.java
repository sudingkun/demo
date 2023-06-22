package top.sudk.config.git;

import org.springframework.context.annotation.Configuration;
import top.sudk.util.GitUtil;

import javax.annotation.PostConstruct;

/**
 * @author sudk
 */
@Configuration
public class GitRepositoryConfig {

    @PostConstruct
    public void init() {
        GitUtil.init();
    }
}
