package com.scbb.bank.interfaces;

import java.util.List;

public interface AbstractService<E, I> {

    List<E> findAll();

    E findById(I id);

    E persist(E e);

    void delete(I id);

    List<E> search(E e);

}
