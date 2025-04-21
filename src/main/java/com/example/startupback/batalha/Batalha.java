package com.example.startupback.batalha;

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

    private Integer pontuacaoA;
    private Integer pontuacaoB;

    private boolean finalizada = false;

    @ManyToOne
    private Startup vencedora;
}
