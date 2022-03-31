package com.customcontroller.repository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by SeanCunniffe on 15/Feb/2022
 */

@Transactional
@Stateless
public abstract class Repository<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    public List<T> findAll(){
        String className = getEntityClass().getSimpleName();
        String queryS = "select entity from " +
                className +
                " entity";
        TypedQuery<T> query = entityManager.createQuery(queryS, getEntityClass());
        return query.getResultList();
    }

    public T add( T entity){
        entityManager.persist(entity);
        return entity;
    }

    public T merge(T entity){
        entityManager.merge(entity);
        return entity;
    }

    public boolean delete(T entity){
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        return true;
    }

    public abstract Class<T> getEntityClass();

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
