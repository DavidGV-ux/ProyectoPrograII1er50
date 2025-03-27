package co.edu.uptc.servicio;

import co.edu.uptc.modelo.Residuo;
import co.edu.uptc.modelo.Usuario;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class ReporteServicio {
    private ReciclajeServicio servicio;

    public ReporteServicio(ReciclajeServicio servicio) {
        this.servicio = servicio;
    }

    public void generarReporte(String nombreArchivo) {
        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Título
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Reporte de Reciclaje", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(new Paragraph("\n"));

            // Agregar el total reciclado por usuario
            agregarTablaTotalReciclado(documento);

            // Agregar gráfico del material más reciclado
            agregarGraficoMaterialMasReciclado(documento);

            // Agregar ranking de usuarios con más reciclaje
            agregarRankingUsuarios(documento);

            documento.close();
            System.out.println("Reporte PDF generado con éxito!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarTablaTotalReciclado(Document documento) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);
        tabla.addCell(new PdfPCell(new Phrase("Usuario", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        tabla.addCell(new PdfPCell(new Phrase("Total Reciclado (Kg)", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

        for (Usuario usuario : servicio.obtenerTodosLosUsuarios()) {
            double totalReciclado = usuario.getResiduos().stream().mapToDouble(Residuo::getPeso).sum();
            tabla.addCell(usuario.getNombre());
            tabla.addCell(String.valueOf(totalReciclado));
        }

        documento.add(new Paragraph("Total Reciclado por Usuario", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        documento.add(tabla);
        documento.add(new Paragraph("\n"));
    }

    private void agregarGraficoMaterialMasReciclado(Document documento) throws Exception {
        Map<String, Double> materialReciclado = new HashMap<>();
        for (Usuario usuario : servicio.obtenerTodosLosUsuarios()) {
            for (Residuo residuo : usuario.getResiduos()) {
                materialReciclado.put(residuo.getTipoMaterial(),
                        materialReciclado.getOrDefault(residuo.getTipoMaterial(), 0.0) + residuo.getPeso());
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : materialReciclado.entrySet()) {
            dataset.addValue(entry.getValue(), "Kg", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Material Más Reciclado",
                "Material",
                "Kg",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        Image imagen = Image.getInstance(baos.toByteArray());

        documento.add(new Paragraph("Material Más Reciclado", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        documento.add(imagen);
        documento.add(new Paragraph("\n"));
    }

    private void agregarRankingUsuarios(Document documento) throws DocumentException {
        List<Usuario> usuariosOrdenados = new ArrayList<>(servicio.obtenerTodosLosUsuarios());
        usuariosOrdenados.sort((u1, u2) -> Double.compare(
                u2.getResiduos().stream().mapToDouble(Residuo::getPeso).sum(),
                u1.getResiduos().stream().mapToDouble(Residuo::getPeso).sum()));

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);
        tabla.addCell(new PdfPCell(new Phrase("Usuario", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        tabla.addCell(new PdfPCell(new Phrase("Total Reciclado (Kg)", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

        for (Usuario usuario : usuariosOrdenados) {
            double totalReciclado = usuario.getResiduos().stream().mapToDouble(Residuo::getPeso).sum();
            tabla.addCell(usuario.getNombre());
            tabla.addCell(String.valueOf(totalReciclado));
        }

        documento.add(new Paragraph("Ranking de Usuarios con Más Reciclaje", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        documento.add(tabla);
    }
}
