package com.example.startupback.controller;


import com.example.startupback.startup.*;
import com.example.startupback.batalha.BatalhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("startup")
public class StartupController {

    @Autowired
    private StartupRepository repository;

    @Autowired
    private BatalhaRepository batalhaRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public void saveStartup(@RequestBody StartupRequestDTO data){
        Startup startupData = new Startup(data);
        repository.save(startupData);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<StartupResponseDTO> getAll(){

        List<StartupResponseDTO> startupList = repository.findAll().stream().map(StartupResponseDTO::new).toList();

        return startupList;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteStartup(@PathVariable Long id) {
        // Limpa todas as batalhas antes
        batalhaRepository.deleteAll();

        // Agora sim pode deletar a startup
        repository.deleteById(id);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarStartup(@PathVariable Long id, @RequestBody AtualizarStartupDTO dto) {
        Optional<Startup> optional = repository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Startup startup = optional.get();

        startup.setPontuacao(dto.pontuacao());
        startup.setContPitch(startup.getContPitch() + dto.contPitch());
        startup.setContBug(startup.getContBug() + dto.contBug());
        startup.setContTracao(startup.getContTracao() + dto.contTracao());
        startup.setContInvestidor(startup.getContInvestidor() + dto.contInvestidor());
        startup.setContPenalidade(startup.getContPenalidade() + dto.contPenalidade());


        repository.save(startup);
        return ResponseEntity.ok().build();
    }






}
