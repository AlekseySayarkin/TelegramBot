package com.telegram.bot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ExceptionResponseDTO {
    private List<String> errors;
}
