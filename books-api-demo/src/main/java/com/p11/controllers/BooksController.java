package com.p11.controllers;

import com.p11.models.ApiResponse;
import com.p11.models.BooksDto;
import com.p11.services.BooksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BooksController {

    private final BooksService booksService;

    @PostMapping
    public ResponseEntity<ApiResponse<BooksDto>> createBook(@Valid @RequestBody BooksDto booksDto) {
        log.info("Create book: {}", booksDto);

        return ResponseEntity.ok(apiResponse(booksService.createBook(booksDto), "Book created"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BooksDto>>> getBooks(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        log.info("Fetch books, page={}, size={}", page, size);
        return ResponseEntity.ok(apiResponse(booksService.getBooks(page, size), "Books retrieved"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BooksDto>> getBookById(@PathVariable("id") Long id) {
        log.info("Fetch book with id: {}", id);
        return ResponseEntity.ok(apiResponse(booksService.getBookById(id), "Book retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BooksDto>> updateBook(
            @PathVariable("id") Long id,
            @Valid @RequestBody BooksDto booksDto
    ) {
        log.info("Updating book with id: {}: {}", id, booksDto);
        return ResponseEntity.ok(apiResponse(booksService.updateBook(id, booksDto), "Book updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBookById(@PathVariable("id") Long id) {
        log.info("Deleting book with id: {}", id);
        return ResponseEntity.ok(apiResponse(null, booksService.deleteBookById(id)));
    }

    private <T> ApiResponse<T> apiResponse(T data, String message) {
        return new ApiResponse<>(
                true,
                message,
                data,
                HttpStatus.OK.value()
        );
    }
}