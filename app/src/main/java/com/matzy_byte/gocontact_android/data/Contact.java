package com.matzy_byte.gocontact_android.data;

public class Contact {
    private final String firstName;
    private final String lastName;
    private final int age;

    public Contact(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }
}
