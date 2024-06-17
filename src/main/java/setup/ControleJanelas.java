package setup;

import com.github.britooo.looca.api.group.janelas.Janela;
import componentes.GrupoJanelas;
import dao.JanelasDao;

import java.util.ArrayList;
import java.util.List;

public class ControleJanelas {
    GrupoJanelas gp = new GrupoJanelas();
    private List<String> janelasProibidas;
    private List<Janela> janelasAbertas;

    public ControleJanelas() {
        this.janelasProibidas = new JanelasDao().buscarProcessosLiberados();
        this.janelasAbertas = gp.getJanelas();
    }

    public List<Janela> verificandoJanela() {
        List<Janela> janelasNãoPermitidas = new ArrayList<>();
        System.out.println("JANELAS QUE ESTAO VINDO DO BANcO: " + this.janelasProibidas);

        for (Janela janelaAberta : janelasAbertas) {
            Boolean janelaPermitida = true;

            for (String janelaPermitidaTitulo : janelasProibidas) {
                if (janelaAberta.getTitulo().toLowerCase().contains(janelaPermitidaTitulo.toLowerCase())) {
                    janelaPermitida = false;
                    break;
                }
            }

            if (janelaPermitida) {
                for (String janelaPermitidaComando : janelasProibidas) {
                    if (janelaAberta.getComando().toLowerCase().contains(janelaPermitidaComando.toLowerCase())) {
                        janelaPermitida = false;
                        break;
                    }
                }
            }

            if (!janelaPermitida) {
                //System.out.println("Janela não é permitida - Titulo: " + janelaAberta.getTitulo() + ", Comando: " + janelaAberta.getComando());
                janelasNãoPermitidas.add(janelaAberta);
            }
        }

        return janelasNãoPermitidas;
    }

}
