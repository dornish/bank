package com.scbb.bank.person.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AbstractController<E, I> {

    ResponseEntity<List<E>> findAll();

    ResponseEntity<E> findById(I id);

    ResponseEntity<E> persist(E e);

    ResponseEntity delete(I id);

    ResponseEntity<List<E>> search(E e);

    E modifyResource(E e);

    List<E> modifyResources(List<E> eList);


}
