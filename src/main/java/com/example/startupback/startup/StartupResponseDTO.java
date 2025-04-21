package com.example.startupback.startup;

public record StartupResponseDTO(Long id, String nome, String slogan, Integer ano, Integer pontuacao) {
    public StartupResponseDTO(Startup s) {
        this(s.getId(), s.getNome(), s.getSlogan(), s.getAno(), s.getPontuacao());
    }
}

