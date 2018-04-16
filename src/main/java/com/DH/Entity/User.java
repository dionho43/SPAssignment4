package com.DH.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.DH.Entity.Book;


@NamedQueries( {
	@NamedQuery(name = "User.findAll", query = "select o from User o"),
	@NamedQuery(name = "User.findById", query = "select o from User o where o.id=:id"),
	@NamedQuery(name = "User.findByUsername", query = "select o from User o where o.username=:username"),
})

@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String address;
	private boolean admin;
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Book> purchases = new ArrayList<Book>();
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Book> cart = new ArrayList<Book>();
	
	public User(){
		
	}
	
	
	public User(String firstName, String lastName, String address, String username, String password, boolean admin) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.username = username;
		this.password = password;
		this.admin = admin;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void addPurchase(Book b)
	{
		this.purchases.add(b);
	}
	
	public void removePurchase(Book b)
	{
		this.purchases.remove(b);
	}
	
	public List<Book> getPurchases(){
		return this.purchases;
	}
	
	public void addToCart(Book b)
	{
		this.cart.add(b);		
	}
	
	public void removeFromCart(Book b)
	{
		List<Book> toRemove = new ArrayList<>();
		for (Book a : this.cart) {
		    if (a.getId()==b.getId()) {
		        toRemove.add(a);
		    }
		}
		this.cart.removeAll(toRemove);
	}
	
	public List<Book> getCart(){
		return this.cart;
	}
	

	

	


}