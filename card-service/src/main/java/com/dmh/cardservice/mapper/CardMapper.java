package com.dmh.cardservice.mapper;


import com.dmh.cardservice.entity.Card;
import com.dmh.cardservice.entity.dto.CardDto;
import com.dmh.cardservice.entity.dto.CardRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.Mapping;


@Mapper(componentModel = "spring")
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    CardDto toCardDto(Card card);



    Card toCard(CardRequestDto cardRequestDto);

    Card toCard(CardDto cardDto);


}
