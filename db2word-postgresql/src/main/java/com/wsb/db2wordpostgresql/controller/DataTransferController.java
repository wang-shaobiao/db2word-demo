package com.wsb.db2wordpostgresql.controller;

import com.lowagie.text.DocumentException;
import com.wsb.db2wordpostgresql.service.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/getDb/{dbName}")
    public String getDb(@PathVariable String dbName) {
        List<Map<String, Object>> list = this.dataTransferService.getAllName(dbName);
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
