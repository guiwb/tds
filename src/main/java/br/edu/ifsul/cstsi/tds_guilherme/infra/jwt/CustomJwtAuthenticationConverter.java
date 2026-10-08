package br.edu.ifsul.cstsi.tds_guilherme.infra.jwt;

import br.edu.ifsul.cstsi.tds_guilherme.auth.AuthRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final AuthRepository authRepository;

    public CustomJwtAuthenticationConverter(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        var user = authRepository.findByEmail(jwt.getSubject());

        if (user == null) {
            throw new BadCredentialsException("Usuário do token JWT não encontrado");
        }

        return new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());
    }

}