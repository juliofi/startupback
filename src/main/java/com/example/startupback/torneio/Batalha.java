package com.example.startupback.torneio;

import com.example.startupback.startup.Startup;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "batalhas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Batalha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Startup startupA;

    @ManyToOne
    private Startup startupB;

    private Integer pontuacaoA = 70;
    private Integer pontuacaoB = 70;

    private boolean finalizada = false;

    @ManyToOne
    private Startup vencedora;
}
