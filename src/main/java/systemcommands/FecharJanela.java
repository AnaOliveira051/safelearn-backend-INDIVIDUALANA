package systemcommands;

import com.github.britooo.looca.api.group.janelas.Janela;
import setup.ControleJanelas;
import setup.Notificacao;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FecharJanela {
    private ControleJanelas controle = new ControleJanelas();
    private Notificacao notificacao = new Notificacao();
    private List<Janela> janelasNaoPermitidas;

    private static Boolean closeProcess = false;

    public FecharJanela() {

    }

    public void closeWindow() {
        this.janelasNaoPermitidas = controle.verificandoJanela();

        if (janelasNaoPermitidas != null && !janelasNaoPermitidas.isEmpty()) {
            System.out.println("Janelas não permitidas encontradas:");

            for (Janela janelaDavez : janelasNaoPermitidas) {
                String pidJanela = String.valueOf(janelaDavez.getPid());
                String processName = janelaDavez.getTitulo();
                notificacao.SendNotification(janelaDavez.getTitulo());

                if (isWindows()) {
                    callForExecution("taskkill", "/PID", pidJanela);
                } else {
                    callForExecution("pkill", processName);
                }
            }

            controle.verificandoJanela();

        } else {
            System.out.println("Nenhuma janela não permitida encontrada.");
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    public static void callForExecution(String... comand){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                executeComand(comand);
            }
        }, 5000);
    }

    public static void executeComand(String... comand){
        try{
            ProcessBuilder processBuilder = new ProcessBuilder(comand);
            Process process = processBuilder.start();
            Integer exitCode = process.waitFor();

            if(exitCode == 0){
                System.out.println("Comando executado com sucesso");
            }else{
                System.out.println("Falha ao executar o comando.\nCódigo de saída: "+exitCode);
            }

        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
