package com.libraryManagement.demo.mappers.rowMappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
        try {
            book.setTags(objectMapper.readValue(rs.getObject("tags").toString(), new TypeReference<>() {}));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
}