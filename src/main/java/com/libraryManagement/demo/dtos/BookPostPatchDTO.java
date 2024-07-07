package com.libraryManagement.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookPostPatchDTO {
    private String isbn;
    private List<String> tags;
}
