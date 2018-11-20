package by.bsuir.course.repository;

import java.util.List;

public interface Repository<T> {
    void add(T obj);

    void remove(T obj);

    void update(T obj);

    List<T> sortBy(Comparator comparator);

    List<T> findBy(Specification specification);
}
