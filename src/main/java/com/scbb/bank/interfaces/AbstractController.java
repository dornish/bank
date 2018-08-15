package com.scbb.bank.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AbstractController<E, I> {

    List<E> findAll();

    E findById(I id);

    E persist(E e);

    ResponseEntity delete(I id);

    List<E> search(E e);

    E modifyResource(E e);

    List<E> modifyResources(List<E> eList);


}
