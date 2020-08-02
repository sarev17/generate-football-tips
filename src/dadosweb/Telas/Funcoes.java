/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dadosweb.Telas;

import BancoDados.DAO;
import BancoDados.DAOOdds;
import BancoDados.InserirDados;
import BancoDados.ObterDados;
import dadosweb.Json;
import static dadosweb.Telas.Principal.deAccent;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author andre
 *
 */
public class Funcoes {

    public boolean buscarEscudo(String clube, String liga) throws IOException, URISyntaxException {

        /**
         * essa função testa o tamanho da imagem buscada,se retornar -1 é porque
         * ela não existe
         */
        Funcoes f = new Funcoes();
        String caminho = "";
        caminho = f.obterCaminho("../generate-football-tips/src/escudos/" + liga + "/" + clube + ".png");
        if (new ImageIcon(caminho).getIconHeight() == (-1)) {
            //System.out.println("salvando imagem");
            return true;
        } else {
            return false;
        }

    }

    public void salvarEscudos(String competicao) {

        String sql = "select *from prognosticos where competicao = \"" + competicao + "\" group by timeCasa";

        ObterDados obt;
        Funcoes f = new Funcoes();
        try {
            obt = new ObterDados();

            obt.ProgSalvo(sql).forEach((a) -> {
                try {
                    String[] link = a.getLogo().split(",");

                    if (buscarEscudo(a.getTimeCasa(), competicao)) {
                        f.salvarImagem(link[0], competicao, a.getTimeCasa());
                        //System.out.println(a.getTimeCasa() + " salvo");
                    }
                    if (buscarEscudo(a.getTimeFora(), competicao)) {
                        f.salvarImagem(link[1], competicao, a.getTimeFora());
                        // System.out.println(a.getTimeFora() + " salvo");
                    }
                } catch (IOException | URISyntaxException ex) {
                    Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

        } catch (IOException ex) {
            Logger.getLogger(AtuEscudos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String obterCaminho(String nomeArq) throws URISyntaxException, IOException {

        //retorna o caminho absoluto de algum arquivo do projeto
        String path = new File(nomeArq).getCanonicalPath();

        return path;
    }

    public void abrirLink(String link) throws IOException, URISyntaxException {

        // abre a pagina da web no navegador padrao
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(link));
        } catch (URISyntaxException ex) {
            JOptionPane.showMessageDialog(null, "erro ao acessar o link\n" + ex);
        }
    }

    public Document buscarArquivo(String path) {

        Path caminho = Paths.get(path);

        String leitura;
        Document doc;

        try {
            byte[] texto = Files.readAllBytes(caminho);
            leitura = new String(texto);
            doc = Jsoup.parse(leitura);

            return doc;

        } catch (IOException e) {
            System.out.println("Arquivo nao encontrado\n" + e);
            return null;
        }

    }

    public void analisarJogo(String url) {
        new Thread() {
            @Override
            public void run() {

                Analise a = new Analise();

                a.lastGames(url);
                try {
                    a.desempenho(url);
                } catch (IOException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }

                a.show();
                a.setExtendedState(JFrame.MAXIMIZED_BOTH);

            }
        }.start();

    }

    /**
     *
     * @param prog prognostico gerado
     * @param placar placarfinal
     * @return red/grean
     * @throws IOException
     */
    public String testarProg(String prog, String placar) throws IOException {

        String h2h = null;
        String ou1 = null;
        String ou2 = null;
        String resultado = null;
        String[] resultadoprog = null;

        String[] p = placar.split("-");

        if (p.length > 0) {
            if (!p[0].equals("") && p[0].length() == 1 && !"x".equals(p[0])) {

                if (Integer.parseInt(p[0].trim()) > Integer.parseInt(p[1].trim())) {
                    h2h = "Casa";
                }

                if (Integer.parseInt(p[0].trim()) < Integer.parseInt(p[1].trim())) {
                    h2h = "Fora";
                }

                if (Integer.parseInt(p[0].trim()) == Integer.parseInt(p[1].trim())) {
                    h2h = "Empate";
                }

                if ((Integer.parseInt(p[0].trim()) + Integer.parseInt(p[1].trim())) > 2) {
                    ou2 = "Over 2.5";
                } else {
                    ou2 = "Under 2.5";
                }

                if ((Integer.parseInt(p[0].trim()) + Integer.parseInt(p[1].trim())) > 1) {
                    ou1 = "Over 1.5";
                } else {
                    ou1 = "Under 1.5";
                }

                resultadoprog = prog.split("-");

                if (resultadoprog[1].trim().equals(h2h)
                        || resultadoprog[1].trim().equals(ou2)
                        || resultadoprog[1].trim().equals(ou1)) {
                    resultado = "Green";
                } else {
                    resultado = "Red";
                }

                return resultado;
            }
        }

        return "ND";

    }

    public boolean buscarDuploGen(String sql, String labelCol) {
        try {

            ObterDados obt = new ObterDados();

            if (obt.obterIds(sql, labelCol) != null) {
                System.out.println("ja esta no banco");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Erro na consulta id");
        }
        return false;
    }

    public boolean buscarDuplo(String link) {

        String sql = "select competicao,link,count(result) as greens,count(data) as jogos from prognosticos \n"
                + "		where link = \"" + link + "\"";

        try {

            ObterDados dad = new ObterDados();
            for (DAO a : dad.ConsultaJoin(sql)) {

                if (a.getCompeticao() != null) {
                    return true;
                }
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public void salvarImagem(String link, String comp, String clube) {

        URL logo;

        try {

            URL url = new URL(link);
            BufferedImage imagem = ImageIO.read(url);

            File dir = new File("src\\escudos\\" + comp);
            dir.mkdir();

            // ImageIO.write(imagem, "png", new File("D:\\documentos\\engenharia\\projetos\\DadosWeb\\src\\escudos\\" + comp + "\\" + clube + ".png"));
            ImageIO.write(imagem, "png", new File(dir.getAbsolutePath() + "\\" + clube + ".png"));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro al dsalvar imagem \n" + e);
        }

    }

    public ImageIcon buscarImagem(String competicao, String clube, int wt, int ht) {

        try {

            File dir = new File("src\\escudos\\");

            ImageIcon icon = new ImageIcon(dir.getAbsolutePath() + "\\" + competicao + "\\" + clube + ".png");

            icon.setImage(icon.getImage().getScaledInstance(wt, ht, 1));

            // System.out.println("imagem em \n" + dir.getAbsolutePath() + "\\" + competicao + "\\" + clube + ".png");
            return icon;
        } catch (Exception e) {
            System.out.println("Não foi possivel obter a imagem");
        }

        return null;

    }

    public ArrayList CampanhaTimes(String timeCasa, String timeFora, String prog) throws SQLException {

        String[] prognostico = prog.split("-");
        String progf = "%" + prognostico[1];

        String sql = "select  (greenMerCasa*100/totCasa) as competicao,(greenCasa*100/totCasa) as greens,(greenMerFora*100/totFora) as link,(greenFora*100)/totFora as jogos  from\n"
                + "	(select timecasa, count(result) as greenCasa from prognosticos\n"
                + "		where timecasa = \"" + timeCasa + "\" and result = \"green\")a\n"
                + "		\n"
                + "	join\n"
                + "		\n"
                + "	(select timecasa as tc,count(result) as totCasa from prognosticos\n"
                + "		where timecasa = \"" + timeCasa + "\")b\n"
                + "    \n"
                + "	join\n"
                + "    \n"
                + "    (select timefora,count(result) as greenFora from prognosticos\n"
                + "		where timefora = \"" + timeFora + "\" and result = \"green\")c\n"
                + "		\n"
                + "    join    \n"
                + "    \n"
                + "    (select timefora as tf,count(result) as totFora from prognosticos\n"
                + "		where timefora = \"" + timeFora + "\")d\n"
                + "\n"
                + "	join\n"
                + "    \n"
                + "	(select timecasa tc1,right(prog,7),count(result) as greenMerCasa from prognosticos\n"
                + "		where timecasa=\"" + timeCasa + "\" and result=\"green\"  and prog like \"" + progf + "\")e\n"
                + "    \n"
                + "    join\n"
                + "    \n"
                + "    (select timefora tf1,right(prog,7),count(result) as greenMerFora from prognosticos\n"
                + "		where timefora=\"" + timeFora + "\" and result=\"green\"   and prog like \"" + progf + "\")f\n"
                + "    ";

        ArrayList l = new ArrayList();

        try {
            ObterDados obt = new ObterDados();

            //green -> campahna casa
            //competicao -> campanha casa no mercado 
            //link -> campanha fora mercado
            //jogos -> campanha fora
            for (DAO a : obt.ConsultaJoin(sql)) {
                l.add(a.getGreen());
                l.add(a.getCompeticao());
                l.add(a.getTjogos());
                l.add(a.getLink());

                return l;
            }

        } catch (IOException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public ColorirLinha ColorLinha(JTable tabela, int row, Color corFundo, Color corTexto) {

        ColorirLinha co = new ColorirLinha();
        co.fundo = corFundo;
        co.texto = corTexto;
        co.fundodef = tabela.getBackground();
        tabela.getForeground();
        co.linha = row;

        return co;

    }

    public File[] listarArquivos(String dir) {

        File file = new File(dir);
        File afile[] = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            //System.out.println(arquivos.getName());
        }

        return afile;

    }

    public void salvarJson(String path) throws IOException, FileNotFoundException, ParseException {
        Funcoes f = new Funcoes();
        File[] fi = f.listarArquivos(path);
        Json j = new Json();
        for (File fi1 : fi) {
            j.tratarDados(path + "\\" + fi1.getName());
        }
    }

    /**
     *
     * @param v1 odd casa
     * @param v2 odd fora
     * @param nomeCasa
     * @param nomeFora
     * @param progVit vitorias em casa
     * @param progVit1 fora
     * @return prognostico+odd
     */
    public String progH2H(float v1, float v2, JLabel nomeCasa, JLabel nomeFora,
            JProgressBar progVit, JProgressBar progVit1) {

        // System.out.println("matchodds");
        // System.out.println("v1 e v2 = " + v1 + " " + v2);
        DecimalFormat df = new DecimalFormat("0.00");

        if (v1 > 50) {
            if (progVit.getValue() > 49) {
                return df.format(((1 / (v1)) * 100)) + " - " + nomeCasa.getText();
            }
        }
        if (v2 > 50) {
            if (progVit1.getValue() > 49) {
                return df.format(((1 / (v2)) * 100)) + " - " + nomeFora.getText();
            }
        }

        if (v1 > v2) {
            if ((v1 - v2) > 20 && progVit.getValue() > 49) {
                return df.format(((1 / (v1)) * 100)) + " - " + nomeCasa.getText();
            }
        }

        if (v2 > v1) {
            if ((v2 - v1) > 20 && progVit1.getValue() > 49) {
                return df.format(((1 / (v2)) * 100)) + " - " + nomeFora.getText();
            }
        }

        return "";
    }

    /**
     *
     * @param res saldo de gols em casapartida
     * @param res2 fora
     * @param lnp1 sequencia de vitorias
     * @param lnp fora
     * @param progVit vitorias em casa
     * @param progVit1 fora
     * @param progDer derrotas em casa
     * @param progDer1 fora
     * @param progMaior partidas que o saldo de gols é maior que 1
     * @param progMaior1 fora
     * @param progMenor partidas que o saldo é menor que -1
     * @param progMenor1 fora
     * @param cap1 vitTimeCasa+DerTimeFora
     * @param cap2 fora
     * @return odd da analise [0] casa, [1] fora
     */
    public String[] gerarODD(ArrayList res, ArrayList res2, JLabel lnp1, JLabel lnp,
            JProgressBar progVit, JProgressBar progVit1, JProgressBar progDer, JProgressBar progDer1,
            JProgressBar progMaior, JProgressBar progMaior1, JProgressBar progMenor, JProgressBar progMenor1,
            float cap1, float cap2) {

        DecimalFormat df = new DecimalFormat("0.00");
        int i = 0;
        int cont = 0;
        float camp = 0, camp1 = 0;

        while (cont < res.size()) {
            if ((int) res.get(cont) > 1) {
                i++;
            }
            cont++;

        }
        camp += 3 * i;
        i = 0;
        cont = 0;
        while (cont < res2.size()) {
            if ((int) res2.get(cont) > 1) {
                i++;
            }
            cont++;

        }
        camp1 += 3 * i;

        i = 0;
        cont = 0;
        while (cont < res.size()) {
            if ((int) res.get(cont) < 1) {
                i++;
            }
            cont++;

        }
        camp -= 2 * i;

        i = 0;
        cont = 0;
        while (cont < res2.size()) {
            if ((int) res2.get(cont) < 1) {
                i++;
            }
            cont++;

        }
        camp1 -= 2 * i;

        camp += Integer.parseInt(lnp1.getText()) * 5;
        camp1 += Integer.parseInt(lnp.getText()) * 5;

        camp += ((progVit.getValue() - progDer.getValue()) / 10) + (((progMaior.getValue() - progMenor.getValue()) / 10) * 2);
        camp1 += (progVit1.getValue() - progDer1.getValue()) / 10 + (((progMaior1.getValue() - progMenor1.getValue()) / 10) * 2);

        String[] Odds = new String[2];

        Odds[0] = cap1 + cap1 * camp / 100 + "%";
        Odds[1] = cap2 + cap2 * camp1 / 100 + "%";

        //System.out.println("classe gerarOdds \n"+Arrays.toString(Odds)+"\n"+camp+" "+camp1);
        return Odds;
    }

    /**
     *
     * @param jTablecamp1 tabela onde os resultados serão salvos
     * @return tabela com as porcentagens de acertos em ordem crescente
     */
    public JTable Resultadosgerais(JTable jTablecamp1) {

        jTablecamp1.getColumnModel().getColumn(0).setMinWidth(50);
        jTablecamp1.getColumnModel().getColumn(0).setMaxWidth(50);
        jTablecamp1.getColumnModel().getColumn(1).setMaxWidth(80);
        jTablecamp1.getColumnModel().getColumn(1).setMinWidth(80);
        jTablecamp1.getColumnModel().getColumn(2).setMaxWidth(80);
        jTablecamp1.getColumnModel().getColumn(2).setMinWidth(80);
        jTablecamp1.getColumnModel().getColumn(3).setMaxWidth(50);
        jTablecamp1.getColumnModel().getColumn(3).setMinWidth(50);
        jTablecamp1.getColumnModel().getColumn(4).setMaxWidth(50);
        jTablecamp1.getColumnModel().getColumn(4).setMinWidth(50);
        jTablecamp1.setRowHeight(30);
        TableColumnModel columnModel1 = jTablecamp1.getColumnModel();
        JTableRendererImageCell renderer1 = new JTableRendererImageCell();
        DefaultTableModel bestCamp = (DefaultTableModel) jTablecamp1.getModel();
        columnModel1.getColumn(0).setCellRenderer(renderer1);

        String sql = "select timecasa as competicao, (greencasa+greenfora)*100/(jcasa+jfora) as \"greens\",c1 as link,jcasa+jfora as jogos from\n"
                + "		(((Select timecasa as tc,count(timecasa) as Jcasa,competicao as c from prognosticos\n"
                + "				group by timecasa order by timecasa)a\n"
                + "		\n"
                + "        join\n"
                + "		\n"
                + "        (select timecasa,count(timecasa) as greenCasa,competicao as c1  from prognosticos	\n"
                + "			where result=\"green\"\n"
                + "				group by timecasa order by timecasa)b\n"
                + "			on  timecasa=tc)\n"
                + "		\n"
                + "        join\n"
                + "		\n"
                + "        (select timefora,count(timecasa) as greenfora,competicao as c2  from prognosticos	\n"
                + "			where result=\"green\"\n"
                + "				group by timefora order by timefora)c    \n"
                + "		on timecasa=timefora)\n"
                + "        \n"
                + "        join\n"
                + "		\n"
                + "		(select timefora as y,count(timeFora) as Jfora,competicao as c3  from prognosticos	\n"
                + "		group by y order by y)d\n"
                + "		on timecasa=y\n"
                + "	\n"
                + "    order by greens desc\n"
                + "        ";

        Funcoes f = new Funcoes();

        try {

            ObterDados o = new ObterDados();

            for (DAO a : o.ConsultaJoin(sql)) {
                Object[] inserir = {f.buscarImagem(a.getLink(), a.getCompeticao(), 25, 25),
                    a.getCompeticao(), a.getLink(), a.getGreen() + "%", a.getTjogos()};
                bestCamp.addRow(inserir);
            }

        } catch (IOException | SQLException ex) {
            Logger.getLogger(DadosResult.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jTablecamp1;
    }

    public String corrigirData(String data) {

        String coc[] = data.split(" ");

        switch (coc[1]) {

            case "janeiro":
                coc[1] = "01";
                break;

            case "fevereiro":
                coc[1] = "02";
                break;

            case "março":
                coc[1] = "03";
                break;

            case "abril":
                coc[1] = "04";
                break;

            case "maio":
                coc[1] = "05";
                break;

            case "junho":
                coc[1] = "06";
                break;

            case "julho":
                coc[1] = "07";
                break;

            case "agosto":
                coc[1] = "08";
                break;

            case "setembro":
                coc[1] = "09";
                break;

            case "outubro":
                coc[1] = "10";
                break;

            case "novembro":
                coc[1] = "11";
                break;

            case "dezembro":
                coc[1] = "12";
                break;

        }

        return coc[2] + coc[1] + coc[0];

    }

    /**
     *
     * @param TableConfrontos table com os confrontos a analisar
     * @param TableAnalises table que será salvo os prognosticos gerados
     * @param i linha ta tableconfrontos que está sendo analizada
     * @param docLive documento html do site
     * @param dados Array(nomecasa,nomefora,progH2H,progGols)
     * @param prog prognostico gerado
     * @param LabelNCompeticao nome da comp
     * @param data data do confronto
     * @param sql comando sql
     * @return Array[objects] contendo os dados para preencher a table analises
     * @throws IOException
     * @throws SQLException
     */
    public ArrayList<Object[]> salvarPrognostico(JTable TableConfrontos, JTable TableAnalises, int i, Document docLive, ArrayList dados, String prog,
            String LabelNCompeticao, String data, String sql) throws IOException, SQLException {

        ArrayList<Object[]> objetos = new ArrayList();
        DefaultTableModel analises = (DefaultTableModel) TableAnalises.getModel();
        String linkconf = TableConfrontos.getValueAt(i, 3).toString();
        docLive = Jsoup.connect(linkconf.replace("prelive", "live")).get();
        Funcoes f = new Funcoes();

        Object[] inserir = {f.buscarImagem(LabelNCompeticao, deAccent(dados.get(0).toString()), 30, 30),
            dados.get(0) + " x " + dados.get(1) + "   " + prog,
            f.buscarImagem(LabelNCompeticao, deAccent(dados.get(1).toString()), 30, 30), linkconf};
        objetos.add(inserir);

        ArrayList l;
        String campanha;
        l = f.CampanhaTimes(dados.get(0).toString(), dados.get(1).toString(), dados.get(2).toString());
        if (l != null) {
            campanha = l.get(0) + "% | " + dados.get(0).toString() + " x "
                    + dados.get(1).toString() + " | " + l.get(3) + "%";

        } else {
            campanha = "";
        }

        f.salvarEscudos(LabelNCompeticao);

        Object[] inserirCampanha = {f.buscarImagem(LabelNCompeticao,
            deAccent(dados.get(0).toString()), 30, 30),
            campanha,f.buscarImagem(LabelNCompeticao, 
                    deAccent(dados.get(1).toString()), 30, 30)};
        objetos.add(inserirCampanha);

        //inserindo no banco
        data = docLive.getElementsByClass("gamehead").get(1).text();
        data = data.substring(0, data.length() - 8);
        InserirDados id = new InserirDados();

        id.inserir(f.corrigirData(data),
                deAccent(docLive.getElementsByClass("gamehead").get(2).text()),
                deAccent(dados.get(0).toString()), deAccent(dados.get(1).toString()),
                deAccent(prog),
                f.testarProg(deAccent(prog),
                        TableConfrontos.getValueAt(i, 1).toString()), linkconf,
                docLive.getElementsByClass("stats-game-head-teamname hide-mobile")
                        .get(0).select("a").select("img").attr("src") + ","
                + docLive.getElementsByClass("stats-game-head-teamname hide-mobile")
                        .get(1).select("a").select("img").attr("src"),
                sql, TableConfrontos.getValueAt(i, 1).toString());

        //dados.clear();
        return objetos;
    }


    /**
     * 
     * @param gols lista[media marcado casa, media sofrido casa, mm fora, ms fora, macouCasa, sofreuCasa, mfora,sfora,num jogos]
     * @return prognostico BTS
     */
    
    public String gerarBTS(float[] gols){
        
        if((gols[0]+gols[1])/2>2)
            if((gols[2]+gols[3])/2>2)
                if(gols[4]>6&&gols[6]>6)
                    if(gols[5]>5&&gols[7]>5)
                        return 1/((gols[4]+gols[6])/(gols[8])*2)+" - BTS";
       return "";
        
    }

}
