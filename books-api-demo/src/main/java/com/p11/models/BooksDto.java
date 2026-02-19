package com.p11.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BooksDto {
    private Long id;
    private String title;
    private String author;
    private String email;
    private String phoneNumber;
}
