package com.libraryManagement.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageKey {
    ISBN_PROVIDED_IS_INVALID("isbn_provided_is_invalid"),
    TAGS_CANNOT_BE_EMPTY("tags_cannot_be_empty");
    private final String key;
}
