package top.sudk.config.mybatis.plugin;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.sudk.bean.QueryEvent;

import java.util.Properties;

/**
 * @author sudingkun
 */
@Slf4j
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@RequiredArgsConstructor
public class SlowQueryNotificationPlugin implements Interceptor {

    private Integer slowQueryThreshold;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        // 获取 mappedStatement
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        String namespace = mappedStatement.getId();
        String id = namespace.substring(namespace.lastIndexOf(".") + 1);
        namespace = namespace.substring(0, namespace.lastIndexOf("."));
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        TimeInterval timer = DateUtil.timer();
        Object result = invocation.proceed();
        long sqlTime = timer.intervalRestart();
        if (sqlTime > slowQueryThreshold) {
            String sql = mappedStatement.getBoundSql(parameter).getSql();
            QueryEvent queryEvent = new QueryEvent();
            queryEvent.setId(id);
            queryEvent.setNamespace(namespace);
            queryEvent.setSql(sql);
            queryEvent.setSqlTime(sqlTime);
            queryEvent.setSqlCommandType(sqlCommandType);
            SpringUtil.publishEvent(queryEvent);
        }


        return result;
    }

    @Override
    public void setProperties(Properties properties) {
        this.slowQueryThreshold = Integer.valueOf(properties.getProperty("slowQueryThreshold"));
    }


}
