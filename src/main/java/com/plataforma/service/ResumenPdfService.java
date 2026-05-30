package com.plataforma.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.plataforma.model.Resumen;
import com.plataforma.model.ResumenItem;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

/**
 * Genera el PDF del resumen de inscripcion usando OpenPDF.
 */
@Service
public class ResumenPdfService {

    private static final DateTimeFormatter FECHA_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public byte[] generar(Resumen resumen) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font label = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Resumen de Inscripcion", titulo));
        document.add(new Paragraph(" "));

        document.add(linea("Numero de resumen: ", String.valueOf(resumen.getId()), label, normal));
        document.add(linea("Estudiante: ", resumen.getEstudianteNombre(), label, normal));
        document.add(linea("Fecha: ",
                resumen.getFecha() != null ? resumen.getFecha().format(FECHA_FMT) : "", label, normal));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell(headerCell("Curso", label));
        table.addCell(headerCell("Instructor", label));
        table.addCell(headerCell("Costo", label));

        for (ResumenItem item : resumen.getItems()) {
            table.addCell(new Phrase(item.getNombre(), normal));
            table.addCell(new Phrase(item.getInstructor(), normal));
            table.addCell(new Phrase(formatCosto(item.getCosto()), normal));
        }
        document.add(table);

        document.add(new Paragraph(" "));
        Paragraph total = new Paragraph("Total a pagar: " + formatCosto(resumen.getTotalAPagar()), label);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
        return out.toByteArray();
    }

    private Paragraph linea(String label, String valor, Font labelFont, Font valorFont) {
        Paragraph p = new Paragraph();
        p.add(new Phrase(label, labelFont));
        p.add(new Phrase(valor, valorFont));
        return p;
    }

    private PdfPCell headerCell(String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private String formatCosto(Double costo) {
        if (costo == null) {
            return "$0";
        }
        return String.format("$%,.0f", costo);
    }
}
