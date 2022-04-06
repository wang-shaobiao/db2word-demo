package com.wsb.db2wordmysql.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataTransferMapper {
    /**
     * 描述：根据表明获取表的详细信息
     *
     * @param tableName
     * @return
     */
    //这里用到${} 一般表名
    @Select("SHOW FULL FIELDS FROM ${tableName}")
    List<Map<String, Object>> getDataDetail(@Param("tableName") String tableName);

    /**
     * 描述：根据数据库名称获取数据库中表的名称和注释
     * @param dbName
     * @return
     */
    @Select("select table_name ,table_comment from information_schema.tables where table_schema = #{dbName}")
    List<Map<String, Object>> getAllDataSourceName(@Param("dbName")String dbName);
}
