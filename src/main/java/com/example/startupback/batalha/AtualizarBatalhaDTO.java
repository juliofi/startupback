package com.example.startupback.batalha;

public record AtualizarBatalhaDTO(
        Long id,
        int pontuacaoA,
        int pontuacaoB,
        boolean finalizada,
        String vencedora,

        int pitchsA,
        int bugsA,
        int tracoesA,
        int investidoresA,
        int penalidadesA,

        int pitchsB,
        int bugsB,
        int tracoesB,
        int investidoresB,
        int penalidadesB
) {}

