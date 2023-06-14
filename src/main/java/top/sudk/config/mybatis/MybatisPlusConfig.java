package top.sudk.config.mybatis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import top.sudk.config.mybatis.plugin.MybatisResourcesConfig;
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
        properties.setProperty("slowQueryThreshold", "2000");
        slowQueryNotificationPlugin.setProperties(properties);
        return slowQueryNotificationPlugin;
    }

}