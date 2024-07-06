package com.libraryManagement.demo.services;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageSource messageSource;
    public String get(String key, Object[] tokens) {return messageSource.getMessage(key, tokens, Locale.ENGLISH);}
}
