package br.edu.ifsul.cstsi.tds_guilherme.serie;

import br.edu.ifsul.cstsi.tds_guilherme.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SerieRepository extends JpaRepository<Workout, Long> {
}