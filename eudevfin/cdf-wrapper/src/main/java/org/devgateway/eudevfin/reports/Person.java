package org.devgateway.eudevfin.reports;

// Test class for jasperreports
public class Person {
	private int id;
	private String name;
	private String lastName;

	public Person() {
	}

	public Person(String name, String lastName) {
		this.name = name;
		this.lastName = lastName;
	}

	public Person(int id, String name, String lastName) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
