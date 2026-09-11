package br.edu.ifsul.cstsi.tds_guilherme.serie;

import br.edu.ifsul.cstsi.tds_guilherme.session.Session;
import jakarta.persistence.*;

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
