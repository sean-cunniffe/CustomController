package com.customcontroller.exceptions.repository;

import com.customcontroller.repository.Repository;

import javax.ejb.ApplicationException;

/**
 * Created by SeanCunniffe on 16/Jan/2022
 */

@ApplicationException
public class ResourceNotFoundException extends RepositoryException{


    public ResourceNotFoundException(Class<? extends Repository> repository) {
        super("Resource not found", repository);
    }
}
