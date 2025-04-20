package com.example.startupback.torneio;

import com.example.startupback.startup.Startup;

public record BatalhaResponseDTO(
        Long id,
        Startup startupA,
        Startup startupB,
        Integer pontuacaoA,
        Integer pontuacaoB,
        boolean finalizada,
        Startup vencedora
) {
    public BatalhaResponseDTO(Batalha batalha) {
        this(
                batalha.getId(),
                batalha.getStartupA(),
                batalha.getStartupB(),
                batalha.getPontuacaoA(),
                batalha.getPontuacaoB(),
                batalha.isFinalizada(),
                batalha.getVencedora()
        );
    }
}
