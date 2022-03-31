package com.customcontroller.exceptions.repository;

import com.customcontroller.repository.Repository;

/**
 * Created by SeanCunniffe on 16/Jan/2022
 */

public abstract class RepositoryException extends RuntimeException {

    public RepositoryException() {
    }

    protected RepositoryException(String message, Class<? extends Repository> repository) {
        super(message+ repository.getCanonicalName());
    }

}
