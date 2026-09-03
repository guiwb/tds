package br.edu.ifsul.cstsi.tds_guilherme.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "series")
public class Serie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer repetitions;
    private Integer distance;
    private Integer total_time;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id", nullable = false)
    private Session session;
}
