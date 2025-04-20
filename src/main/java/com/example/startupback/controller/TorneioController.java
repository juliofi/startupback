package com.example.startupback.controller;

import com.example.startupback.startup.Startup;
import com.example.startupback.startup.StartupRepository;
import com.example.startupback.torneio.Batalha;
import com.example.startupback.torneio.BatalhaRepository;
import com.example.startupback.torneio.BatalhaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/torneio")
public class TorneioController {

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private BatalhaRepository batalhaRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/iniciar")
    public List<BatalhaResponseDTO> iniciarTorneio() {
        List<Startup> startups = startupRepository.findAll();

        if (startups.size() < 4 || startups.size() > 8 || startups.size() % 2 != 0) {
            throw new RuntimeException("Número de startups inválido. Deve ser 4, 6 ou 8.");
        }

        // Apaga batalhas anteriores
        batalhaRepository.deleteAll();

        // Embaralha as startups
        Collections.shuffle(startups);

        List<Batalha> batalhas = new ArrayList<>();

        for (int i = 0; i < startups.size(); i += 2) {
            Batalha batalha = new Batalha();
            batalha.setStartupA(startups.get(i));
            batalha.setStartupB(startups.get(i + 1));
            batalha.setPontuacaoA(70);
            batalha.setPontuacaoB(70);
            batalha.setFinalizada(false);
            batalhas.add(batalha);
        }

        return batalhaRepository.saveAll(batalhas)
                .stream()
                .map(BatalhaResponseDTO::new)
                .toList();

    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/batalhas-da-startup/{id}")
    public void deletarBatalhasDaStartup(@PathVariable Long id) {
        batalhaRepository.deleteByStartupId(id);
    }


}
