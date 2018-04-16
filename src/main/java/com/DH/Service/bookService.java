package com.DH.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DH.Entity.Book;
import com.DH.Repository.bookRepository;


//Establish service with Spring
@Service
public class bookService {

@Autowired
protected bookRepository bookRepository;
//Inherited Save method
public void save(Book book) {
    bookRepository.save(book);
} 

//Methods executing named queries
public List<Book> findByTitle(String title)
{
	return bookRepository.findByTitle(title);
}

public List<Book> search(String search)
{
	return bookRepository.search(search);
}

public List<Book> findById(int id)
{
	return bookRepository.findById(id);
}

public List<Book> findAll()
{
	return bookRepository.findAll();
}
}