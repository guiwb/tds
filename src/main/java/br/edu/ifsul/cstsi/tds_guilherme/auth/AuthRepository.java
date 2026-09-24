package br.edu.ifsul.cstsi.tds_guilherme.auth;

import br.edu.ifsul.cstsi.tds_guilherme.user.User;
import org.springframework.data.repository.Repository;

public interface AuthRepository extends Repository<User, Long> {
    User findByEmail(String email);
}
