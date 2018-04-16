package com.DH.Entity;

public class Search {
	
	private String input;
	private String category;
	//Blank Constructor
	public Search()
	{
		
	}
	//Constructor for creating objects
	public Search (String input, String category)
	{
		this.input=input;
		this.category=category;
	}
	//Mutator methods
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
