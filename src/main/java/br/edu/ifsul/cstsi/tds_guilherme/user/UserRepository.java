package br.edu.ifsul.cstsi.tds_guilherme.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}