package br.edu.ifsul.cstsi.tds_guilherme.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "sessions")
public class Session {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer rest_time;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER)
    private Collection<Serie> series;

    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "id", nullable = false)
    private Workout workout;
}
