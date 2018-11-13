package by.bsuir.repository;

public interface Specification<T> {
    boolean specified(T obj);
}
