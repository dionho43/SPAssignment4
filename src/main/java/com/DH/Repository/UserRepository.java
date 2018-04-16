package com.DH.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DH.Entity.Book;
import com.DH.Entity.User;
//Interface class
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByUsername(@Param("username") String username);
	List<User> findById(@Param("id") int id);
	List<User> findAll();
}