package br.edu.ifsul.cstsi.tds_guilherme.auth;

import br.edu.ifsul.cstsi.tds_guilherme.infra.jwt.TokenJwtDto;
import br.edu.ifsul.cstsi.tds_guilherme.infra.jwt.TokenService;
import br.edu.ifsul.cstsi.tds_guilherme.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJwtDto> login(@Valid @RequestBody AuthDto data) {
        var authDto = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var authentication = authenticationManager.authenticate(authDto);

        var tokenJwt = tokenService.generate((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJwtDto(tokenJwt));
    }
}
