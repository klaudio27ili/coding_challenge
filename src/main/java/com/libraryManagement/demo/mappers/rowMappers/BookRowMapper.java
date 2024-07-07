package com.libraryManagement.demo.mappers.rowMappers;

import com.libraryManagement.demo.dal.entities.Book;
import com.libraryManagement.demo.dal.entities.Tag;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BookRowMapper implements RowMapper<Book> {
    private final Map<UUID, Book> bookMap = new HashMap<>();

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID bookId = UUID.fromString(rs.getString("id"));
        String isbn = rs.getString("isbn");
        String tagName = rs.getString("name");

        Book book = bookMap.computeIfAbsent(bookId, id -> {
            Book newBook = new Book();
            newBook.setId(id);
            newBook.setIsbn(isbn);
            newBook.setTags(new HashSet<>());
            return newBook;
        });

        Tag tag = new Tag(tagName);
        book.getTags().add(tag);

        return null;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(bookMap.values());
    }
}