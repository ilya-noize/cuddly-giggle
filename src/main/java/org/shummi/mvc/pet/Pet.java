package org.shummi.mvc.pet;

import java.util.Objects;
import java.util.StringJoiner;

public class Pet {
    private Long id;
    private String name;
    private Long userId;

    public Pet() {
    }

    public Pet(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long userId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Pet pet)) return false;

        return Objects.equals(id, pet.id) && name.equals(pet.name) && userId.equals(pet.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Pet.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("userId=" + userId)
                .toString();
    }
}
