package com.example.aoi_endka.repository.interfaces;

import com.example.aoi_endka.exceptions.DatabaseOperationException;
import com.example.aoi_endka.exceptions.ResourceNotFoundException;
import java.util.List;


public interface CrudRepository<T> {


    int create(T entity) throws DatabaseOperationException;


    List<T> getAll() throws DatabaseOperationException;


    T getById(int id) throws DatabaseOperationException, ResourceNotFoundException;


    void update(int id, T entity) throws DatabaseOperationException, ResourceNotFoundException;


    void delete(int id) throws DatabaseOperationException, ResourceNotFoundException;
}