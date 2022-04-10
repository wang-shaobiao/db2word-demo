package com.wsb.itext7helloword;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.wsb.itext7helloword.pdfutils.PdfHeaderMarker;
import com.wsb.itext7helloword.pdfutils.PdfPageMarker;
import com.wsb.itext7helloword.pdfutils.PdfWaterMarker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest
class IText7HellowordApplicationTests {

    @Test
    void contextLoads() {

    }
    @Test
    public void  hello(){
        //设置下载路径
        //获取jar包所在目录
//        ApplicationHome h = new ApplicationHome(getClass());
//        File jarF = h.getSource();
//        //在jar包所在目录下生成一个upload文件夹用来存储上传的图片
//        String dirPath = jarF.getParentFile().toString()+File.separator+"data"+ File.separator;
        String dirPath = System.getProperty("user.dir")+File.separator+"data"+ File.separator;
        System.out.println(dirPath);
        File filePath=new File(dirPath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }

        //String filePath = "e:" + File.separator + "data" + File.separator + "helloWorld.pdf";
        File file = new File(dirPath+"hello.pdf");
        try {

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);//看原码可知，默认是A4
//            关于中文乱码的解决方式：两种
//                    - 1.使用iText-asian中文包
//                    - 2.系统字体下载到项目中，然后引用
//                    - 99.引出嵌入式字体的问题EmbeddingStrategy，嵌入方式
            //PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED,false);
            PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
            //使用项目内上传的楷体
            PdfFont f2 = PdfFontFactory.createFont(new ClassPathResource("simkai.ttf").getPath(), PdfEncodings.IDENTITY_H);
            Paragraph ph = new Paragraph("helloworld...你好？？?");
            Text tx1 = new Text("abcdefg,1.表名：").setFont(f).setFontSize(20);
            Text tx2 = new Text("txtdm ,测试表名").setFont(f2).setFontSize(18).setBold();
            Paragraph ph2 = new Paragraph().add(tx1).add(tx2);
            document.add(ph.setFont(f2));
            document.add(ph2);
            //简单添加图片，稍微再细一点的可以见水印图片的处理方式,路径或者字节流均可
            Image im = new Image(ImageDataFactory.create(new ClassPathResource("test.jpg").getURL()),36,100);
            im.scaleAbsolute(480, 550);
            document.add(im);
            //分页
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            //页眉
            pdf.addEventHandler(PdfDocumentEvent.START_PAGE,new PdfHeaderMarker(f2, "页眉"));

            //页码
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE,new PdfPageMarker(f2));
            //水印
            pdf.addEventHandler(PdfDocumentEvent.INSERT_PAGE,new PdfWaterMarker(f2,"tesxt","1.jpg"));
            // 表格
            Paragraph p = new Paragraph();
            Table table = new Table(3).useAllAvailableWidth().setAutoLayout();
            for (int i = 0; i < 3; i++) {
                table.addHeaderCell(new Cell().add(new Paragraph(""+(i + 1) )));

            }
            for (int k = 0; k < 100; k++) {
                Cell cell;
                //第一个单元格
                cell=new Cell().add(new Paragraph().add("序号1111111111111111111111111"+k).setFont(f)).setKeepTogether(true);
                table.addCell(cell);
                //第二个
                cell=new Cell().add(new Paragraph().add("序号2"+k).setFont(f)).setKeepTogether(true);
                table.addCell(cell);
                //第三个
                cell= new Cell().add(new Paragraph().add("序号3"+k).setFont(f)).setKeepTogether(true);
                table.addCell(cell);


            }



            document.add(table);
            document.close();

        } catch (Exception e) {
            System.out.println("file有问题！");
        }
        System.out.println("生成pdf成功"+filePath);

    }
    @Test
    void hello2() {
        String filePath = "e:" + File.separator + "data" + File.separator + "helloWorld2.pdf";
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
            // 表格
            Paragraph p = new Paragraph();
//            Table table = new Table(new float[] { 55, 13, 14, 13 });
            Table table = new Table(4).setAutoLayout();
            //表头
            for(int i=0;i<4;i++){
                table.addCell(new Cell().add(new Paragraph(""+(i+1))));
            }
            //表格行合并"2"代表合并2行单元格
            Cell cell=new Cell(2,1).add(new Paragraph("one"));
            table.addCell(cell);
            //表格列合并"3"代表合并3列
            cell=new Cell(1,3).add(new Paragraph("two"));
            table.addCell(cell);
            //将剩余格补齐
            cell=new Cell().add(new Paragraph("three"));
            table.addCell(cell);
            cell=new Cell().add(new Paragraph("three"));
            table.addCell(cell);
            cell=new Cell().add(new Paragraph("three"));
            table.addCell(cell);
            document.add(table);
            document.close();


        } catch (Exception e) {
            System.out.println("file有问题！");
        }
        System.out.println("生成pdf成功"+filePath);

    }
    @Test
    void hello3() throws FileNotFoundException {
        String path1 = this.getClass().getResource("").getPath();
        System.out.println("path1:"+path1);
        String path2 = this.getClass().getResource("").getPath();
        System.out.println("path2:"+path2);
//        String path3 = this.getClass().getResource("helloWorld.class").getPath();
//        System.out.println("path3:"+path3);
        System.out.println(ResourceUtils.getFile("classpath:"+"application.yml").getPath());
        //System.out.println(System.getProperty("user.dir"));
        ClassPathResource cpr = new ClassPathResource("application.yml");
        System.out.println(cpr.getPath());

    }


}
