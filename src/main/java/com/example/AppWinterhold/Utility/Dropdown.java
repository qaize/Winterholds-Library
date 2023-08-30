package com.example.AppWinterhold.Utility;

import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Book.BookIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Dropdown.DropdownDto;

import java.util.ArrayList;
import java.util.List;

public class Dropdown {

    public static List<DropdownDto> dropdownAuthor(List<AuthorIndexDto> drop) {
        List<DropdownDto> data = new ArrayList<>();

        for (AuthorIndexDto value : drop
        ) {
            data.add(new DropdownDto(value.getId().toString(), value.getFullname()));
        }
        return data;
    }

    public static List<DropdownDto> dropdownCustomer(List<CustomerIndexDto> drop) {
        List<DropdownDto> data = new ArrayList<>();

        for (CustomerIndexDto value : drop
        ) {
            data.add(new DropdownDto(value.getMembershipNumber().toString(), value.getFullname()));
        }
        return data;
    }

    public static List<DropdownDto> dropdownBook(List<BookIndexDto> drop) {
        List<DropdownDto> data = new ArrayList<>();

        for (BookIndexDto value : drop
        ) {
            Integer stock = value.getQuantity() - value.getInBorrow();
            data.add(new DropdownDto(value.getCode().toString(),
                    value.getTitle().concat(" (").concat(stock.toString()).concat(" left )")));
        }
        return data;
    }

    public static List<DropdownDto> dropdownGender() {
        List<DropdownDto> data = new ArrayList<>();
        data.add(new DropdownDto("Female", "Female"));
        data.add(new DropdownDto("Male", "Male"));
        return data;
    }

    public static List<DropdownDto> dropdownTitle() {
        List<DropdownDto> data = new ArrayList<>();
        data.add(new DropdownDto("Mrs", "Mrs"));
        data.add(new DropdownDto("Mr", "Mr"));
        data.add(new DropdownDto("Ms", "Ms"));
        return data;
    }


}
