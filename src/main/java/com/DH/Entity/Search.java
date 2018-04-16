package com.DH.Entity;

public class Search {
	
	private String input;
	private String category;
	
	public Search()
	{
		
	}
	
	public Search (String input, String category)
	{
		this.input=input;
		this.category=category;
	}

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
