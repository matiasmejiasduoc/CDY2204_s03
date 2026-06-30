package cl.duoc.gestion_guias.service;

import cl.duoc.gestion_guias.model.Guide;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generate(Guide guide) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph title = new Paragraph("Guia de Despacho N° " + guide.getGuideNumber(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        document.add(line("Transportista: ", guide.getCarrier(), labelFont, valueFont));
        document.add(line("Fecha: ", String.valueOf(guide.getDate()), labelFont, valueFont));
        document.add(line("Destinatario: ", guide.getRecipient(), labelFont, valueFont));
        document.add(line("Direccion de destino: ", guide.getDestinationAddress(), labelFont, valueFont));
        document.add(line("Descripcion de carga: ", guide.getCargoDescription(), labelFont, valueFont));
        document.add(line("Estado: ", String.valueOf(guide.getStatus()), labelFont, valueFont));

        document.close();
        return out.toByteArray();
    }

    private Paragraph line(String label, String value, Font labelFont, Font valueFont) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new com.lowagie.text.Chunk(label, labelFont));
        paragraph.add(new com.lowagie.text.Chunk(value == null ? "" : value, valueFont));
        paragraph.setSpacingAfter(8);
        return paragraph;
    }
}
