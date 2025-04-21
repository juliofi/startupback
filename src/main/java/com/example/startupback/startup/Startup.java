package com.example.startupback.startup;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "startups")
@Entity(name = "startups")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Startup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer ano;
    private String slogan;
    private Integer pontuacao;
    private int contPitch = 0;
    private int contBug = 0;
    private int contTracao = 0;
    private int contInvestidor = 0;
    private int contPenalidade = 0;


    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public Integer getAno() {
        return ano;
    }
    public String getSlogan() {
        return slogan;
    }

    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getContPitch() {
        return contPitch;
    }

    public void setContPitch(int contPitch) {
        this.contPitch = contPitch;
    }

    public int getContBug() {
        return contBug;
    }

    public void setContBug(int contBug) {
        this.contBug = contBug;
    }

    public int getContTracao() {
        return contTracao;
    }

    public void setContTracao(int contTracao) {
        this.contTracao = contTracao;
    }

    public int getContInvestidor() {
        return contInvestidor;
    }

    public void setContInvestidor(int contInvestidor) {
        this.contInvestidor = contInvestidor;
    }

    public int getContPenalidade() {
        return contPenalidade;
    }

    public void setContPenalidade(int contPenalidade) {
        this.contPenalidade = contPenalidade;
    }





    public Startup(StartupRequestDTO data) {
        this.slogan = data.slogan();
        this.ano = data.ano();
        this.nome = data.nome();
        this.pontuacao = data.pontuacao();
    }

}


