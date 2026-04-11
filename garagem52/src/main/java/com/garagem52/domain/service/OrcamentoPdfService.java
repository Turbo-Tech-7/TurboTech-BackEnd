package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.ItemOrcadoResponseDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class OrcamentoPdfService {

    private static final DeviceRgb DARK     = new DeviceRgb(30, 30, 30);
    private static final DeviceRgb ACCENT   = new DeviceRgb(30, 80, 160);
    private static final DeviceRgb LIGHT_BG = new DeviceRgb(245, 247, 252);
    private static final DeviceRgb BORDER   = new DeviceRgb(200, 210, 230);
    private static final DeviceRgb TOTAL_BG = new DeviceRgb(30, 80, 160);
    private static final DeviceRgb WHITE    = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb MUTED    = new DeviceRgb(110, 120, 140);

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

    public byte[] gerar(OrcamentoResponseDTO orcamento) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf  = new PdfDocument(writer);

            // try-with-resources garante doc.close() e flush completo antes de toByteArray()
            try (Document doc = new Document(pdf, PageSize.A4)) {
                doc.setMargins(40, 50, 40, 50);

                PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

                addHeader(doc, orcamento, bold, regular);
                addSpacer(doc, 14);
                addClienteSection(doc, orcamento, bold, regular);
                addSpacer(doc, 10);
                addServicoSection(doc, orcamento, bold, regular);
                addSpacer(doc, 10);
                addItensSection(doc, orcamento, bold, regular);
                addSpacer(doc, 10);
                addTotalSection(doc, orcamento, bold, regular);
                addSpacer(doc, 20);
                addFooter(doc, regular);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do orçamento #" + orcamento.getId() + ": " + e.getMessage(), e);
        }
        return baos.toByteArray();
    }

    private void addHeader(Document doc, OrcamentoResponseDTO o,
                           PdfFont bold, PdfFont regular) {
        Table header = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);

        Cell left = new Cell().setBorder(Border.NO_BORDER).setPadding(0);
        left.add(new Paragraph("Orçamento #" + String.format("%04d", o.getId()))
                .setFont(bold).setFontSize(22).setFontColor(DARK));
        String data = o.getDataOrcamento() != null
                ? o.getDataOrcamento().format(DATE_FMT) : "—";
        left.add(new Paragraph(data)
                .setFont(regular).setFontSize(10).setFontColor(MUTED).setMarginTop(2));
        left.add(new Paragraph("Status: " + statusLabel(o.getStatus()))
                .setFont(bold).setFontSize(10).setFontColor(ACCENT).setMarginTop(4));
        header.addCell(left);

        Cell right = new Cell().setBorder(Border.NO_BORDER).setPadding(0)
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
        right.add(new Paragraph("GARAGEM 52")
                .setFont(bold).setFontSize(16).setFontColor(ACCENT)
                .setTextAlignment(TextAlignment.RIGHT));
        right.add(new Paragraph("Serviços Automotivos")
                .setFont(regular).setFontSize(9).setFontColor(MUTED)
                .setTextAlignment(TextAlignment.RIGHT));
        header.addCell(right);

        doc.add(header);
        doc.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine())
                .setMarginTop(8).setMarginBottom(4)
                .setStrokeColor(BORDER));
    }

    private void addClienteSection(Document doc, OrcamentoResponseDTO o,
                                   PdfFont bold, PdfFont regular) {
        doc.add(sectionTitle("Informações do Cliente", bold));
        Table t = cardTable();
        VeiculoResponseDTO v = o.getVeiculo();
        String placa  = v != null ? v.getPlaca() : "—";
        String modelo = v != null
                ? (v.getMarca() + " " + v.getModelo() + " " + (v.getAno() != null ? v.getAno() : "")).trim()
                : "—";
        addRow(t, "Placa do Veículo",  placa,                      bold, regular);
        addRow(t, "Nome do Cliente",   nvl(o.getNomeCliente()),     bold, regular);
        addRow(t, "Telefone",          nvl(o.getTelefoneCliente()), bold, regular);
        addRow(t, "Modelo do Veículo", modelo,                      bold, regular);
        doc.add(t);
    }

    private void addServicoSection(Document doc, OrcamentoResponseDTO o,
                                   PdfFont bold, PdfFont regular) {
        doc.add(sectionTitle("Detalhes do Serviço", bold));
        Table t = cardTable();
        addRow(t, "Descrição do Serviço", nvl(o.getDescricaoServico()), bold, regular);
        if (OrcamentoStatus.CANCELADO.equals(o.getStatus()) && o.getMotivoCancelamento() != null) {
            addRow(t, "Motivo do Cancelamento", o.getMotivoCancelamento().getDescricao(), bold, regular);
        }
        doc.add(t);
    }

    private void addItensSection(Document doc, OrcamentoResponseDTO o,
                                 PdfFont bold, PdfFont regular) {
        List<ItemOrcadoResponseDTO> itens = o.getItens();
        if (itens == null || itens.isEmpty()) return;

        doc.add(sectionTitle("Peças / Materiais", bold));

        Table t = new Table(UnitValue.createPercentArray(new float[]{35, 25, 15, 12, 13}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBackgroundColor(LIGHT_BG)
                .setBorder(new SolidBorder(BORDER, 0.5f))
                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6));

        String[] headers = {"Peça / Material", "Fornecedor", "Qtd", "Vlr Unit.", "Subtotal"};
        for (String h : headers) {
            t.addHeaderCell(new Cell()
                    .setBackgroundColor(ACCENT)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(7)
                    .add(new Paragraph(h).setFont(bold).setFontSize(9).setFontColor(WHITE)));
        }

        boolean alt = false;
        for (ItemOrcadoResponseDTO item : itens) {
            DeviceRgb rowBg = alt ? LIGHT_BG : WHITE;
            double subtotal = (item.getValor() != null ? item.getValor() : 0)
                    * (item.getQuantidade() != null ? item.getQuantidade() : 1);
            addItemRow(t, rowBg, bold, regular,
                    nvl(item.getNomePeca()),
                    nvl(item.getFornecedor()),
                    String.valueOf(item.getQuantidade() != null ? item.getQuantidade() : 1),
                    brl(item.getValor()),
                    brl(subtotal));
            alt = !alt;
        }
        doc.add(t);
    }

    private void addTotalSection(Document doc, OrcamentoResponseDTO o,
                                 PdfFont bold, PdfFont regular) {
        double pecas = 0;
        if (o.getItens() != null) {
            pecas = o.getItens().stream()
                    .mapToDouble(i -> (i.getValor() != null ? i.getValor() : 0)
                            * (i.getQuantidade() != null ? i.getQuantidade() : 1))
                    .sum();
        }
        double mdo = o.getValorMaoDeObra() != null ? o.getValorMaoDeObra() : 0;
        double total = o.getValorTotal() != null ? o.getValorTotal() : pecas + mdo;

        Table t = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(new SolidBorder(BORDER, 0.5f))
                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6));

        addTotalRow(t, "Peça / Material", brl(pecas), bold, regular, false);
        addTotalRow(t, "Mão de Obra",     brl(mdo),   bold, regular, false);

        t.addCell(new Cell().setBackgroundColor(TOTAL_BG).setBorder(Border.NO_BORDER)
                .setPadding(10)
                .add(new Paragraph("Total").setFont(bold).setFontSize(12).setFontColor(WHITE)));
        t.addCell(new Cell().setBackgroundColor(TOTAL_BG).setBorder(Border.NO_BORDER)
                .setPadding(10).setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph(brl(total)).setFont(bold).setFontSize(12).setFontColor(WHITE)));

        doc.add(t);
    }

    private void addFooter(Document doc, PdfFont regular) {
        doc.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine())
                .setStrokeColor(BORDER).setMarginBottom(6));
        doc.add(new Paragraph("Documento gerado automaticamente pelo sistema Garagem52.")
                .setFont(regular).setFontSize(8).setFontColor(MUTED)
                .setTextAlignment(TextAlignment.CENTER));
    }

    private Paragraph sectionTitle(String text, PdfFont bold) {
        return new Paragraph(text)
                .setFont(bold).setFontSize(11).setFontColor(ACCENT)
                .setMarginBottom(5);
    }

    private Table cardTable() {
        return new Table(UnitValue.createPercentArray(new float[]{45, 55}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(new SolidBorder(BORDER, 0.5f))
                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6))
                .setBackgroundColor(LIGHT_BG);
    }

    private void addRow(Table t, String label, String value, PdfFont bold, PdfFont regular) {
        t.addCell(new Cell().setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(BORDER, 0.4f))
                .setPadding(8).setPaddingLeft(12)
                .add(new Paragraph(label).setFont(regular).setFontSize(9).setFontColor(MUTED)));
        t.addCell(new Cell().setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(BORDER, 0.4f))
                .setPadding(8).setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph(value).setFont(bold).setFontSize(9).setFontColor(DARK)));
    }

    private void addItemRow(Table t, DeviceRgb bg, PdfFont bold, PdfFont regular,
                            String... values) {
        boolean[] rightAlign = {false, false, true, true, true};
        for (int i = 0; i < values.length; i++) {
            TextAlignment align = rightAlign[i] ? TextAlignment.RIGHT : TextAlignment.LEFT;
            t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER)
                    .setBorderBottom(new SolidBorder(BORDER, 0.3f))
                    .setPadding(7).setTextAlignment(align)
                    .add(new Paragraph(values[i])
                            .setFont(i == 0 ? bold : regular).setFontSize(9).setFontColor(DARK)));
        }
    }

    private void addTotalRow(Table t, String label, String value,
                             PdfFont bold, PdfFont regular, boolean highlight) {
        DeviceRgb bg = highlight ? TOTAL_BG : WHITE;
        DeviceRgb fg = highlight ? WHITE : DARK;
        t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(BORDER, 0.4f))
                .setPadding(8).setPaddingLeft(12)
                .add(new Paragraph(label).setFont(regular).setFontSize(10).setFontColor(fg)));
        t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(BORDER, 0.4f))
                .setPadding(8).setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph(value).setFont(bold).setFontSize(10).setFontColor(fg)));
    }

    private void addSpacer(Document doc, float height) {
        doc.add(new Paragraph(" ").setMargin(0).setHeight(height));
    }

    private String brl(Double value) {
        if (value == null) return "R$ 0,00";
        return String.format(new Locale("pt", "BR"), "R$ %,.2f", value);
    }

    private String nvl(String s) {
        return s != null && !s.isBlank() ? s : "—";
    }

    private String statusLabel(OrcamentoStatus status) {
        return status != null ? status.getDescricao() : "Aberto";
    }
}