package com.libraryManagement.demo.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookGetDTO {
    private String isbn;
    private List<TagGetDTO> tags;
}
