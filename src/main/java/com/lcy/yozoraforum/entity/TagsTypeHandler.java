package com.lcy.yozoraforum.entity;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将JAVA的数据类型转换数据库能看懂的数据
 */

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Tags.class)
public class TagsTypeHandler extends BaseTypeHandler<Tags> {
    /**
     * 把Java对象parameter转换成JDBC可以识别的类型，然后通过PreparedStatement设置到SQL参数中。
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Tags parameter, JdbcType jdbcType) throws SQLException {
       ps.setString(i, parameter.getTagName());
    }

    /**
     * 把数据库查询结果（ResultSet）中的一列 转换成 Java 对象
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */

    @Override
    public Tags getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Tags tags = new Tags();
        tags.setTagName(rs.getString(columnName));
        return tags;
    }

    /**
     * 将数据库查询结（ResultSet）按列索引转成 Java 对象
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Tags getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Tags tags = new Tags();
        tags.setTagName(rs.getString(columnIndex));
        return tags;
    }

    @Override
    public Tags getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
