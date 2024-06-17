import componentes.*;
import dao.MaquinaDaoLocal;
import dao.MaquinaDaoServer;
import setup.InserirRegistros;
import systemcommands.FecharJanela;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    private static Runnable tarefaMonitoramento;

    public static void main(String[] args) {
        InserirRegistros inserirRegistros = new InserirRegistros();
        FecharJanela controle = new FecharJanela();

        MaquinaDaoServer maquinaDaoServer = new MaquinaDaoServer();
        MaquinaDaoLocal maquinaDaoLocal = new MaquinaDaoLocal();
        UsoProcessador processador = new UsoProcessador();
        MemoriaRam memoriaRam = new MemoriaRam();
        UsoDisco disco = new UsoDisco();
        GrupoJanelas janelas = new GrupoJanelas();
        Bateria bateria = new Bateria();
        SlackIntegracao slackIntegracao = new SlackIntegracao();
        MaquinaDaoServer maquinaDaoServer1 = new MaquinaDaoServer();

        if (!maquinaDaoLocal.verificarRegistro(processador)) {
            maquinaDaoLocal.inserirDadosMaquina(processador, new Sistema(), 1);
            maquinaDaoLocal.inserirDadosComponente(processador, memoriaRam, disco);
        }
        
        List<Integer> idsComponentesServer = maquinaDaoServer.getIdsComponentes(processador);
        List<Integer> idsComponentesLocal = maquinaDaoLocal.getIdsComponentes(processador);

        if (idsComponentesServer == null || idsComponentesServer.isEmpty()) {
            System.err.println("idsComponentesServer is null or empty");
        }
        if (idsComponentesLocal == null || idsComponentesLocal.isEmpty()) {
            System.err.println("idsComponentesLocal is null or empty");
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable tarefaMonitoramento = new Runnable() {
            @Override
            public void run() {
                try {

                    
                   if (idsComponentesLocal == null || idsComponentesLocal.isEmpty() || idsComponentesLocal.size() < 3) {
                        throw new IllegalStateException("idsComponentesLocal is null, empty, or does not contain enough elements");
                    }
                    if (idsComponentesServer == null || idsComponentesServer.isEmpty() || idsComponentesServer.size() < 3) {
                        throw new IllegalStateException("idsComponentesServer is null, empty, or does not contain enough elements");
                    }

                    maquinaDaoServer.executarComandoDeMaquina(processador);
                    maquinaDaoLocal.monitoramento(processador, memoriaRam, disco, idsComponentesLocal);
                    maquinaDaoServer.monitoramento(processador, memoriaRam, disco, idsComponentesServer);
                    slackIntegracao.alertSlack();
                    maquinaDaoLocal.inserirDadosProcessso(processador, janelas);
                    maquinaDaoServer.inserirDadosProcessso(processador, janelas);
                    maquinaDaoLocal.inserirDadosBateria(processador, bateria);
                    maquinaDaoServer.inserirDadosBateria(processador, bateria);
                    controle.closeWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Erro no monitoramento: " + e.getMessage());
                }
            }
        };
        maquinaDaoServer1.setStatus(processador);
        scheduler.scheduleWithFixedDelay(tarefaMonitoramento, 0, 7, TimeUnit.SECONDS);
    }
}
