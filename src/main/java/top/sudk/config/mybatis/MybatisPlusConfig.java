package top.sudk.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.sudk.config.mybatis.plugin.QueryUserPlugin;
import top.sudk.config.mybatis.plugin.SlowQueryNotificationPlugin;

import java.util.Properties;

/**
 * @author we
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public SlowQueryNotificationPlugin slowQueryNotificationPlugin() {
        SlowQueryNotificationPlugin slowQueryNotificationPlugin = new SlowQueryNotificationPlugin();
        Properties properties = new Properties();
        properties.setProperty("slowQueryThreshold", "1");
        slowQueryNotificationPlugin.setProperties(properties);
        return slowQueryNotificationPlugin;
    }


    @Bean
    public QueryUserPlugin queryUserPlugin() {
        return new QueryUserPlugin();
    }

}
