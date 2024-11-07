package com.dmh.cardservice.mapper;

import com.dmh.cardservice.entity.Card;
import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.dto.CardRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    // Método para mapear de Card a CardDto
    CardDto toCardDto(Card card);

    // Método para mapear de CardRequestDto a Card
    Card toCard(CardRequestDto cardRequestDto);


    // Método para mapear de CardDto a Card
    Card toCard(CardDto cardDto);

   @Mapping(target = "id", ignore = true)
    void updateCardFromDto(CardDto cardDto, @MappingTarget Card card);

}
