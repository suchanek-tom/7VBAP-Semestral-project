package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private final BookRepository repo;

    public BookController(BookRepository repo) {
        this.repo = repo;
    }

    // GET all books
    @GetMapping
    public List<Book> getAll() {
        return repo.findAll();
    }

    // GET book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getOne(@PathVariable Integer id) {
        Book book = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        return ResponseEntity.ok(book);
    }

    // POST
    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book savedBook = repo.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Integer id, @RequestBody Book updated) {
        Book book = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setContent(updated.getContent());
        book.setPublicationYear(updated.getPublicationYear());
        book.setIsbn(updated.getIsbn());
        book.setAvailable(updated.isAvailable());

        Book savedBook = repo.save(book);
        return ResponseEntity.ok(savedBook);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
