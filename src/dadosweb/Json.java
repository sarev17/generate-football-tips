/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dadosweb;

import BancoDados.DAOOdds;
import BancoDados.InserirDados;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author andre
 */
public class Json {

    public void tratarDados(String path) throws FileNotFoundException, IOException, ParseException {

        JSONObject jb;
        JSONObject jb2;
        JSONArray ja = new JSONArray();
        JSONParser parser = new JSONParser();
        BufferedReader br = new BufferedReader(new FileReader(path));

        int contLinha = 0;

        String linha;
        List l = new ArrayList();

        //salvando linhas do arquivo em array
        while ((linha = br.readLine()) != null) {
            l.add(linha);
        }
        br.close();

        //obtendo dados do confronto
        jb = (JSONObject) parser.parse(l.get(0).toString());

        ja.add(jb.get("mc"));

        String dados = ja.get(0).toString().substring(21, ja.get(0).toString().length() - 21);

        jb = (JSONObject) parser.parse(dados);

        String confronto = jb.get("eventName").toString();
        String data = jb.get("marketTime").toString().substring(0, 10);
        int IDEvento = Integer.parseInt(jb.get("eventId").toString());
        String mercado = jb.get("marketType").toString();
        String listOdds = "";
        double oddIUnder = 0.0;
        double oddIOver = 0.0;
        

        List priOdds = new ArrayList();
        //obtendo odd
        //jb2 = (JSONObject) parser.parse(ja.get(0).toString());
        //System.out.println(confronto + "\n" + IDEvento + "\n" + mercado+"\n"+data);
        for (int i = 0; i < l.size(); i++) {
            jb = (JSONObject) parser.parse(l.get(i).toString());
            ja.add(jb.get("mc"));
            String odds = ja.get(0).toString().substring(1, ja.get(0).toString().length() - 1);
            jb = (JSONObject) parser.parse(odds);
            ja.clear();
            ja.add(jb.get("rc"));
            String[] odd = ja.toString().substring(2, ja.toString().length() - 2).replace(",{", "#{").split("#");

            //separa as odds do mercado por '#'
            if (odd.length > 1) {

                jb = (JSONObject) parser.parse(odd[0]);
                jb2 = (JSONObject) parser.parse(odd[1]);

                listOdds = listOdds + "#" + jb.toString() + "#" + jb2.toString();

                priOdds.add(jb.get("ltp").toString());
                priOdds.add(jb2.get("ltp").toString());

                if (oddIOver == 0.0) {
                    oddIUnder = Double.parseDouble(priOdds.get(1).toString());
                    oddIOver = Double.parseDouble(priOdds.get(0).toString());
                }
                
                //System.out.println(jb.get("ltp")+" "+jb2.get("ltp"));

            }

            ja.clear();
        }

        System.out.println(listOdds);
        //System.out.println(oddIOver + " " + oddIUnder);
        InserirDados id = new InserirDados();

       
        
        
        String sql = "INSERT INTO odds(mercado,ID,confronto,dataC,oddsT,oddIOver,oddIUnder) VALUES(?,?,?,?,?,?,?)";
        if (!"".equals(listOdds)) {
            id.inserirOdds(IDEvento, confronto, listOdds, data, oddIUnder, oddIOver, mercado,sql);
        }    


        System.out.println(confronto);
        System.out.println(data);
        System.out.println("concluido");

    }

}
