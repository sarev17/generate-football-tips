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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
         * essa função testa o tamanho da imagem buscada,se retornar -1 é porque ela não existe
         */
        
        Funcoes f = new Funcoes();
        String caminho = "";
        caminho = f.obterCaminho("../generate-football-tips/src/escudos/" + liga + "/" + clube + ".png");
        if (new ImageIcon(caminho).getIconHeight() == (-1)) {
            System.out.println("salvando imagem");
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
                        System.out.println(a.getTimeCasa()+" salvo");
                    }
                    if (buscarEscudo(a.getTimeFora(), competicao)) {
                        f.salvarImagem(link[1], competicao, a.getTimeFora());
                        System.out.println(a.getTimeFora()+" salvo");
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

                Analise a;
                a = null;
                a = new Analise();

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

            System.out.println("imagem em \n" + dir.getAbsolutePath() + "\\" + competicao + "\\" + clube + ".png");

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
            System.out.println(arquivos.getName());
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

}
