package com.example.startupback.controller;


import com.example.startupback.startup.Startup;
import com.example.startupback.startup.StartupRepository;
import com.example.startupback.startup.StartupRequestDTO;
import com.example.startupback.startup.StartupResponseDTO;
import com.example.startupback.torneio.BatalhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


}
