package top.sudk.config.mybatis.plugin;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import top.sudk.bean.RequestDataHelper;

import java.lang.reflect.Field;
import java.sql.Connection;

/**
 * @author sudingkun
 */
@Slf4j
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        }
)
public class QueryUserPlugin implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取原始的 StatementHandler 对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        String userName = RequestDataHelper.get();
        if (StrUtil.isBlank(userName)) {
            return invocation.proceed();
        }
        String sql = boundSql.getSql() + "  /* queryUser: " + userName + "  */";


        // 修改 BoundSql 对象中的 SQL 语句
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, sql);

        return invocation.proceed();
    }


}
