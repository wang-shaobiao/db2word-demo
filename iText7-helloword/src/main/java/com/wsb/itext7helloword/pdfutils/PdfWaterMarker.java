package com.wsb.itext7helloword.pdfutils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfWaterMarker implements IEventHandler {
    private PdfFont pdfFont;
    private String waterContent;
    private String waterImgPath;

    public PdfWaterMarker(PdfFont pdfFont, String waterContent, String waterImgPath) {
        this.pdfFont = pdfFont;
        this.waterContent = waterContent;
        this.waterImgPath = waterImgPath;
    }

    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        //PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);
        Paragraph waterMarker = new Paragraph(waterContent)
                .setFont(pdfFont)
                .setOpacity(.5f)
                .setFontSize(13);
        // 右下角位置
        //canvas.showTextAligned(waterMarker, pageSize.getRight() - 250, pageSize.getBottom() + 250, pdf.getNumberOfPages(), TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        float r = pageSize.getRight();
        float t = pageSize.getTop();
        float l = pageSize.getLeft();
        float b = pageSize.getBottom();
        waterMarker.add("r"+r).add("t"+t).add("l"+l).add("b"+b);
        canvas.showTextAligned(waterMarker,pageSize.getLeft()+400,pageSize.getTop()-300,TextAlignment.CENTER);
//        canvas.showTextAligned(waterMarker.add("111"),pageSize.getLeft()+400,pageSize.getTop()-300,TextAlignment.CENTER);
//        canvas.showTextAligned(waterMarker.add("222"),pageSize.getLeft()+400,pageSize.getTop()-500,TextAlignment.RIGHT);
//        canvas.showTextAligned(waterMarker.add("333"),pageSize.getLeft()+400,pageSize.getTop()-700,TextAlignment.LEFT);


        // 水印图片
        Image waterImg = null;
        if ((waterImgPath != null)&&(waterImgPath != "")) {
            try {
                InputStream inputStream = returnInputStream(waterImgPath);
                ImageData waterImgData = ImageDataFactory.create(toByteArray(inputStream));
                waterImg = new Image(waterImgData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 图片水印
        if (waterImg != null) {
            int length = waterContent.length();
            // 设置坐标 X Y
            waterImg.setFixedPosition(pdf.getNumberOfPages(), pageSize.getRight() - (168 + length), pageSize.getBottom() + 12);
            // 设置等比缩放
            waterImg.scaleAbsolute(20, 20);// 自定义大小
            // 写入图片水印
            canvas.add(waterImg);
        }

        canvas.close();
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static InputStream returnInputStream(String filePath) throws IOException {
        return new ClassPathResource(filePath).getInputStream();
    }
}
