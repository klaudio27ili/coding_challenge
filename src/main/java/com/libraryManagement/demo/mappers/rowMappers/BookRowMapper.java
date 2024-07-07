package com.libraryManagement.demo.mappers.rowMappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryManagement.demo.dal.entities.Book;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BookRowMapper implements RowMapper<Book> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(UUID.fromString(rs.getString("id")));
        book.setIsbn(rs.getString("isbn"));
        return book;
    }
}