package com.p11.services.impl;

import com.p11.entities.Book;
import com.p11.exception.ResourceNotFoundException;
import com.p11.models.BooksDto;
import com.p11.repositories.BooksRepository;
import com.p11.services.BooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;

    @Override
    public BooksDto createBook(BooksDto booksDto) {
        Book entity = dtoToEntity.apply(booksDto);
        Book saved = booksRepository.save(entity);
        return entityToDto(saved);
    }

    @Override
    public Page<BooksDto> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Book> booksPage = booksRepository.findAll(pageable);

        // Convert entities to DTOs
        return booksPage.map(this::entityToDto);
    }


    @Override
    public BooksDto getBookById(Long id) {
        Book book = booksRepository.findById(id).orElseThrow(() ->
                new ResolutionException("Book not found")
        );

        return entityToDto(book);
    }

    @Override
    public BooksDto updateBook(Long id, BooksDto booksDto) {
        Book book = booksRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found")
        );
        book.setTitle(booksDto.getTitle());
        book.setAuthor(booksDto.getAuthor());
        book.setEmail(booksDto.getEmail());
        book.setPhoneNumber(booksDto.getPhoneNumber());

        booksRepository.save(book);
        return entityToDto(book);
    }

    @Override
    public String deleteBookById(Long id) {
        Book book = booksRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found")
        );

        booksRepository.delete(book);
        return "Book deleted successfully";
    }

    private BooksDto entityToDto(Book entity) {
        return new BooksDto(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getEmail(),
                entity.getPhoneNumber()
        );
    }

    private final Function<BooksDto, Book> dtoToEntity = dto -> {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setEmail(dto.getEmail());
        book.setPhoneNumber(dto.getPhoneNumber());
        return book;
    };
}