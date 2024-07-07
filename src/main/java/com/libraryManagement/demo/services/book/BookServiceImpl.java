package com.libraryManagement.demo.services.book;

import com.libraryManagement.demo.dal.entities.Book;
import com.libraryManagement.demo.dal.entities.Tag;
import com.libraryManagement.demo.dal.repositories.BookRepository;
import com.libraryManagement.demo.dtos.BookGetDTO;
import com.libraryManagement.demo.dtos.BookPostPatchDTO;
import com.libraryManagement.demo.dtos.FilterDTO;
import com.libraryManagement.demo.exception.ApiException;
import com.libraryManagement.demo.exception.ErrorCode;
import com.libraryManagement.demo.exception.MessageKey;
import com.libraryManagement.demo.helpers.PatternConstants;
import com.libraryManagement.demo.mappers.BookMapper;
import com.libraryManagement.demo.mappers.rowMappers.BookRowMapper;
import com.libraryManagement.demo.services.tag.TagService;
import com.libraryManagement.demo.services.utils.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BookServiceImpl extends SpecificationService<Book> implements BookService{

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final TagService tagService;
    private final JdbcTemplate jdbcTemplate;

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
        Set<Tag> tags = tagService.findByNameOrSave(bookPostPatchDTO.getTags());
        if(existingBook.isPresent()) {
            Book dbBook = existingBook.get();
            dbBook.setTags(tags);
            bookRepository.save(dbBook);
        } else {
            bookRepository.save(new Book(bookPostPatchDTO.getIsbn(), tags));
        }
    }

    @Override
    public List<BookGetDTO> search(FilterDTO filterDTO) {
        if(filterDTO.getTags().isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, MessageKey.TAGS_CANNOT_BE_EMPTY);
        }
        Set<Tag> tags = tagService.findByNames(filterDTO.getTags());
        if(filterDTO.getTags().size() != tags.size()) {
            return Collections.emptyList();
        }
        return bookMapper.toDTOs(
                bookRepository.findAllByTagsIn(tags, (long)tags.size())
        );
    }

    private List<Book> getByTagsJdbc(Set<Tag> tags) {
        Set<UUID> tagIds = new HashSet<>();
        for (Tag tag : tags) {
            tagIds.add(tag.getId());
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < tagIds.size(); i++) {
            if (i > 0) {
                placeholders.append(", ");
            }
            placeholders.append("?");
        }

        String query = "SELECT b.id, b.isbn " +
                "FROM book b " +
                "JOIN book_tag bt ON b.id = bt.book_id " +
                "JOIN tag t ON bt.tag_id = t.id " +
                "WHERE t.id IN (" + placeholders + ") " +
                "GROUP BY b.id " +
                "HAVING COUNT(DISTINCT t.id) = ?";

        PreparedStatementSetter pss = preparedStatement -> {
            int i = 1;
            for (UUID tagId : tagIds) {
                preparedStatement.setObject(i++, tagId);
            }
            preparedStatement.setInt(i, tagIds.size());
        };

        return jdbcTemplate.query(query, pss, new BookRowMapper());
    }

    private boolean isIsbnValid(String isbn) {
        return isbn != null && isbn.length() == 13 && isNumeric(isbn);
    }

    private boolean isNumeric(String isbn) {
        Pattern pattern = Pattern.compile(PatternConstants.IS_NUMERIC);
        return pattern.matcher(isbn).matches();
    }

}
