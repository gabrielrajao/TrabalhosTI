package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Agenda {

    // atributos
    private int usuario;
    private int planta;
    private LocalDate datainicio;
    private String nome;
    private String rega;
    private String poda;
    private String exposicao;
    private String descricao;

    // construtores
    public Agenda() {
        usuario = -1;
        planta = -1;
        datainicio = null;
        nome = "";
        rega = "";
        poda = "";
        exposicao = "";
        descricao = "";
    }

    public Agenda(int usuario, int planta, LocalDate datainicio, String nome, String rega, String poda, String exposicao,String descricao) {
        this.usuario = usuario;
        this.planta = planta;
        this.datainicio = datainicio;
        this.nome = nome;
        this.rega = rega;
        this.poda = poda;
        this.exposicao = exposicao;
        this.descricao = descricao;
    }

    //gets
    public int getUsuario () {
        return usuario;
    }
    public int getPlanta () {
        return planta;
    }
    public LocalDate getDataInicio() {
    	return datainicio;
    }
    public String getNome () {
        return nome;
    }
    public String getRega () {
        return rega;
    }
    public String getPoda () {
        return poda;
    }
    public String getExposicao () {
        return exposicao;
    }
    public String getDescricao () {
        return descricao;
    }

    //sets
    public void setUsuario (int usuario) {
        this.usuario = usuario;
    }
    public void setPlanta (int planta) {
        this.planta = planta;
    }
    public void setDataInicio(LocalDate datainicio) {
    	this.datainicio = datainicio;
    }
    public void setNome (String nome) {
        this.nome = nome;
    }
    public void setRega (String rega) {
        this.rega = rega;
    }
    public void setPoda (String poda) {
        this.poda = poda;
    }
    public void setExposicao (String exposicao) {
        this.exposicao = exposicao;
    }
    public void setDescricao (String descricao) {
        this.descricao = descricao;
    }
    
}
