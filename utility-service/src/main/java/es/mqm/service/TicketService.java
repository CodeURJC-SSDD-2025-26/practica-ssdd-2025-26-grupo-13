package es.mqm.service;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import es.mqm.dto.MessageInfoDTO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    public byte[] generateTicket(MessageInfoDTO info) throws RuntimeException {
         try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            ClassPathResource imgFile = new ClassPathResource("/static/images/MQM.png");
            Image logo = new Image(
               ImageDataFactory.create(imgFile.getInputStream().readAllBytes())
            );
            logo.scaleToFit(120, 60);
            logo.setMarginBottom(10);
            document.add(logo);

            document.add(new Paragraph("MQM - Ticket de compra").setBold().setFontSize(20));
            document.add(new Paragraph("Número de pedido: " + info.id()));
            document.add(new Paragraph("Fecha: " + info.createdAt()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Información del comprador: "));
            document.add(new Paragraph("Nombre: " + info.buyerName()));
            document.add(new Paragraph("Email: " + info.buyerEmail()));
            document.add(new Paragraph("Teléfono: " + info.phone()));
            String address = info.address() + ", " +
                   info.city() + ", " +
                   info.province() + ", " +
                   info.zipcode() + ".";
            document.add(new Paragraph("Dirección: " + address));
            document.add(new Paragraph("Tarjeta de crédito: **** **** **** " + (info.creditCardNumberLast4Digits())));
            document.add(new Paragraph(" "));

            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Producto");
            table.addHeaderCell("Precio");

            table.addCell(info.productName());
            table.addCell(String.format("%.2f €", info.productPrice()));

            document.add(table);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Gastos de envio: " + (info.totalPrice() - info.productPrice()) + " €"));
            document.add(new Paragraph("Total: " + String.format("%.2f €", info.totalPrice())));

            document.close();
            return baos.toByteArray();
         }catch(Exception e){
            throw new RuntimeException("Error generando PDF", e);
         }
    }
}
