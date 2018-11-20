package by.bsuir.course.repository;

public interface Specification<T> {
    boolean specified(T obj);
}
