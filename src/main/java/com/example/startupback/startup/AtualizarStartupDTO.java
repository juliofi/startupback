package com.example.startupback.startup;

public record AtualizarStartupDTO(
        int pontuacao,
        int contPitch,
        int contBug,
        int contTracao,
        int contInvestidor,
        int contPenalidade
) {}
