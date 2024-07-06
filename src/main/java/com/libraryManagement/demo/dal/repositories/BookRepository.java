package com.libraryManagement.demo.dal.repositories;

import com.libraryManagement.demo.dal.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByIsbn(String isbn);

    @Query(value = "SELECT * from Book b WHERE b.tags @> cast(:tags AS jsonb)", nativeQuery = true)
    List<Book> findByTags(@Param("tags") String tags);
}
