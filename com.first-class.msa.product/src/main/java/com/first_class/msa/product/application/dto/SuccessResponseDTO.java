package com.first_class.msa.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDTO<T> {
    private Integer code;
    private String message;
    private T data;
}