package br.edu.ifsul.cstsi.tds_guilherme.workout;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
}