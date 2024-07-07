package com.libraryManagement.demo.mappers;

import com.libraryManagement.demo.dal.entities.Book;
import com.libraryManagement.demo.dtos.BookGetDTO;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookGetDTO toDTO(Book entity);
    List<BookGetDTO> toDTOs(List<Book> entities);
}
