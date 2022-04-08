package com.wsb.db2wordpostgresql.serviceimpl;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.wsb.db2wordpostgresql.dao.DataTransferMapper;
import com.wsb.db2wordpostgresql.service.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //创建PDF文档
        PdfWriter pdfWriter = new PdfWriter("E:/data/AmsDbDetail.doc");
        PdfDocument pdf = new PdfDocument(pdfWriter);
        Document document = new Document(pdf);
        PdfFont f = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        //设置文档标题
        Paragraph ph = new Paragraph("数据库设计文档");
        ph.setFont(f).setFontSize(18);
        ph.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(ph);
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
            //构建表说明
            String all = " " + (i + 1) +" 表名："+table_name+" "+table_comment + "";
            Table table = new Table(6);
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
            Cell cell = new Cell();//单元格
            cell.add;
            cell.setBackgroundColor(blue);
            //cell.setHeader(true);
//            cell.setColspan(3);//设置表格为3列
//            cell.setRowspan(3);//设置表格为3行
            table.addCell(cell);
            cell = new Cell("字段名");//单元格
            cell.setBackgroundColor(blue);
            table.addCell(cell);
            cell = new Cell("类型");//单元格
            cell.setBackgroundColor(blue);
            table.addCell(cell);
            cell = new Cell("是否为空");//单元格
            cell.setBackgroundColor(blue);
            table.addCell(cell);
            cell = new Cell("主键");//单元格
            cell.setBackgroundColor(blue);
            table.addCell(cell);
            cell = new Cell("字段说明");//单元格
            cell.setBackgroundColor(blue);
            table.addCell(cell);
            table.endHeaders();//表头结束
            /*
             * 表格主体
             */
            for (int k = 0; k < list.size(); k++) {
                String field = (String) list.get(k).get("field");
                String type = (String) list.get(k).get("type");
                String isnull = (String) list.get(k).get("null");
                String key = (String) list.get(k).get("key");
                String comment = (String)list.get(k).get("comment");
                table.addCell((k + 1) + "");
                table.addCell(field);
                table.addCell(type);
                table.addCell(isnull);
                table.addCell(key);
                table.addCell(comment);
            }
            Paragraph pheae = new Paragraph(all);
            //写入表说明
            document.add(pheae);
            //生成表格
            document.add(table);

        }
        document.close();
    }
}

