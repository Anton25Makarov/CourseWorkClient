package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.Objects;

public abstract class Sport implements Serializable {
    private static final long serialVersionUID = 8414573596571830858L;

    protected String name;

    public Sport(String name) {
        this.name = name;
    }

    public Sport() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sport sport = (Sport) o;
        return Objects.equals(name, sport.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Sport{" +
                "name='" + name + '\'' +
                '}';
    }
}
