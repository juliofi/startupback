package com.example.startupback.startup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StartupRepository extends JpaRepository<Startup, Long> {
}
