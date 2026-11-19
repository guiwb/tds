package br.edu.ifsul.cstsi.tds_guilherme.user;

import br.edu.ifsul.cstsi.tds_guilherme.BaseAPIIntegrationTest;
import br.edu.ifsul.cstsi.tds_guilherme.auth.AuthRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerIntegrationTest extends BaseAPIIntegrationTest {
    private static final String URL = "/api/v1/users";
    private static final String PASSWORD = "Senha@123";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final List<Long> createdIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        userRepository.deleteAllById(createdIds);
        createdIds.clear();
    }

    private ResponseEntity<UserDto> getUser(String url) {
        return get(url, UserDto.class);
    }

    private ResponseEntity<List<UserDto>> getUsersList(String url) {
        return rest.exchange(url, HttpMethod.GET, new HttpEntity<>(getHeaders()), new ParameterizedTypeReference<>() {
        });
    }

    private String uniqueEmail() {
        return "test-" + UUID.randomUUID() + "@email.com";
    }

    private User createUser(String name, String email) {
        var response = post(URL, new CreateUserDto(name, email, PASSWORD), UserDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        var user = authRepository.findByEmail(email);
        assertNotNull(user);
        createdIds.add(user.getId());
        return user;
    }

    @Test
    void findAllExpectsListContainingSeedUsers() {
        var response = getUsersList(URL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var emails = response.getBody().stream().map(UserDto::email).toList();
        assertTrue(emails.contains(ADMIN_EMAIL));
        assertTrue(emails.contains(USER_EMAIL));
    }

    @Test
    void findByIdExpectsUserAndNotFoundForMissingId() {
        var response = getUser(URL + "/7");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin", response.getBody().name());
        assertEquals(ADMIN_EMAIL, response.getBody().email());

        assertEquals(HttpStatus.NOT_FOUND, getUser(URL + "/100000").getStatusCode());
    }

    @Test
    void createExpects201CreatedAndPersistsEncodedPassword() {
        var email = uniqueEmail();

        var response = post(URL, new CreateUserDto("Test", email, PASSWORD), UserDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test", response.getBody().name());
        assertEquals(email, response.getBody().email());

        var user = authRepository.findByEmail(email);
        assertNotNull(user);
        createdIds.add(user.getId());
        assertNotEquals(PASSWORD, user.getPassword());
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, user.getPassword()));
    }

    @Test
    void createWithInvalidDataExpects400BadRequest() {
        var response = post(URL, new CreateUserDto("", "invalid-email", "123"), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Erro de validação"));
    }

    @Test
    void createWithDuplicatedEmailExpects409Conflict() {
        var response = post(URL, new CreateUserDto("Dup", ADMIN_EMAIL, PASSWORD), String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void updateExpects200OkAndChangesNameAndPassword() {
        var email = uniqueEmail();
        var user = createUser("Test", email);
        var newPassword = "Nova@1234";

        var response = put(URL, new UpdateUserDto(user.getId(), "Test Modified", newPassword), UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Modified", response.getBody().name());
        assertEquals(email, response.getBody().email());

        var updated = authRepository.findByEmail(email);
        assertTrue(bCryptPasswordEncoder.matches(newPassword, updated.getPassword()));
    }

    @Test
    void updateWithoutPasswordKeepsCurrentPassword() {
        var email = uniqueEmail();
        var user = createUser("Test", email);

        var response = put(URL, new UpdateUserDto(user.getId(), "Test Modified", null), UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var updated = authRepository.findByEmail(email);
        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, updated.getPassword()));
    }

    @Test
    void updateWithMissingIdExpects404NotFound() {
        var response = put(URL, new UpdateUserDto(100000L, "Nobody", null), UserDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void nonAdminUserExpects403Forbidden() {
        loginAs(USER_EMAIL);

        assertEquals(HttpStatus.FORBIDDEN, get(URL, String.class).getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, get(URL + "/7", String.class).getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, post(URL, new CreateUserDto("Test", uniqueEmail(), PASSWORD), String.class).getStatusCode());
    }

    @Test
    void requestWithoutTokenExpects401Unauthorized() {
        var response = rest.getForEntity(URL, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
