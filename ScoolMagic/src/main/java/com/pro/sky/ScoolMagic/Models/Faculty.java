package com.pro.sky.ScoolMagic.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @OneToMany(mappedBy = "faculty")
    @JsonIgnore
            private List<Student> students=new ArrayList<>();

    public Faculty(){}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
    public void setStudents(List<Student> students) {
        this.students=students;
    }

    public List<Student> getStudents() {
        return students;
    }


    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", students=" + students +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Faculty faculty = (Faculty) object;
        return color == faculty.color && Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
