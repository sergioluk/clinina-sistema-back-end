package com.clinina.sistema.mapper;

import com.clinina.sistema.dto.animal.response.AnimalBuscaClienteResponseDto;
import com.clinina.sistema.dto.animal.response.AnimalCompletoResponseDto;
import com.clinina.sistema.dto.animal.request.AnimalCreateRequestDto;
import com.clinina.sistema.dto.animal.response.AnimalPdvResponseDto;
import com.clinina.sistema.model.entity.Animal;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    Animal toEntity(AnimalCreateRequestDto dto);

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "marcacoes", source = "marcacoes")
    AnimalCompletoResponseDto toResponseDto(Animal animal);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AnimalCreateRequestDto dto, @MappingTarget Animal animal);

    AnimalBuscaClienteResponseDto toBuscaClienteResponseDto(Animal animal);

    @Mapping(target = "idade", expression = "java(formatarIdade(animal.getNascimento()))")
    AnimalPdvResponseDto toPdvResponseDto(Animal animal);

    default String formatarIdade(LocalDate nascimento) {
        if (nascimento == null) {
            return null;
        }

        Period periodo = Period.between(nascimento, LocalDate.now());

        return periodo.getYears() + " anos e "
                + periodo.getMonths() + " meses e "
                + periodo.getDays() + " dias";
    }

}
