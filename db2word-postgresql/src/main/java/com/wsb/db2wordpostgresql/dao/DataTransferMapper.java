package com.wsb.db2wordpostgresql.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataTransferMapper {
    /**
     * 根据数据库名称获取数据库中表的名称和注释
     *
     * @param tableName
     * @return
     */
    //这里用到${} 一般表名
    @Select("select relname as table_name," +
            "(select description from pg_description where objoid=oid and objsubid=0) as table_comment " +
            "from pg_class " +
            "where relkind ='r' and relname NOT LIKE 'pg%' AND relname NOT LIKE 'sql_%' " +
            "and relnamespace = (select oid from pg_namespace where nspname=#{dbName} ) " +
            "order by table_name;")
    List<Map<String, Object>> getAllTableNames(@Param("dbName") String dbName);

    /**
     * 描述：根据数据库名称获取数据库中表的名称和注释
     * @param dbName
     * @return
     */
    @Select("select " +
            "a.attname as Field," +
            "format_type(a.atttypid,a.atttypmod) as Type," +
            "(case " +
            "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='p')>0 then 'PRI' " +
            "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='u')>0 then 'UNI'" +
            "when (select count(*) from pg_constraint where conrelid = a.attrelid and conkey[1]=attnum and contype='f')>0 then 'FRI'" +
            "else '' end) as key," +
            "(case when a.attnotnull=true then 'NO' else 'YES' end) as Null," +
            "col_description(a.attrelid,a.attnum) as Comment " +
            "from pg_attribute a " +
            "where attstattarget=-1 and attrelid = (select oid from pg_class where relname = #{tableName} " +
            "and relnamespace=(select oid from pg_namespace where nspname=#{dbName} ));")
    List<Map<String, Object>> getTableColumnDetail(@Param("tableName")String tableName,@Param("dbName") String dbName);
}
