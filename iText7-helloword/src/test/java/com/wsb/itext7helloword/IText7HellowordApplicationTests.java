package com.wsb.itext7helloword;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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
    public void  hello(){
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
            System.out.println("file有问题！");
        }
        System.out.println("生成pdf成功"+filePath);

    }

}
