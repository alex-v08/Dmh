package com.dmh.UserService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for new user creation")
public class NewUserResponse {

    @Schema(description = "Account ID created for the user", example = "1")
    private Long accountId;

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User ID", example = "1")
    private Long userId;
}