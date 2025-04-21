package com.example.startupback.batalha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



public interface BatalhaRepository extends JpaRepository<Batalha, Long> {
    void deleteAllByStartupA_IdOrStartupB_Id(Long idA, Long idB);

    @Modifying
    @Transactional
    @Query("DELETE FROM Batalha b WHERE b.startupA.id = :id OR b.startupB.id = :id")
    void deleteByStartupId(@Param("id") Long id);
    List<Batalha> findByFinalizadaTrue();


}