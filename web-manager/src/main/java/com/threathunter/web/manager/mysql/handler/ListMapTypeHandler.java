package com.threathunter.web.manager.mysql.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@MappedTypes(List.class)
public class ListMapTypeHandler extends BaseTypeHandler<List<Map<String, Object>>> {
    private Gson gson = new GsonBuilder().create();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Map<String, Object>> parameter, JdbcType jdbcType) throws SQLException {
        String s = gson.toJson(parameter);
        ps.setString(i, s);
    }

    @Override
    public List<Map<String, Object>> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        if (string != null) {
            List ret = gson.fromJson(string, List.class);
            return ret;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        if (string != null) {
            List ret = gson.fromJson(string, List.class);
            return ret;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        if (string != null) {
            List ret = gson.fromJson(string, List.class);
            return ret;
        }
        return null;
    }
}
