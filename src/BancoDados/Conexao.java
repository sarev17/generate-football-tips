/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JOptionPane;
/**
 *
 * @author Rhuan Souza
 */
public class Conexao {
    
    
    
   
    
    
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/prognosticos?characterEncoding=latin1&useConfigs=maxPerformance";
    //private static String USER = ;
    //private static String PASS = "";

    public Conexao() {
        throw new UnsupportedOperationException("Não suportado ainda."); //To change body of generated methods, choose Tools | Templates.
    }
    
  
   
   
    public  static Connection getConnection() throws IOException{
        try {
            
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL,"root","sareverdna");
            
            
        } 
        catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException("Erro de Conexao "+ex);
        }
    }
    
    public static void  closeConnection(Connection con){
        
        if(con!=null){
            try {
                con.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "erro ao fechar conexao "+ex);
            }
        }
    }
    public static void closeConnection(Connection con, PreparedStatement stmt){
        
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "erro ao fechar conexao "+ex);
            }
        }    
        closeConnection(con);
    }
    
    public static void closeConnection(Connection con, PreparedStatement stmt, ResultSet rs){
        
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "erro ao fechar conexao "+ex);
            }
        }
        
        closeConnection(con, stmt);
    }

    Conexao(String Usuario, String Senha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
 
    
}