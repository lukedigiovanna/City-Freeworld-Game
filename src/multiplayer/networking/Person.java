package multiplayer.networking;

import java.io.Serializable;

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int age;
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getAge() {
		return this.age;
	}
}
