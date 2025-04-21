package com.example.startupback.batalha;

import com.example.startupback.startup.Startup;
import com.example.startupback.startup.StartupResponseDTO;

public record BatalhaResponseDTO(
        Long id,
        StartupResponseDTO startupA,
        StartupResponseDTO startupB,
        Integer pontuacaoA,
        Integer pontuacaoB,
        boolean finalizada,
        String vencedora
) {
    public BatalhaResponseDTO(Batalha batalha) {
        this(
                batalha.getId(),
                new StartupResponseDTO(batalha.getStartupA()),
                new StartupResponseDTO(batalha.getStartupB()),
                batalha.getPontuacaoA(),
                batalha.getPontuacaoB(),
                batalha.isFinalizada(),
                batalha.getVencedora() != null ? batalha.getVencedora().getNome() : null
        );
    }
}
