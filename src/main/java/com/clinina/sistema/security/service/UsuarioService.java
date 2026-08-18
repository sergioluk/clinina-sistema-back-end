package com.clinina.sistema.security.service;

import com.clinina.sistema.model.entity.Funcionario;
import com.clinina.sistema.model.entity.Usuario;
import com.clinina.sistema.repository.FuncionarioRepository;
import com.clinina.sistema.repository.UsuarioRepository;
import com.clinina.sistema.security.LoginResponseDto;
import com.clinina.sistema.security.UsuarioCreateRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          FuncionarioRepository funcionarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponseDto criarUsuario(UsuarioCreateRequestDto dto) {

        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha())); // criptografa a senha
        usuario.setRole(dto.role());

        if (dto.funcionarioId() != null) {
            Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
            usuario.setFuncionario(funcionario);
        }

        Usuario salvo = usuarioRepository.save(usuario);

        return new LoginResponseDto("Usuario Criado", "Bearer", new LoginResponseDto.FuncionarioResumoDto(
                salvo.getFuncionario().getId(),
                salvo.getFuncionario().getNome()
        ));
    }
}
