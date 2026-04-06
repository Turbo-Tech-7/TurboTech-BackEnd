package com.garagem52.domain.service;

public class EmailTemplateService {

    private static final String BRAND   = "#1E50A0";
    private static final String DARK    = "#1A1A2E";
    private static final String MUTED   = "#6B7280";
    private static final String BG      = "#F0F4FA";
    private static final String WHITE   = "#FFFFFF";
    private static final String BORDER  = "#C8D2E6";
    private static final String CODE_BG = "#EEF3FF";

    private static String wrap(String title, String bodyHtml) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>%s</title>
              <style>
                body  { margin:0; padding:0; background:%s;
                        font-family:'Segoe UI',Arial,sans-serif; }
                .wrap { max-width:580px; margin:36px auto; background:%s;
                        border-radius:14px; border:1px solid %s;
                        overflow:hidden; box-shadow:0 4px 24px rgba(30,80,160,.08); }
                .hdr  { background:%s; padding:28px 36px 22px; text-align:center; }
                .hdr-logo { margin:0; font-size:28px; font-weight:900;
                             color:#fff; letter-spacing:3px; }
                .hdr-sub  { margin:5px 0 0; font-size:10px;
                             color:rgba(255,255,255,.70);
                             letter-spacing:4px; text-transform:uppercase; }
                .body { padding:32px 36px; }
                .ftr  { background:%s; border-top:1px solid %s;
                        padding:18px 32px; text-align:center; }
                .ftr p { margin:0; font-size:11px; color:%s; line-height:1.6; }
                h2 { margin:0 0 10px; color:%s; font-size:20px; }
                p  { margin:0 0 14px; color:%s; font-size:14px; line-height:1.75; }
                .code-wrap { background:%s; border:2px dashed %s;
                              border-radius:12px; text-align:center;
                              padding:26px 16px; margin:24px 0; }
                .code-digits { display:block; font-size:42px; font-weight:900;
                                letter-spacing:14px; color:%s;
                                font-family:'Courier New',monospace; }
                .code-exp { display:block; margin-top:10px; font-size:12px; color:%s; }
                hr { border:none; border-top:1px solid %s; margin:22px 0; }
                .badge { display:inline-block; background:%s; color:#fff;
                          font-size:10px; font-weight:700; letter-spacing:1.5px;
                          text-transform:uppercase; padding:4px 12px;
                          border-radius:20px; margin-bottom:14px; }
                .info-table { width:100%%; border-collapse:collapse; margin:16px 0 24px; }
                .info-table td { padding:9px 12px; font-size:13px;
                                  border-bottom:1px solid %s; }
                .info-table td:first-child { color:%s; font-weight:600;
                                              width:45%%; }
                .info-table td:last-child  { color:%s; text-align:right; }
                .info-table tr:last-child td { border-bottom:none; }
                .total-row td { background:%s; color:#fff !important;
                                 font-size:14px; font-weight:700;
                                 border-radius:6px; }
                .btn { display:inline-block; margin-top:8px;
                        background:%s; color:#fff !important;
                        font-size:13px; font-weight:700; padding:12px 28px;
                        border-radius:8px; text-decoration:none;
                        letter-spacing:.5px; }
                .note { font-size:12px; color:%s; }
              </style>
            </head>
            <body>
              <div class="wrap">
                <div class="hdr">
                  <p class="hdr-logo">GARAGEM 52</p>
                  <p class="hdr-sub">Serviços Automotivos</p>
                </div>
                <div class="body">
                  %s
                </div>
                <div class="ftr">
                  <p>Este e-mail foi gerado automaticamente pelo sistema <strong>Garagem52</strong>.</p>
                  <p>Por favor, não responda este e-mail.</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                title,
                BG, WHITE, BORDER,
                BRAND,
                BG, BORDER, MUTED,
                DARK, MUTED,
                CODE_BG, BRAND,
                BRAND, MUTED,
                BORDER,
                BRAND,
                BORDER, DARK, MUTED,
                BRAND,
                BRAND, MUTED,
                bodyHtml
        );
    }

    public static String passwordReset(String userName, String code) {
        String body = """
            <span class="badge">🔐 Segurança da Conta</span>
            <h2>Redefinição de Senha</h2>
            <p>Olá, <strong>%s</strong>!</p>
            <p>Recebemos uma solicitação para redefinir a senha da sua conta na <strong>Garagem52</strong>.
               Use o código abaixo no aplicativo para criar uma nova senha:</p>
            <div class="code-wrap">
              <span class="code-digits">%s</span>
              <span class="code-exp">⏱ Válido por <strong>30 minutos</strong></span>
            </div>
            <hr/>
            <p class="note">
              Se você <strong>não solicitou</strong> a redefinição de senha, ignore este e-mail.
              Sua senha permanecerá a mesma e nenhuma alteração será feita na sua conta.
            </p>
            """.formatted(userName, formatCode(code));

        return wrap("Garagem52 — Redefinição de Senha", body);
    }

    public static String loginCode(String userName, String code) {
        String body = """
            <span class="badge">🔑 Verificação de Acesso</span>
            <h2>Seu código de acesso</h2>
            <p>Olá, <strong>%s</strong>!</p>
            <p>Use o código abaixo para concluir o seu login na <strong>Garagem52</strong>:</p>
            <div class="code-wrap">
              <span class="code-digits">%s</span>
              <span class="code-exp">⏱ Válido por <strong>10 minutos</strong></span>
            </div>
            <hr/>
            <p class="note">
              Se você <strong>não tentou fazer login</strong>, ignore este e-mail e sua conta
              permanecerá segura. Recomendamos trocar sua senha caso receba este e-mail sem ter
              feito a solicitação.
            </p>
            """.formatted(userName, formatCode(code));

        return wrap("Garagem52 — Código de Acesso", body);
    }

    public static String orcamentoPdf(String nomeCliente, long orcamentoId,
                                      String descricaoServico, String placa,
                                      String modelo, String valorTotal, String data) {
        String body = """
            <span class="badge">🚗 Orçamento</span>
            <h2>Seu orçamento está pronto!</h2>
            <p>Olá, <strong>%s</strong>!</p>
            <p>O orçamento <strong>#%04d</strong> foi elaborado pela equipe da <strong>Garagem52</strong>
               e está anexado a este e-mail em formato PDF. Confira o resumo abaixo:</p>
            <table class="info-table">
              <tr><td>Nº do Orçamento</td><td>#%04d</td></tr>
              <tr><td>Data</td><td>%s</td></tr>
              <tr><td>Serviço</td><td>%s</td></tr>
              <tr><td>Veículo</td><td>%s</td></tr>
              <tr><td>Placa</td><td>%s</td></tr>
              <tr class="total-row"><td>Valor Total</td><td>%s</td></tr>
            </table>
            <p>O PDF completo com todos os detalhes, peças e materiais está em anexo.</p>
            <hr/>
            <p class="note">
              Dúvidas? Entre em contato com a nossa equipe. Agradecemos a preferência pela
              <strong>Garagem52</strong> — cuidando do seu veículo com qualidade e transparência.
            </p>
            """.formatted(
                nomeCliente,
                orcamentoId, orcamentoId,
                data, descricaoServico, modelo, placa, valorTotal
        );

        return wrap("Garagem52 — Orçamento #" + String.format("%04d", orcamentoId), body);
    }

    private static String formatCode(String code) {
        if (code != null && code.length() == 6) {
            return code.substring(0, 3) + " " + code.substring(3);
        }
        return code;
    }
}
