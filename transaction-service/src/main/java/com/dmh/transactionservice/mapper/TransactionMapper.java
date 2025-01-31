package com.dmh.transactionservice.mapper;

import com.dmh.transactionservice.dto.TransactionRequest;
import com.dmh.transactionservice.dto.TransferenceRequest;
import com.dmh.transactionservice.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;


/**
 * Mapper interface for converting transaction requests into Transaction entities.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "dated", expression = "java(parseDateTime(request.getDated()))")
    @Mapping(target = "type", constant = "DEPOSIT")
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "amount", source = "request.amount")  // Mapping the amount
    @Mapping(target = "description", source = "request.description")  // Mapping the description
    Transaction toDepositTransaction(TransactionRequest request, Long accountId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "dated", expression = "java(parseDateTime(request.getDated()))")
    @Mapping(target = "type", constant = "TRANSFERENCE")
    @Mapping(target = "description", source = "request.description")  // Mapping the description
    @Mapping(target = "amount", source = "request.amount")  // Mapping the amount
    Transaction toTransferenceTransaction(TransferenceRequest request, Long accountId);

    default LocalDateTime parseDateTime(String date) {
        return LocalDateTime.parse(date);
    }
}

