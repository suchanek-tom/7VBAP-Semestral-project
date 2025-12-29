package com.example.library.repository;

import com.example.library.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("SELECT a FROM Author a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Author> findByName(@Param("name") String name, Pageable pageable);

    Page<Author> findByNationality(String nationality, Pageable pageable);
}
