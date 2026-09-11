package br.edu.ifsul.cstsi.tds_guilherme.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userRepository.findAll().stream().map(UserDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new UserDto(user.get()));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody CreateUserDto data) {
        var user = new User();
        user.setName(data.name());
        user.setEmail(data.email());

        user = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user));
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UpdateUserDto data) {
        var existentUser = userRepository.findById(data.id());

        if (existentUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = existentUser.get();

        user.setName(data.name());

        if (data.password() != null && !data.password().isBlank()) {
            user.setPassword(data.password());
        }

        var updatedUser = userRepository.save(user);

        return ResponseEntity.ok(new UserDto(updatedUser));
    }
}
