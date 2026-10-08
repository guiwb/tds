package br.edu.ifsul.cstsi.tds_guilherme.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

public record CreateUserDto(
        @NotBlank(message = "O nome não pode ser nulo ou vazio")
        @Size(min = 2, max = 50, message = "O nome deve ter no mínimo 2 caracteres e no máximo de 200")
        String name,

        @NotBlank(message = "O e-mail não pode ser nulo ou vazio")
        @Email(message = "O e-mail deve ser válido")
        String email,

        @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$", message = "A senha deve conter ao menos uma letra maiúscula, uma letra minúscula, um númeral, um caractere especial e um total de 8 caracteres.")
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        String password
) implements Serializable {
}