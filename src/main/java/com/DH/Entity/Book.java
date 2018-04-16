package com.DH.Entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//Named Queries
@NamedQueries( {
	@NamedQuery(name = "Book.findByTitle", query = "select o from Book o where o.title=:title"),
	@NamedQuery(name = "Book.findById", query = "select o from Book o where o.id=:id"),
	@NamedQuery(name = "Book.findAll", query = "select o from Book o"),
	@NamedQuery(name = "Book.search", query = "select o from Book o where o.title LIKE :search OR o.author LIKE :search")
})
//Defining JPA Hibernate Entity
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book implements Serializable{
	//To autogenerate my ID values for objects
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String title;
	private String author;
	private String price;
	private String category;
	private File image;
	private int stock;
	//Establishing a one to many relationship
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Rating> ratings = new ArrayList<Rating>();
	//Blank Constructor
	public Book() {
		
	}
	//Constructor for creating objects
	public Book(String title, String author, String price, String category, File image, int stock)
	{
		this.title = title;
		this.author = author;
		this.price = price;
		this.category = category;
		this.image = image;
		this.stock=stock;
	}
	//Constructor for editing object
	public Book(int id, String title, String author, String price, String category, File image, int stock)
	{
		this.id = id;
		this.title = title;
		this.author = author;
		this.price = price;
		this.category = category;
		this.image = image;
		this.stock=stock;
	}
	
	
	//Mutator methods
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public void addToRatings(Rating r)
	{
		this.ratings.add(r);		
	}
	
	public void removeFromRatings(Rating r)
	{
		//Iterator pattern use to avoid concurrent modification exception
		for (Iterator<Rating> iterator = this.ratings.iterator(); iterator.hasNext();) {
		    Rating rating = iterator.next();
		    if (rating.getId()==r.getId()) {
		        iterator.remove();
		    }
		}
	}
	
	public List<Rating> getRatings(){
		return this.ratings;
	}
	
	
	

}