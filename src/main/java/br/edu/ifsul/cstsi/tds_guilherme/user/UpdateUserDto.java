package br.edu.ifsul.cstsi.tds_guilherme.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UpdateUserDto(
        @NotNull(message = "Você precisa informar um ID")
        Long id,

        @NotBlank(message = "O nome não pode ser nulo ou vazio")
        String name,

        @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$", message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um númeral, um caractere especial e um total de 8 caracteres.")
        String password
) implements Serializable {
}