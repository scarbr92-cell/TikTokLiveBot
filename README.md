# TikTok Live Bot — protótipo Android

Este projeto é um **protótipo**, não um bot funcional de chat.

## O que já existe
- Tela Android para informar o link da live, comando e intervalo.
- Validação básica dos campos.
- Serviço em primeiro plano com notificação persistente.
- Botão para iniciar/parar uma sessão de teste.

## O que NÃO existe
- Não faz login no TikTok.
- Não abre nem acompanha a transmissão.
- Não envia `!pontos` ou qualquer outra mensagem.
- Não contorna CAPTCHA, limites, autenticação ou proteções da plataforma.

O TikTok não oferece uma API pública geral para enviar mensagens ao chat como uma conta comum. Antes de adicionar essa função, é necessário confirmar um método oficial/permitido e compatível com as regras da plataforma. **Nunca coloque sua senha do TikTok neste aplicativo.**

## Como abrir
1. Extraia o ZIP.
2. Abra a pasta `TikTokLiveBotAndroid` no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Conecte o Android com depuração USB ou use um emulador.
5. Execute o app.

Requisitos: Android Studio recente, JDK 17 e Android SDK 35.
