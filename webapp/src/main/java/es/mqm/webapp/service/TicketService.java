package es.mqm.webapp.service;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import es.mqm.webapp.model.Order;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    public byte[] generateTicket(Order order) throws RuntimeException {
         try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            ClassPathResource imgFile = new ClassPathResource("static/MQM.png");
            Image logo = new Image(
               ImageDataFactory.create(imgFile.getInputStream().readAllBytes())
            );
            logo.scaleToFit(120, 60);
            logo.setMarginBottom(10);
            document.add(logo);

            document.add(new Paragraph("MQM - Ticket de compra").setBold().setFontSize(20));
            document.add(new Paragraph("Número de pedido: " + order.getId()));
            document.add(new Paragraph("Fecha: " + order.getCreatedAt()));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Información del comprador: "));
            document.add(new Paragraph("Nombre: " + order.getBuyer().getName()));
            document.add(new Paragraph("Email: " + order.getBuyer().getEmail()));
            document.add(new Paragraph("Teléfono: " + order.getPhone()));
            String direccion = order.getAddress() + ", " +
                   order.getCity() + ", " +
                   order.getProvince() + ", " +
                   order.getZipcode() + ".";
            document.add(new Paragraph("Dirección: " + direccion));
            document.add(new Paragraph("Tarjeta de crédito: **** **** **** " + (order.getCreditCardNumber())));
            document.add(new Paragraph(" "));

            Table table = new Table(2);
            table.addHeaderCell("Producto");
            table.addHeaderCell("Precio");

            table.addCell(order.getProduct().getName());
            table.addCell(String.format("%.2f €", order.getProduct().getPrice()));

            document.add(table);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Gastos de envio: 3,50 €"));
            document.add(new Paragraph("Total: " + String.format("%.2f €", order.getTotalPrice())));

            document.close();
            return baos.toByteArray();
         }catch(Exception e){
            throw new RuntimeException("Error generando PDF", e);
         }
    }
}
