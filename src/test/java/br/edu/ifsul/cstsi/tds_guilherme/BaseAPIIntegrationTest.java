package br.edu.ifsul.cstsi.tds_guilherme;

import br.edu.ifsul.cstsi.tds_guilherme.auth.AuthService;
import br.edu.ifsul.cstsi.tds_guilherme.infra.jwt.TokenService;
import br.edu.ifsul.cstsi.tds_guilherme.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(classes = TdsGuilhermeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public abstract class BaseAPIIntegrationTest {
    protected static final String ADMIN_EMAIL = "admin@email.com";
    protected static final String USER_EMAIL = "user@email.com";

    @Autowired
    protected TestRestTemplate rest;
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenService tokenService;

    private String jwtToken = "";

    @BeforeEach
    public void setupTest() {
        loginAs(ADMIN_EMAIL);
    }

    protected void loginAs(String email) {
        var user = (User) authService.loadUserByUsername(email);
        assertNotNull(user);

        jwtToken = tokenService.generate(user);
        assertNotNull(jwtToken);
    }

    protected HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    protected <T> ResponseEntity<T> post(String url, Object body, Class<T> responseType) {
        return rest.exchange(url, POST, new HttpEntity<>(body, getHeaders()), responseType);
    }

    protected <T> ResponseEntity<T> put(String url, Object body, Class<T> responseType) {
        return rest.exchange(url, PUT, new HttpEntity<>(body, getHeaders()), responseType);
    }

    protected <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return rest.exchange(url, GET, new HttpEntity<>(getHeaders()), responseType);
    }

    protected <T> ResponseEntity<T> delete(String url, Class<T> responseType) {
        return rest.exchange(url, DELETE, new HttpEntity<>(getHeaders()), responseType);
    }
}
