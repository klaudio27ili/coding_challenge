package com.libraryManagement.demo.services.book;

import com.libraryManagement.demo.dtos.BookGetDTO;
import com.libraryManagement.demo.dtos.BookPostPatchDTO;

import java.util.List;

public interface BookService {
    void save(BookPostPatchDTO bookPostPatchDTO);
    List<BookGetDTO> search(List<String> tags);
}
