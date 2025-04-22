package com.example.startupback.controller;

import com.example.startupback.startup.Startup;
import com.example.startupback.startup.StartupRepository;
import com.example.startupback.batalha.Batalha;
import com.example.startupback.batalha.BatalhaRepository;
import com.example.startupback.batalha.BatalhaResponseDTO;
import com.example.startupback.batalha.AtualizarBatalhaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



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

        if (!(startups.size() == 4 || startups.size() == 8)) {
            throw new RuntimeException("Número de startups inválido. Deve ser 4 ou 8.");
        }

        // Não reseta mais a pontuação aqui! 👇 Removido o trecho de primeiraRodada

        // Apaga batalhas anteriores
        batalhaRepository.deleteAll();

        // Embaralha as startups
        Collections.shuffle(startups);

        List<Batalha> batalhas = new ArrayList<>();

        for (int i = 0; i < startups.size(); i += 2) {
            Startup a = startups.get(i);
            Startup b = startups.get(i + 1);

            Batalha batalha = new Batalha();
            batalha.setStartupA(a);
            batalha.setStartupB(b);
            batalha.setPontuacaoA(a.getPontuacao()); // usa a pontuação atual da startup
            batalha.setPontuacaoB(b.getPontuacao());
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

    // NOVO ENDPOINT: Finalizar batalha por ID
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/batalha/{id}")
    public ResponseEntity<Void> finalizarBatalha(@PathVariable Long id, @RequestBody AtualizarBatalhaDTO dto) {
        Optional<Batalha> optional = batalhaRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Batalha batalha = optional.get();
        batalha.setPontuacaoA(dto.pontuacaoA());
        batalha.setPontuacaoB(dto.pontuacaoB());
        batalha.setFinalizada(dto.finalizada());

        // Atualiza os contadores de eventos para startupA
        Startup startupA = batalha.getStartupA();
        startupA.setContPitch(startupA.getContPitch() + dto.pitchsA());
        startupA.setContBug(startupA.getContBug() + dto.bugsA());
        startupA.setContTracao(startupA.getContTracao() + dto.tracoesA());
        startupA.setContInvestidor(startupA.getContInvestidor() + dto.investidoresA());
        startupA.setContPenalidade(startupA.getContPenalidade() + dto.penalidadesA());

        // Atualiza os contadores de eventos para startupB
        Startup startupB = batalha.getStartupB();
        startupB.setContPitch(startupB.getContPitch() + dto.pitchsB());
        startupB.setContBug(startupB.getContBug() + dto.bugsB());
        startupB.setContTracao(startupB.getContTracao() + dto.tracoesB());
        startupB.setContInvestidor(startupB.getContInvestidor() + dto.investidoresB());
        startupB.setContPenalidade(startupB.getContPenalidade() + dto.penalidadesB());

        // Atualiza pontuação da vencedora
        if (dto.vencedora() != null) {
            Optional<Startup> vencedoraOpt = startupRepository.findByNome(dto.vencedora());
            if (vencedoraOpt.isPresent()) {
                Startup vencedora = vencedoraOpt.get();
                vencedora.setPontuacao(vencedora.getPontuacao() + 30);
                startupRepository.save(vencedora);
                batalha.setVencedora(vencedora);
            }
        }

        // Salva as startups com eventos atualizados
        startupRepository.save(startupA);
        startupRepository.save(startupB);

        batalhaRepository.save(batalha);
        return ResponseEntity.ok().build();
    }



    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/batalhas")
    public List<BatalhaResponseDTO> listarBatalhas() {
        return batalhaRepository.findAll()
                .stream()
                .map(BatalhaResponseDTO::new)
                .toList();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/reset")
    public ResponseEntity<Void> resetarTorneio() {
        batalhaRepository.deleteAll();
        startupRepository.deleteAll();
        return ResponseEntity.ok().build(); // <- resposta 200 OK
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/proxima-fase")
    public List<BatalhaResponseDTO> avancarParaProximaFase() {
        List<Batalha> batalhasFinalizadas = batalhaRepository.findByFinalizadaTrue();

        // 1. Verifica se todas as batalhas foram finalizadas
        long totalBatalhas = batalhaRepository.count();
        if (batalhasFinalizadas.size() < totalBatalhas) {
            throw new RuntimeException("Ainda há batalhas não finalizadas.");
        }

        // 2. Pega os vencedores sem duplicações
        List<Startup> vencedoras = new ArrayList<>(
                batalhasFinalizadas.stream()
                        .map(Batalha::getVencedora)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        );

        // LOG para debug
        System.out.println("Vencedoras:");
        vencedoras.forEach(v -> System.out.println(v.getNome() + " (ID: " + v.getId() + ")"));

        // Se sobrou só uma startup, o torneio terminou — não há como formar novas batalhas
        // Se só sobrou uma startup, o torneio terminou
        if (vencedoras.size() == 1) {
            return new ArrayList<>();
        }

// ⚠️ Garantia extra: se houver só 1 batalha anterior e ela foi final, não cria mais nada
        if (totalBatalhas == 1 && vencedoras.size() == 2) {
            return new ArrayList<>();
        }


        // 3. Cria as novas batalhas (sem embaralhar)
        List<Batalha> novasBatalhas = new ArrayList<>();
        for (int i = 0; i < vencedoras.size(); i += 2) {
            Batalha nova = new Batalha();
            nova.setStartupA(vencedoras.get(i));
            nova.setStartupB(vencedoras.get(i + 1));
            nova.setPontuacaoA(vencedoras.get(i).getPontuacao());
            nova.setPontuacaoB(vencedoras.get(i + 1).getPontuacao());
            nova.setFinalizada(false);
            novasBatalhas.add(nova);
        }

        // 🧠 Adicione AQUI a impressão dos pares criados
        System.out.println("🔄 Criando novas batalhas:");
        for (Batalha b : novasBatalhas) {
            System.out.println("→ " + b.getStartupA().getNome() + " vs " + b.getStartupB().getNome());
        }


        // 4. Limpa batalhas anteriores
        batalhaRepository.deleteAll();

        // 5. Salva e retorna novas batalhas
        return batalhaRepository.saveAll(novasBatalhas)
                .stream()
                .map(BatalhaResponseDTO::new)
                .toList();
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/ranking")
    public List<Startup> getRankingFinal() {
        return startupRepository.findAll(Sort.by(Sort.Direction.DESC, "pontuacao"));
    }


}
