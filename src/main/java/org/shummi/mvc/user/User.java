package org.shummi.mvc.user;

import org.shummi.mvc.pet.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private List<Pet> pets = new ArrayList<>();

    public User() {
    }

    public User(Long id, String name, String email, Integer age, List<Pet> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public void updatePet(Pet pet) {
        int index = pets.indexOf(pet);
        pets.set(index, pet);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("email='" + email + "'")
                .add("age=" + age)
                .add("pets=" + pets)
                .toString();
    }
}
