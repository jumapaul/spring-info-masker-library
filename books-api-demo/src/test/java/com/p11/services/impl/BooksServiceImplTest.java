package com.p11.services.impl;

import com.p11.entities.Book;
import com.p11.models.ApiResponse;
import com.p11.models.BooksDto;
import com.p11.repositories.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BooksServiceImplTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BooksServiceImpl booksService;

    private BooksDto sampleDto;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDto = new BooksDto(1L, "Title", "Author", "email@test.com", "12345");
        sampleBook = new Book();
        sampleBook.setId(1L);
        sampleBook.setTitle("Title");
        sampleBook.setAuthor("Author");
        sampleBook.setEmail("email@test.com");
        sampleBook.setPhoneNumber("12345");
    }

    @Test
    void testCreateBook() {
        when(booksRepository.save(any(Book.class))).thenReturn(sampleBook);

        ResponseEntity<ApiResponse<BooksDto>> response = booksService.createBook(sampleDto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Book created successfully", response.getBody().getMessage());
        assertEquals(sampleDto.getTitle(), response.getBody().getData().getTitle());

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(booksRepository).save(captor.capture());
        assertEquals("Title", captor.getValue().getTitle());
    }

    @Test
    void testGetBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(booksRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        ResponseEntity<ApiResponse<Page<BooksDto>>> response = booksService.getBooks(0, 10);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Books retrieved successfully", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().getTotalElements());
    }

    @Test
    void testGetBookById_Found() {
        when(booksRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        ResponseEntity<ApiResponse<BooksDto>> response = booksService.getBookById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Title", response.getBody().getData().getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<BooksDto>> response = booksService.getBookById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Book not found", response.getBody().getMessage());
    }

    @Test
    void testUpdateBook_Found() {
        when(booksRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(booksRepository.save(any(Book.class))).thenReturn(sampleBook);

        BooksDto updatedDto = new BooksDto(1L, "NewTitle", "NewAuthor", "new@test.com", "67890");
        ResponseEntity<ApiResponse<BooksDto>> response = booksService.updateBook(1L, updatedDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Book updated successfully", response.getBody().getMessage());
        assertEquals("NewTitle", response.getBody().getData().getTitle());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(booksRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<BooksDto>> response = booksService.updateBook(1L, sampleDto);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Book not found", response.getBody().getMessage());
    }

    @Test
    void testDeleteBookById_Found() {
        when(booksRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<ApiResponse<String>> response = booksService.deleteBookById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Book deleted successfully", response.getBody().getData());
        verify(booksRepository).deleteById(1L);
    }

    @Test
    void testDeleteBookById_NotFound() {
        when(booksRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<ApiResponse<String>> response = booksService.deleteBookById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Book not found", response.getBody().getMessage());
        verify(booksRepository, never()).deleteById(anyLong());
    }
}