package com.libraryManagement.demo.services.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.libraryManagement.demo.dtos.BookGetDTO;
import com.libraryManagement.demo.dtos.BookPostPatchDTO;
import com.libraryManagement.demo.dtos.FilterDTO;

import java.util.List;

public interface BookService {
    void save(BookPostPatchDTO bookPostPatchDTO);
    List<BookGetDTO> search(FilterDTO filterDTO);
}
