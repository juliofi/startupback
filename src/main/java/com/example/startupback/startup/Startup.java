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

    public Startup(StartupRequestDTO data){
        this.slogan = data.slogan();
        this.ano = data.ano();
        this.nome = data.nome();
    }
}


