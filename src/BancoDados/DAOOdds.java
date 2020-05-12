/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BancoDados;

/**
 *
 * @author andre
 */
public class DAOOdds {
    
    String confronto,oddsT,data,mercado;

    int golsHT,golsFT;

    public int getGolsHT() {
        return golsHT;
    }

    public void setGolsHT(int golsHT) {
        this.golsHT = golsHT;
    }

    public int getGolsFT() {
        return golsFT;
    }

    public void setGolsFT(int golsFT) {
        this.golsFT = golsFT;
    }
    
    public String getMercado() {
        return mercado;
    }

    public void setMercado(String mercado) {
        this.mercado = mercado;
    }
    double oddsIUnder,oddsIOver;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfronto() {
        return confronto;
    }

    public void setConfronto(String confronto) {
        this.confronto = confronto;
    }

    public String getOddsT() {
        return oddsT;
    }

    public void setOddsT(String oddsT) {
        this.oddsT = oddsT;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getOddsIUnder() {
        return oddsIUnder;
    }

    public void setOddsIUnder(double oddsIUnder) {
        this.oddsIUnder = oddsIUnder;
    }

    public double getOddsIOver() {
        return oddsIOver;
    }

    public void setOddsIOver(double oddsIOver) {
        this.oddsIOver = oddsIOver;
    }
    
}
