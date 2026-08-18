package com.clinina.sistema.security.controller;

import com.clinina.sistema.model.entity.Usuario;
import com.clinina.sistema.repository.UsuarioRepository;
import com.clinina.sistema.security.LoginRequestDto;
import com.clinina.sistema.security.LoginResponseDto;
import com.clinina.sistema.security.UsuarioCreateRequestDto;
import com.clinina.sistema.security.jwt.JwtUtil;
import com.clinina.sistema.security.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtUtil.gerarToken(dto.email());

        LoginResponseDto.FuncionarioResumoDto funcionarioResumo = null;
        if (usuario.getFuncionario() != null) {
            funcionarioResumo = new LoginResponseDto.FuncionarioResumoDto(
                    usuario.getFuncionario().getId(),
                    usuario.getFuncionario().getNome()
            );
        }

        return new LoginResponseDto(token, "Bearer", funcionarioResumo);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<LoginResponseDto> criarUsuario(@RequestBody @Valid UsuarioCreateRequestDto dto) {
        LoginResponseDto response = usuarioService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
