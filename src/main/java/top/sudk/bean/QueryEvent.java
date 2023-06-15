package top.sudk.bean;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author sudingkun
 */
@Data
public class QueryEvent {
    private String namespace;
    private String id;
    private SqlCommandType sqlCommandType;
    private String sql;
    private long sqlTime;
    private int startLine;
    private int endLine;
}
