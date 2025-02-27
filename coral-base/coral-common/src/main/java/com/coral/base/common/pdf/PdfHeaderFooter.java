package com.coral.base.common.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author huss
 * @version 1.0
 * @className PdfHeaderFooter
 * @description pdf页脚
 * @date 2021/9/23 16:34
 */
@Slf4j
public class PdfHeaderFooter extends PdfPageEventHelper {
    /**
     * 总页数
     */
    private PdfTemplate totalPage;
    /**
     * 字体
     */
    private Font hfFont;

    {
        try {
            hfFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 8, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            log.error("Error:", e);
        }
    }

    /**
     * 打开文档时，创建一个总页数的模版
     *
     * @param writer
     * @param document
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        totalPage = cb.createTemplate(30, 16);
    }

    /**
     * 一页加载完成触发，写入页眉和页脚
     *
     * @param writer
     * @param document
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(3);
        try {
            table.setTotalWidth(PageSize.A4.getWidth() - 100);
            table.setWidths(new int[]{24, 24, 3});
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(-10);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);

            // 可以直接使用addCell(str)，不过不能指定字体，中文无法显示
            table.addCell(new Paragraph("我是页眉/页脚", hfFont));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(new Paragraph("第" + writer.getPageNumber() + "页/", hfFont));
            // 总页数
            PdfPCell cell = new PdfPCell(Image.getInstance(totalPage));
            cell.setBorder(Rectangle.BOTTOM);
            table.addCell(cell);
            // 将页眉写到document中，位置可以指定，指定到下面就是页脚
            table.writeSelectedRows(0, -1, 50, PageSize.A4.getHeight() - 20, writer.getDirectContent());
        } catch (Exception de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * 全部完成后，将总页数的pdf模版写到指定位置
     *
     * @param writer
     * @param document
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        String text = "总" + (writer.getPageNumber()) + "页";
        ColumnText.showTextAligned(totalPage, Element.ALIGN_LEFT, new Paragraph(text, hfFont), 2, 2, 0);
    }

}