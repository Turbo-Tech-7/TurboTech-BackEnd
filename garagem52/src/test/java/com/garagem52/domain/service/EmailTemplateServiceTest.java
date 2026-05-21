package com.garagem52.domain.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTemplateServiceTest {

    @Test
    void deveGerarTemplatePasswordReset() {
        String html = EmailTemplateService.passwordReset(
                "João",
                "123456"
        );

        assertNotNull(html);
        assertTrue(html.contains("João"));
        assertTrue(html.contains("123 456"));
        assertTrue(html.contains("Redefinição de Senha"));
    }

    @Test
    void deveGerarTemplateLoginCode() {
        String html = EmailTemplateService.loginCode(
                "Maria",
                "654321"
        );

        assertNotNull(html);
        assertTrue(html.contains("Maria"));
        assertTrue(html.contains("654 321"));
        assertTrue(html.contains("Código de Acesso"));
    }

    @Test
    void deveGerarTemplateOrcamentoPdf() {
        String html = EmailTemplateService.orcamentoPdf(
                "Carlos",
                "123",
                "Troca de óleo",
                "ABC1234",
                "Gol",
                "R$ 150,00",
                "01/01/2025"
        );

        assertNotNull(html);
        assertTrue(html.contains("Carlos"));
        assertTrue(html.contains("Troca de óleo"));
        assertTrue(html.contains("ABC1234"));
        assertTrue(html.contains("R$ 150,00"));
    }
}
