package com.p11.models;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Integer code;
}
