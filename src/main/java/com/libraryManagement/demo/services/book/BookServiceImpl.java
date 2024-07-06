package com.libraryManagement.demo.services.book;

import com.libraryManagement.demo.dal.entities.Book;
import com.libraryManagement.demo.dal.repositories.BookRepository;
import com.libraryManagement.demo.dtos.BookGetDTO;
import com.libraryManagement.demo.dtos.BookPostPatchDTO;
import com.libraryManagement.demo.dtos.FilterDTO;
import com.libraryManagement.demo.exception.ApiException;
import com.libraryManagement.demo.exception.ErrorCode;
import com.libraryManagement.demo.exception.MessageKey;
import com.libraryManagement.demo.mappers.BookMapper;
import com.libraryManagement.demo.services.utils.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BookServiceImpl extends SpecificationService<Book> implements BookService{

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    @Override
    public void save(BookPostPatchDTO bookPostPatchDTO) {
        String isbn = bookPostPatchDTO.getIsbn();

        if(!isIsbnValid(isbn)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, MessageKey.ISBN_PROVIDED_IS_INVALID, isbn);
        }

        if(bookPostPatchDTO.getTags().size() == 0) {
            throw new ApiException(ErrorCode.BAD_REQUEST, MessageKey.TAGS_CANNOT_BE_EMPTY);
        }

        Optional<Book> existingBook = bookRepository.findByIsbn(isbn);
        if(existingBook.isPresent()) {
            Book dbBook = existingBook.get();
            dbBook.setTags(bookPostPatchDTO.getTags());
            bookRepository.save(dbBook);
        } else {
            bookRepository.save(bookMapper.toEntity(bookPostPatchDTO));
        }
    }

    @Override
    public List<BookGetDTO> search(FilterDTO filterDTO) {
        List<String> tags = filterDTO.getTags();
        if(tags.isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, MessageKey.TAGS_CANNOT_BE_EMPTY);
        }
        return bookMapper.toDTOs(
                bookRepository.findAll(jsonArrayContains(tags))
        );
    }

    private boolean isIsbnValid(String isbn) {
        return isbn != null && isbn.length() == 13 && isNumeric(isbn);
    }

    private boolean isNumeric(String isbn) {
        //todo: add it to helpers, constants
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(isbn).matches();
    }
}
