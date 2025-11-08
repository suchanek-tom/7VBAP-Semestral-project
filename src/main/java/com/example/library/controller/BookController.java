package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        try {
            List<Book> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve books");
        }
    }

    // get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getOne(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid book ID");
            }

            Book book = bookService.getBookById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
            return ResponseEntity.ok(book);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve book");
        }
    }

    // Create a new book
    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        try {
            if (book == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book data is required");
            }

            Book savedBook = bookService.createBook(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid book data: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create book");
        }
    }

    // Create multiple books
    @PostMapping("/bulk")
    public ResponseEntity<List<Book>> createMany(@Valid @RequestBody List<Book> books) {
        try {
            if (books == null || books.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book list cannot be empty");
            }

            if (books.size() > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 100 books can be created at once");
            }

            List<Book> savedBooks = bookService.createMultipleBooks(books);
            return new ResponseEntity<>(savedBooks, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid book data: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create books");
        }
    }

    // Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Integer id, @Valid @RequestBody Book updated) {
        try {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid book ID");
            }

            if (updated == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book data is required");
            }

            Book savedBook = bookService.updateBook(id, updated);
            return ResponseEntity.ok(savedBook);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update book");
        }
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid book ID");
            }

            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete book");
        }
    }
}
