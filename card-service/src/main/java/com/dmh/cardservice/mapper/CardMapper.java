package com.dmh.cardservice.mapper;

import com.dmh.cardservice.entity.Card;
import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.dto.CardRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    // Método para mapear de Card a CardDto
    CardDto toCardDto(Card card);

    // Método para mapear de CardRequestDto a Card
    Card toCard(CardRequestDto cardRequestDto);

    // Método para mapear de CardDto a Card
    Card toCard(CardDto cardDto);
}
