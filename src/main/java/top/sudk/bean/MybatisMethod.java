package top.sudk.bean;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author sudingkun
 */
@Data
public class MybatisMethod {
    private String namespace;
    private String id;
    private SqlCommandType sqlCommandType;
    private String sql;
    private int startLine;
    private int endLine;
}