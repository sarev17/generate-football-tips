/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dadosweb;

import BancoDados.DAO;
import BancoDados.InserirDados;
import BancoDados.ObterDados;
import dadosweb.Telas.AtuEscudos;
import dadosweb.Telas.Competicoes;
import dadosweb.Telas.DadosResult;
import dadosweb.Telas.Funcoes;

import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import dadosweb.Telas.Principal;
import dadosweb.Telas.Prognosticos;
import dadosweb.Telas.Tabela;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.json.simple.parser.ParseException;
import org.jsoup.select.Elements;

/**
 *
 * @author andre
 */
public class DadosWeb {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, URISyntaxException, SQLException, FileNotFoundException, ParseException {

        // Principal p = new Principal();
        // p.show();    
        //Funcoes f = new Funcoes();
       // f.buscarEscudo("Atalanta", "Serie A TIM");
        /*  Document doc = f.buscarArquivo("D:\\documentos\\engenharia\\projetos\\DadosWeb\\src\\dadosweb\\consultaComps.txt");
        Element comps = doc.getElementsByClass("boxed cpt-list").select("div").get(0)
                .getElementsByClass("toggle_content").get(0);
         */
        //System.out.println(f.CampanhaTimes("juventus", "milan", "1.34 - over 2.5"));
        
        Principal p = new Principal();
         p.show();
        
         //DadosResult dr = new DadosResult();
        // dr.show();

        /*
      File[] fi = f.listarArquivos("D:\\documentos\\engenharia\\projetos\\BASIC\\jogos");
      Json j = new Json();
        for (File fi1 : fi) {
            j.tratarDados("D:\\documentos\\engenharia\\projetos\\BASIC\\jogos\\"+fi1.getName());
        }
      
         */
        //f.salvarJson("D:\\\\documentos\\\\engenharia\\\\projetos\\\\BASIC\\\\jogos");
    }

}
