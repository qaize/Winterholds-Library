package com.example.winterhold.validation;

import com.example.winterhold.dao.BookRepository;
import com.example.winterhold.entity.Book;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BookQuantityCheckerValidator implements ConstraintValidator<BookQuantityChecker, Object> {

    private String book;
    private String quantity;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void initialize(BookQuantityChecker constraintAnnotation) {
        this.book = constraintAnnotation.book();
        this.quantity = constraintAnnotation.quantity();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        String books = new BeanWrapperImpl(o).getPropertyValue(book).toString();
        int quantitites = Integer.valueOf(new BeanWrapperImpl(o).getPropertyValue(quantity).toString());

        Optional<Book> result = bookRepository.findById(books);
        Book data = result.isPresent() ? result.get() : null;

        if (data.getInBorrow() > quantitites) {
            return false;
        }

        if (data.getInBorrow() == quantitites) {
            data.setIsBorrowed(true);
        }


        return true;
    }
}
