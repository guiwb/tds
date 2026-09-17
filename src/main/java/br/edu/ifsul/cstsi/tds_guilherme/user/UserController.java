package br.edu.ifsul.cstsi.tds_guilherme.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(UserDto::new).toList());
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new UserDto(user.get()));
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> create(@RequestBody CreateUserDto data) {
        var user = new User();
        user.setName(data.name());
        user.setEmail(data.email());
        user.setPassword(bCryptPasswordEncoder.encode(data.password()));

        user = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user));
    }

    @PutMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<UserDto> update(@RequestBody UpdateUserDto data) {
        var existentUser = userRepository.findById(data.id());

        if (existentUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = existentUser.get();

        user.setName(data.name());

        if (data.password() != null && !data.password().isBlank()) {
            user.setPassword(bCryptPasswordEncoder.encode(data.password()));
        }

        var updatedUser = userRepository.save(user);

        return ResponseEntity.ok(new UserDto(updatedUser));
    }
}
