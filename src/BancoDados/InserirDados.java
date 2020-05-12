/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class InserirDados {

    private Connection con = null;

    public InserirDados() throws IOException {
        con = Conexao.getConnection();
    }

    public boolean save(DAO dad, String sql) {

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dad.getData());
            stmt.setString(2, dad.getCompeticao());
            stmt.setString(3, dad.getTimeCasa());
            stmt.setString(4, dad.getTimeFora());
            stmt.setString(5, dad.getProg());
            stmt.setString(6, dad.getResult());
            stmt.setString(7, dad.getLink());
            stmt.setString(8, dad.getLogo());
            stmt.setString(9, dad.getPlacar());
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inserção " + e);
            return false;
        } finally {
            Conexao.closeConnection(con, stmt);
        }

    }
    
    public boolean saveOdds(DAOOdds dad, String sql){
        
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dad.getMercado());
            stmt.setInt(2, dad.getId());
            stmt.setString(3, dad.getConfronto());
            stmt.setString(4, dad.getData());
            stmt.setString(5, dad.getOddsT());
            stmt.setDouble(6, dad.getOddsIOver());
            stmt.setDouble(7, dad.getOddsIUnder());
            
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Erro inserção " + e);
            System.out.println("Erro de inser��o\n"+e);
            return false;
        }finally {
            Conexao.closeConnection(con, stmt);
        }
    }

    public void inserir(String placar, String result, String sql) throws IOException {
        DAO dad = new DAO();
        InserirDados id = new InserirDados();
        dad.setPlacar(placar);
        dad.setResult(result);
        id.save(dad, sql);
        

    }

    public void inserirOdds(int idEvt,String confronto,String oddsT,String data,
            double oddsIUnder,double oddsIOver,String mercado,String sql) throws IOException{
        
        InserirDados id = new InserirDados();
        
        DAOOdds dad = new DAOOdds();
        dad.setMercado(mercado);
        dad.setOddsT(oddsT);
        dad.setId(idEvt);
        dad.setConfronto(confronto);
        dad.setOddsIUnder(oddsIUnder);
        dad.setOddsIOver(oddsIOver);
        dad.setData(data);
        
        id.saveOdds(dad, sql);
        
    }
    
    public void inserir(String data, String competicao,
            String timeCasa, String timeFora, String prog,
            String result, String link, String logo,String sql,String placar) throws IOException {

        InserirDados id = new InserirDados();
        DAO dad = new DAO();

        dad.setData(data);
        dad.setCompeticao(competicao);
        dad.setTimeCasa(timeCasa);
        dad.setTimeFora(timeFora);
        dad.setProg(prog);
        dad.setResult(result);
        dad.setLink(link);
        dad.setLogo(logo);
        dad.setPlacar(placar);
        id.save(dad, sql);
    }

    public void update(String id, String placar, String result) {
        String sql = "UPDATE PROGNOSTICOS SET PLACAR = ?,RESULT=? WHERE IDPROGNOSTICOS = ? ";

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, placar);
            stmt.setString(2, result);
            stmt.setString(3, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro inserção " + ex);

        } finally {
            
        }

    }

}
