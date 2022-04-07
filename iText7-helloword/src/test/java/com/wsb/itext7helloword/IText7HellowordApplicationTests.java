package com.wsb.itext7helloword;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class IText7HellowordApplicationTests {

    @Test
    void contextLoads() {

    }
    @Test
    String  hello(){
        String filePath = "e:" + File.separator + "data" + File.separator + "helloWorld.pdf";
        File file = new File(filePath);
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            Paragraph ph = new Paragraph("HelloWorld?你好");
            document.add(ph);
            document.close();
        } catch (Exception e) {
            return "file有问题！";
        }
        return "生成pdf成功"+filePath;

    }

}
