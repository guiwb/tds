package br.edu.ifsul.cstsi.tds_guilherme.session;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}