package com.wsb.itext7helloword.controller;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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
//            关于中文乱码的解决方式：两种
//                    - 1.使用iText-asian中文包
//                    - 2.系统字体下载到项目中，然后引用
//                    - 99.引出嵌入式字体的问题EmbeddingStrategy，嵌入方式
            //PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED,false);
            PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
            Paragraph ph = new Paragraph("helloworld...你好？？?");
            document.add(ph.setFont(f));
            document.close();
        } catch (Exception e) {
            return "file有问题！";
        }
        return "生成pdf成功"+filePath;
    }
    @RequestMapping("/1")
    public String path() {
        String path1 = this.getClass().getResource("").getPath();
        System.out.println("path1:"+path1);
        String path2 = this.getClass().getResource("").getPath();
        System.out.println("path2:"+path2);
        String path3 = this.getClass().getResource("/resources/application.yml").getPath();
        System.out.println("path3:"+path3);
        System.getProperty("user.dir");
        return path3;
        //return "path1:"+path1+";\r\n path2:"+path2;
    }


}
