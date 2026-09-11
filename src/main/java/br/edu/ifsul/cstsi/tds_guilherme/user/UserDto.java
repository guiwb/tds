package br.edu.ifsul.cstsi.tds_guilherme.user;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(String name, String email) implements Serializable {
    public UserDto(User user) {
        this(user.getName(), user.getEmail());
    }
}