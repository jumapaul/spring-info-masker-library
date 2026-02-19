package com.p11.services;

import com.p11.models.ApiResponse;
import com.p11.models.BooksDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface BooksService {
    BooksDto createBook(BooksDto booksDto);

    Page<BooksDto> getBooks(int page, int size);

    BooksDto getBookById(Long id);

    BooksDto updateBook(Long id, BooksDto booksDto);

    String deleteBookById(Long id);
}
