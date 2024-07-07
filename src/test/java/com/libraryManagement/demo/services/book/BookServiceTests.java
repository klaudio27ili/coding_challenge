package com.libraryManagement.demo.services.book;

import com.libraryManagement.demo.dal.entities.Book;
import com.libraryManagement.demo.dal.entities.Tag;
import com.libraryManagement.demo.dal.repositories.BookRepository;
import com.libraryManagement.demo.dtos.BookGetDTO;
import com.libraryManagement.demo.dtos.BookPostPatchDTO;
import com.libraryManagement.demo.dtos.FilterDTO;
import com.libraryManagement.demo.exception.ApiException;
import com.libraryManagement.demo.services.tag.TagService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookServiceTests {
    @Autowired
    private BookService bookService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenInvalidLengthISBN_thenThrowException() {
        Assertions.assertThrows(ApiException.class, () -> {
            bookService.save(new BookPostPatchDTO("invalid isbn", null));
        });
    }

    @Test
    public void whenNonNumericISBN_thenThrowException() {
        Assertions.assertThrows(ApiException.class, () -> {
            bookService.save(new BookPostPatchDTO("123456789012a", null));
        });
    }

    @Test
    public void whenEmptyTags_thenThrowException() {
        Assertions.assertThrows(ApiException.class, () -> {
            bookService.save(new BookPostPatchDTO("1234567890121", Collections.emptyList()));
        });
    }

    @Test
    public void successfulSave() {
        Tag existingTag = tagService.findByNameOrSave(List.of("new tag")).stream().toList().get(0);
        String isbn = "0123456789115";
        bookService.save(new BookPostPatchDTO(isbn, List.of("new tag")));
        Optional<Book> savedBookOptional = bookRepository.findByIsbn(isbn);
        Assertions.assertTrue(savedBookOptional.isPresent());
        Assertions.assertEquals(savedBookOptional.get().getTags().size(), 1);
        Assertions.assertEquals(savedBookOptional.get().getTags().stream().toList().get(0).getId(), existingTag.getId());
        Assertions.assertEquals(savedBookOptional.get().getIsbn(), isbn);
    }

    @Test
    public void whenSearchWithEmptyTags_thenThrowException() {
        Assertions.assertThrows(ApiException.class, () -> {
            bookService.search(new FilterDTO(Collections.emptyList()));
        });
    }

    @Test
    public void whenSearchWithNonExistingTag_thenReturnEmptyList() {
        List<BookGetDTO> books = bookService.search(new FilterDTO(List.of("non existing tag")));
        Assertions.assertEquals(books.size(), 0);
    }

    @Test
    public void testSuccessfulSearch() {
        String isbn = "1111111111111";
        List<String> tags = List.of("tag1", "tag2");
        bookService.save(new BookPostPatchDTO(isbn, tags));
        Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
        Assertions.assertTrue(bookOptional.isPresent());
        Book book = bookOptional.get();
        Assertions.assertEquals(book.getIsbn(), isbn);
        Assertions.assertEquals(book.getTags().size(), 2);
        Assertions.assertTrue(tags.contains(book.getTags().stream().toList().get(0).getName()));
        Assertions.assertTrue(tags.contains(book.getTags().stream().toList().get(1).getName()));
    }

    @Test
    public void whenSavingExistingBook_thenUpdateTags() {
        String isbn = "1111111111112";
        bookService.save(new BookPostPatchDTO(isbn, List.of("tag1", "tag2")));
        bookService.save(new BookPostPatchDTO(isbn, List.of("tag3")));
        Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
        Assertions.assertTrue(bookOptional.isPresent());
        Book book = bookOptional.get();
        Assertions.assertEquals(book.getIsbn(), isbn);
        Assertions.assertEquals(book.getTags().size(), 1);
        Assertions.assertEquals(book.getTags().stream().toList().get(0).getName(), "tag3");
    }
}
