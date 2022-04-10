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

public class PdfPageMarker implements IEventHandler {
    private PdfFont font;

    public PdfPageMarker(PdfFont font) {
        this.font = font;
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = pdfDocumentEvent.getDocument();
        PdfPage pdfPage = pdfDocumentEvent.getPage();
        int pageNumber = pdfDocument.getPageNumber(pdfPage);
        if (!(pageNumber == 1)) {//封面不展示页脚页眉
            pageNumber = pageNumber - 1;
            Rectangle pageSize = pdfPage.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(pdfPage);
            Canvas canvas = new Canvas(pdfPage, pageSize);
            float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
            float y = pageSize.getBottom() + 15;
            Paragraph ph = new Paragraph().add("第"+pageNumber+"页").setFont(font).setFontSize(12);
            //底部中间位置
            canvas.showTextAligned(ph, x, y, TextAlignment.CENTER);
            canvas.close();

        }
    }
}
