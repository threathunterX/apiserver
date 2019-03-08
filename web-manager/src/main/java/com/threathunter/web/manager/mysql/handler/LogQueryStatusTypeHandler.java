package com.threathunter.web.manager.mysql.handler;

import com.threathunter.web.manager.mysql.domain.LogQueryStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@MappedTypes(LogQueryStatus.class)
public class LogQueryStatusTypeHandler extends BaseTypeHandler<LogQueryStatus> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LogQueryStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public LogQueryStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (str != null || "".equals(str)) {
            return LogQueryStatus.toType(str);
        }
        return null;
    }

    @Override
    public LogQueryStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (str != null || "".equals(str)) {
            return LogQueryStatus.toType(str);
        }
        return null;
    }

    @Override
    public LogQueryStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (str != null || "".equals(str)) {
            return LogQueryStatus.toType(str);
        }
        return null;
    }
}
