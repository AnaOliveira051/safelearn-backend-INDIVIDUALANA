package dao;

import conexao.ConexaoServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JanelasDao {
    public List<String> buscarProcessosLiberados() {
        List<String> processosLiberados = new ArrayList<>();
        String sql = "SELECT nomeProcessoLiberado FROM processosLiberados";

        ConexaoServer conexaoServer = new ConexaoServer();
        Connection connectionServer = conexaoServer.getConexao();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connectionServer.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nomeProcesso = rs.getString("nomeProcessoLiberado");
                processosLiberados.add(nomeProcesso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return processosLiberados;
    }
}

