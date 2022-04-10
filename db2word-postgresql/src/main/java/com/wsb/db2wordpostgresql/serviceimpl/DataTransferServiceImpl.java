package com.wsb.db2wordpostgresql.serviceimpl;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;

import com.wsb.db2wordpostgresql.dao.DataTransferMapper;
import com.wsb.db2wordpostgresql.pdfutils.PdfHeaderMarker;
import com.wsb.db2wordpostgresql.pdfutils.PdfPageMarker;
import com.wsb.db2wordpostgresql.pdfutils.PdfWaterMarker;
import com.wsb.db2wordpostgresql.service.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.system.SystemProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DataTransferServiceImpl implements DataTransferService {

    private String dbName;
    @Autowired
    private DataTransferMapper dataTransferMapper;
    @Override
    public List<Map<String, Object>> getDetail(String tableName) {
        return dataTransferMapper.getTableColumnDetail(tableName,this.dbName);
    }

    @Override
    public List<Map<String, Object>> getAllName(String dbName) {
        this.dbName = dbName;
        return dataTransferMapper.getAllTableNames(dbName);
    }

    @Override
    public void toWord(List<Map<String, Object>> listAll) throws IOException {
        //设置下载路径
        //获取jar包所在目录
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        //在jar包所在目录下生成一个文件夹用来存储上传的图片
        String dirPath = jarF.getParentFile().toString()+File.separator+"data"+ File.separator;

        //String dirPath = System.getProperty("user.dir")+File.separator+"data"+ File.separator;
        System.out.println(dirPath);
        File filePath=new File(dirPath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        //创建PDF文档
        PdfWriter pdfWriter = new PdfWriter(new File(dirPath+"DataDesign.pdf"));
        PdfDocument pdf = new PdfDocument(pdfWriter);
        Document document = new Document(pdf);
        //宋体
        //PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        //使用项目内上传的楷体
        //PdfFont f = PdfFontFactory.createFont("src/main/resources/simkai.ttf", PdfEncodings.IDENTITY_H);
        PdfFont f = PdfFontFactory.createFont(new ClassPathResource("simkai.ttf").getPath(), PdfEncodings.IDENTITY_H);
        //设置文档标题
        Paragraph ph = new Paragraph("AMS数据库设计文档");
        ph.setFont(f).setFontSize(25);
        ph.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(ph);
        //简单添加图片，稍微再细一点的可以见水印图片的处理方式,路径或者字节流均可
        //Image im = new Image(ImageDataFactory.create("src/main/resources/test.jpg"),36,100);
        Image im = new Image(ImageDataFactory.create(new ClassPathResource("test.jpg").getURL()),36,100);
        im.scaleAbsolute(480, 500);
        document.add(im);
        //分页
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        //页眉
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE,new PdfHeaderMarker(f, "AMS项目内部使用"));
        //页码
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE,new PdfPageMarker(f));
        //简单水印（水印图片/水印字）
        pdf.addEventHandler(PdfDocumentEvent.INSERT_PAGE,new PdfWaterMarker(f,"AMS项目内部使用",""));
        /**
         * 创建表格，通过查询出来的表遍历
         */
        for (int i = 0; i < listAll.size(); i++) {
            //表名
            String table_name = (String)listAll.get(i).get("table_name");
            //表说明
            String table_comment = (String) listAll.get(i).get("table_comment");
            if (table_comment == null) {
                table_comment = "";
            }
            //获取某张表的所有字段说明
            List<Map<String, Object>> list = this.getDetail(table_name);
            //构建表说明--后面再处理，
            //String all = " " + (i + 1) +". 表名："+table_name+" "+table_comment + "";
            Table table = new Table(6).setAutoLayout().useAllAvailableWidth();
            document.add(new Paragraph(""));
            //table.setBorderColor(Color.BLACK);
            //在PDF官方文档(ISO-32000) 中，定义了很多的颜色空间，不同的颜色空间在iText中对应着不同的class,
            // 最常用的颜色空间是DeviceGray（灰度空间，只需一个亮度参数），DeviceRgb（RGB空间，有红色、绿色和蓝色决定）
            // 和DeviceCmyk（印刷四色空间，由青色、品红、黄色和黑色），在这个例子中，我们使用的是DeviceCmyk空间。
            //注意，我们使用的不是来自java.awt.Color定义的颜色，而是来自iText的Color的类，可以在com.itextpdf.kernel.color包中找到。
            Color blue = new DeviceCmyk(1.f, 0.156f, 0.f, 0.118f);
            Border bo = new DashedBorder(blue, 2);
            table.setBorder(bo);
            table.setPadding(0);
            table.setSpacingRatio(0);
            // Creating a table

            /*
             * 添加表头的元素，并设置表头的背景颜色
             */
            //Color chade = new Color(176, 222, 222);
            String[] header ={"序号","字段名","类型","是否为空","主键","字段说明"};
            Cell cell;
            for (int k = 0; k < 6; k++) {
                cell = new Cell().add(new Paragraph(header[k]).setFont(f)).setBackgroundColor(blue);
                table.addHeaderCell(cell);
            }
            /*
             * 表格主体
             */
            for (int k = 0; k < list.size(); k++) {
                table.addCell(new Cell().add(new Paragraph((k + 1) + "")));
                String field = (String) list.get(k).get("field");
                table.addCell(new Cell().add(new Paragraph(field + "")));
                //table.addCell(field);
                String type = (String) list.get(k).get("type");
                table.addCell(new Cell().add(new Paragraph(type + "")));
                //table.addCell(type);
                String isnull = (String) list.get(k).get("null");
                table.addCell(new Cell().add(new Paragraph(isnull + "")));
                //table.addCell(isnull);
                String key = (String) list.get(k).get("key");
                table.addCell(new Cell().add(new Paragraph(key + "")));
                //table.addCell(key);
                String comment = (String)list.get(k).get("comment");
                table.addCell(new Cell().add(new Paragraph(comment + "").setFont(f))).setAutoLayout();
                //table.addCell(comment);





            }
            Paragraph pheae = new Paragraph();
            Text tx1 = new Text(" " + (i + 1) +". 表名：").setFont(f).setFontSize(18);
            Text tx2 = new Text(table_name+" "+table_comment + "").setFont(f).setFontSize(16).setBold();
            pheae.add(tx1).add(tx2);
            //写入表说明,设置粗体
            document.add(pheae);
            //生成表格
            document.add(table);

        }
        document.close();
    }
}

