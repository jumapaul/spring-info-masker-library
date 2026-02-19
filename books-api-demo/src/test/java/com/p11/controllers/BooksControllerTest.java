package com.p11.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p11.models.ApiResponse;
import com.p11.models.BooksDto;
import com.p11.services.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BooksControllerTest {

    @Mock
    private BooksService booksService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BooksController booksController;

    private BooksDto sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDto = new BooksDto(1L, "Title", "Author", "email@test.com", "12345");
    }

    @Test
    void testCreateBook() throws JsonProcessingException {
        ApiResponse<BooksDto> response = ApiResponse.success(sampleDto, "Book created successfully");
        when(booksService.createBook(any(BooksDto.class)))
                .thenReturn(ResponseEntity.status(201).body(response));
        when(objectMapper.writeValueAsString(any())).thenReturn("json-string");

        ResponseEntity<ApiResponse<BooksDto>> result = booksController.createBook(sampleDto);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("Book created successfully", result.getBody().getMessage());
        verify(booksService).createBook(sampleDto);
    }

    @Test
    void testGetBooks() {
        Page<BooksDto> page = new PageImpl<>(List.of(sampleDto));
        ApiResponse<Page<BooksDto>> response = ApiResponse.success(page, "Books retrieved successfully");
        when(booksService.getBooks(0, 10)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ApiResponse<Page<BooksDto>>> result = booksController.getBooks(0, 10);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Books retrieved successfully", result.getBody().getMessage());
        assertEquals(1, result.getBody().getData().getTotalElements());
        verify(booksService).getBooks(0, 10);
    }

    @Test
    void testGetBookById() {
        ApiResponse<BooksDto> response = ApiResponse.success(sampleDto, "Book found");
        when(booksService.getBookById(1L)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ApiResponse<BooksDto>> result = booksController.getBookById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Book found", result.getBody().getMessage());
        verify(booksService).getBookById(1L);
    }

    @Test
    void testUpdateBook() {
        BooksDto updatedDto = new BooksDto(1L, "NewTitle", "NewAuthor", "new@test.com", "67890");
        ApiResponse<BooksDto> response = ApiResponse.success(updatedDto, "Book updated successfully");
        when(booksService.updateBook(eq(1L), any(BooksDto.class))).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ApiResponse<BooksDto>> result = booksController.updateBook(1L, updatedDto);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Book updated successfully", result.getBody().getMessage());
        assertEquals("NewTitle", result.getBody().getData().getTitle());
        verify(booksService).updateBook(1L, updatedDto);
    }

    @Test
    void testDeleteBookById() {
        ApiResponse<String> response = ApiResponse.success("Book deleted successfully");
        when(booksService.deleteBookById(1L)).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<ApiResponse<String>> result = booksController.deleteBookById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Book deleted successfully", result.getBody().getData());
        verify(booksService).deleteBookById(1L);
    }
}