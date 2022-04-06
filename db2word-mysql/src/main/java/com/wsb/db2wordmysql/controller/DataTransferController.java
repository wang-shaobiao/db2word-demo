package com.wsb.db2wordmysql.controller;

import com.lowagie.text.DocumentException;
import com.wsb.db2wordmysql.service.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ams")
public class DataTransferController {

    @Autowired
    private DataTransferService dataTransferService;

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/getDb")
    public String getDb(String dbName) {
        List<Map<String, Object>> list = this.dataTransferService.getAllDataName(dbName);
        try {
            this.dataTransferService.toWord(list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "error ,File not found";
        } catch (DocumentException e) {
            e.printStackTrace();
            return "error ,Document not found";
        }
        return "设计文档生成成功";
    }
}
