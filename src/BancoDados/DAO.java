/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

//
public class DAO {

    private String data, competicao, timeCasa,
            timeFora, prog,result, link,idPrognostico,placar,logo;
    private int green,tjogos;

    public DAO() {
    }

    public DAO(String competicao, int green,int tjogos) {
        this.competicao = competicao;
        this.green = green;
        this.tjogos = tjogos;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getTjogos() {
        return tjogos;
    }

    public void setTjogos(int tjogos) {
        this.tjogos = tjogos;
    }
    
    
    

    public DAO(String data, String competicao, String timeCasa, 
            String timeFora, String prog, String result, String link, 
            String idPrognostico, String placar,String logo) {
        
        this.data = data;
        this.competicao = competicao;
        this.timeCasa = timeCasa;
        this.timeFora = timeFora;
        this.prog = prog;
        this.result = result;
        this.link = link;
        this.idPrognostico = idPrognostico;
        this.placar = placar;
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPlacar() {
        return placar;
    }

    public void setPlacar(String placar) {
        this.placar = placar;
    }




    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIdPrognostico() {
        return idPrognostico;
    }

    public void setIdPrognostico(String idPrognostico) {
        this.idPrognostico = idPrognostico;
    }

    public String getCompeticao() {
        return competicao;
    }

    public void setCompeticao(String competicao) {
        this.competicao = competicao;
    }

    public String getTimeCasa() {
        return timeCasa;
    }

    public void setTimeCasa(String timeCasa) {
        this.timeCasa = timeCasa;
    }

    public String getTimeFora() {
        return timeFora;
    }

    public void setTimeFora(String timeFora) {
        this.timeFora = timeFora;
    }

    public String getProg() {
        return prog;
    }

    public void setProg(String prog) {
        this.prog = prog;
    }



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    

}

