package br.edu.ifsul.cstsi.tds_guilherme.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UpdateUserDto(@NotNull Long id, @NotNull @NotEmpty @NotBlank String name,
                            @Length(min = 8) String password) implements Serializable {
}