package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.cliente.response.ClienteCompletoResponseDto;
import com.clinina.sistema.dto.cliente.request.ClienteCreateRequestDto;
import com.clinina.sistema.dto.cliente.response.ClienteBuscaResponseDto;
import com.clinina.sistema.dto.cliente.response.ClientePdvResponseDto;
import com.clinina.sistema.model.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
        ClienteEnderecoMapper.class,
        ClienteContatoMapper.class,
        ClienteMarcacaoMapper.class,
        AnimalMapper.class
    }
)
public interface ClienteMapper {

    ClienteBuscaResponseDto toClienteListarResponseDto(Cliente cliente);

    Cliente toCliente(ClienteCreateRequestDto dto);

    ClienteCompletoResponseDto toDtoCompleto(Cliente salvo);

    ClienteBuscaResponseDto toBuscaResponseDto(Cliente cliente);

    @Mapping(target = "saldoDevedor", ignore = true)
    @Mapping(target = "animais", source = "animais")
    @Mapping(target = "marcacoes", source = "marcacoes")
    ClientePdvResponseDto toPdvResponseDto(Cliente cliente);
}
