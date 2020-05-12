/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author andre
 */
public class ObterDados {

    Connection con = null;

    public ObterDados() throws IOException {
        con = Conexao.getConnection();
    }

    public List<DAO> ConsultaJoin(String sql) throws SQLException {

        List<DAO> resultados = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = con.prepareStatement(sql);

            rs = stmt.executeQuery();

            //colocando os valors guardados no resultSet no Array
            while (rs.next()) {

                DAO confronto = new DAO();

                confronto.setCompeticao(rs.getString("competicao"));
                confronto.setGreen(rs.getInt("greens"));
                confronto.setLink(rs.getString("link"));
                confronto.setTjogos(rs.getInt("jogos"));

                resultados.add(confronto);

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        return resultados;

    }

    public List<DAO> ProgSalvo(String sql) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<DAO> confrontos;
        confrontos = new ArrayList<>();

        try {

            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            //colocando os valors guardados no resultSet no Array
            while (rs.next()) {

                DAO confronto = new DAO();

                confronto.setIdPrognostico(rs.getString("idPrognosticos"));
                confronto.setData(rs.getString("data"));
                confronto.setCompeticao(rs.getString("competicao"));
                confronto.setTimeCasa(rs.getString("timeCasa"));
                confronto.setTimeFora(rs.getString("timeFora"));
                confronto.setProg(rs.getString("prog"));
                confronto.setPlacar(rs.getString("placar"));
                confronto.setResult(rs.getString("result"));
                confronto.setLink(rs.getString("link"));
                confronto.setLogo(rs.getString("logoTimes"));

                confrontos.add(confronto);

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro " + ex);
        } finally {
            //Conexao.closeConnection(con, stmt, rs);
        }

        return confrontos;
    }

    public List<DAOOdds> obterIds(String sql, String labelCol) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<DAOOdds> ids = new ArrayList<>();

        DAOOdds id = new DAOOdds();

        try {

            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                id.setId(rs.getInt(labelCol));
                ids.add(id);
            }
            
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro na consulta dos Ids"+e);
        }

        return ids;
    }

}
