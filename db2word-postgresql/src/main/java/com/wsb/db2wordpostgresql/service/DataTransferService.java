package com.wsb.db2wordpostgresql.service;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataTransferService {
    /**
     * 描述：更具表名获取表的详细信息
     * @param tableName
     * @return
     */
    List<Map<String, Object>> getDetail(String tableName);

    /**
     * 描述：根据数据库名称获取数据库中表的名称和注释
     * @param dbName
     * @return
     */
    List<Map<String, Object>> getAllName(String dbName);

    /**
     * 描述：数据写出到指定的word文件中
     * @param listAll
     */
    void toWord(List<Map<String, Object>> listAll) throws IOException;
}
