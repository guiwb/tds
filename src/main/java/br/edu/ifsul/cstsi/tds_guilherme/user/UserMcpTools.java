package br.edu.ifsul.cstsi.tds_guilherme.user;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMcpTools {

    private final UserRepository userRepository;

    public UserMcpTools(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @McpTool(description = "Lista todos os usuários cadastrados, com nome e email")
    public List<UserDto> listAll() {
        return userRepository.findAll().stream().map(UserDto::new).toList();
    }

    @McpTool(description = "Busca usuários cujo o nome contém o texto informado")
    public List<UserDto> listUserByName(
            @McpToolParam(description = "Texto que deve conter no nome do usuário, ex: 'ui' para Guilherme", required = true) String name) {
        return userRepository.findByNameContainingIgnoreCase(name).stream().map(UserDto::new).toList();
    }
}
