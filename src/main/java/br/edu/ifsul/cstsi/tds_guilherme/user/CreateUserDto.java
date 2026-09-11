package br.edu.ifsul.cstsi.tds_guilherme.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

public record CreateUserDto(@NotNull @NotEmpty @NotBlank String name, @NotNull @Email @NotEmpty @NotBlank String email,
                            @NotNull @NotEmpty @NotBlank @Length(min = 8) String password) implements Serializable {
}