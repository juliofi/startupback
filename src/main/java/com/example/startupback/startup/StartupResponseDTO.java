package com.example.startupback.startup;

public record StartupResponseDTO(Long id, String nome, Integer ano, String slogan) {

    public StartupResponseDTO(Startup startup) {
        this(startup.getId(), startup.getNome(), startup.getAno(), startup.getSlogan());
    }

}
