package com.wsb.itext7helloword.pdfutils;


import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class PdfHeaderMarker implements IEventHandler {
    private PdfFont font;
    private String title;

    public PdfHeaderMarker(PdfFont font, String title) {
        this.font = font;
        this.title = title;
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfPage pdfPage = pdfDocumentEvent.getPage();
        PdfDocument pdfDocument = pdfDocumentEvent.getDocument();
        int pageNumber = pdfDocument.getPageNumber(pdfPage);
        if (pageNumber != 1) {
            //也就是获得这个页面的矩形大小，原点是左下角，rectangle定义就是两个点连成对角线的矩形
            Rectangle pageSize = pdfPage.getPageSize();
            //PdfCanvas pdfCanvas = new PdfCanvas(pdfPage.getLastContentStream(), pdfPage.getResources(), pdfDocument);
            PdfCanvas pdfCanvas = new PdfCanvas(pdfPage);
            Canvas canvas = new Canvas(pdfPage,pageSize);
            float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            float y = pageSize.getTop() - 25;
            Paragraph ph = new Paragraph().add(title).setFont(font).setFontSize(12);
            canvas.showTextAligned(ph, x, y, TextAlignment.CENTER);
            canvas.close();
        }
    }
}
