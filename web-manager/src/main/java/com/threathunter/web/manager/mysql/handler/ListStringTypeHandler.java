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

/**
 * @author Yuan Yi <yi.yuan@threathunter.cn>
 * @since: 2.16
 */
@MappedTypes(List.class)
public class ListStringTypeHandler extends BaseTypeHandler<List<String>> {

    private Gson gson = new GsonBuilder().create();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String str = gson.toJson(parameter);
        ps.setString(i, str);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (str != null || "".equals(str)) {
            String standard = standardize(str);
            List<String> ret = asList(standard);
            return ret;
        }
        return null;
    }

    private List<String> asList(String standard) {
        List<String> ret = (List<String>) gson.fromJson(standard, List.class);
        return ret;
    }

    private String standardize(String str) {
        StringBuilder builder = new StringBuilder();
        if (!str.startsWith("["))
            builder.append("[");
        builder.append(str);
        if (!str.endsWith("]"))
            builder.append("]");
        return builder.toString();
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (str != null || "".equals(str)) {
            String standard = standardize(str);
            List<String> ret = asList(standard);
            return ret;
        }
        return null;
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (str != null || "".equals(str)) {
            String standard = standardize(str);
            List<String> ret = asList(standard);
            return ret;
        }
        return null;
    }
}
