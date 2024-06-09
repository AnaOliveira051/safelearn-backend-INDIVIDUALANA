import componentes.MemoriaRam;
import componentes.UsoDisco;
import componentes.UsoProcessador;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackIntegracao {

    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/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";

    private static final double RAM_LIMITE = 70.0;
    private static final double RAM_CRITICO = 90.0;
    private static final double CPU_LIMITE = 80.0;
    private static final double CPU_CRITICO = 90.0;
    private static final double DISCO_LIMITE = 70.0;
    private static final double DISCO_CRITICO = 90.0;




    //VC precisa pegar o uso do disco em porcentagem, e arrumar no if aqui em baixo de acordo com os parametros de alerta ali em cima//
    //Bem facil né//
    //Boa sorte//


    public static void sendAlert(String message) {
        try {
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonPayload = "{\"text\":\"" + message + "\"}";
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Mensagem enviada com sucesso.");
            } else {
                System.out.println("Erro ao enviar a mensagem. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MemoriaRam memoriaRam = new MemoriaRam();
        UsoProcessador processador = new UsoProcessador();
        UsoDisco disco = new UsoDisco();

        double usoRam = memoriaRam.getUso();
        double usoCpu = processador.getUso();
        double usoDisco = disco.getUso();

        if (usoRam > RAM_CRITICO) {
            sendAlert(String.format("🚨 Alerta Crítico: A memória RAM está com utilização crítica de recursos: %.2f%%. Ação imediata necessária!", usoRam));
        } else if (usoRam > RAM_LIMITE) {
            sendAlert(String.format("⚠️ Alerta de Desempenho: A memória RAM está com alta utilização de recursos: %.2f%%. Verifique o status para prevenir possíveis falhas.", usoRam));
        }

        if (usoCpu > CPU_CRITICO) {
            sendAlert(String.format("🚨 Alerta Crítico: O processador está com utilização crítica de recursos: %.2f%%. Ação imediata necessária!", usoCpu));
        } else if (usoCpu > CPU_LIMITE) {
            sendAlert(String.format("⚠️ Alerta de Desempenho: O processador está com alta utilização de recursos: %.2f%%. Verifique o status para prevenir possíveis falhas.", usoCpu));
        }

        if (usoDisco > DISCO_CRITICO) {
            sendAlert(String.format("🚨 Alerta Crítico: O disco está com utilização crítica de recursos: %.2f%%. Ação imediata necessária!", usoDisco));
        } else if (usoDisco > DISCO_LIMITE) {
            sendAlert(String.format("⚠️ Alerta de Desempenho: O disco está com alta utilização de recursos: %.2f%%. Verifique o status para prevenir possíveis falhas.", usoDisco));
        }
    }
}
