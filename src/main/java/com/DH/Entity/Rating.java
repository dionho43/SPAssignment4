package com.DH.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Rating implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String score;
	private String comment;
	private int userID;
	
	public Rating() {
		
	}
	
	public Rating(String score, String comment, int userID)
	{
		this.score = score;
		this.comment = comment;
		this.userID = userID;
	}
	
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getUserID() {
		return userID;
	}

	public void setUser(int userID) {
		this.userID = userID;
	}

}