package com.wsb.itext7helloword.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class helloWorld {
    @RequestMapping("/hello")
    public String hello() {
        String filePath = "e:" + File.separator + "data" + File.separator + "helloWorld.pdf";
        File file = new File(filePath);
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            Paragraph ph = new Paragraph("HelloWorld?你好？？??");
            document.add(ph);
            document.close();
        } catch (Exception e) {
            return "file有问题！";
        }
        return "生成pdf成功"+filePath;

    }
}
