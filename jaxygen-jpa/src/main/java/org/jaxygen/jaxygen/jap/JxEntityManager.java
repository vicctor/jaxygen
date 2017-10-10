/*
 * Copyright 2017 Artur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.jaxygen.jap;

import com.google.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Artur
 */
@Singleton
public class JxEntityManager implements EntityManager {

    private final EntityManagerFactory emFactory;

    private JxEntityManager(JxJPASetup setup) {
        emFactory = Persistence.createEntityManagerFactory(setup.getFactoryName());
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    @Override
    public void persist(Object entity) {
        getEntityManager().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        getEntityManager().remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return getEntityManager().find(entityClass, primaryKey);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return getEntityManager().getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        getEntityManager().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        getEntityManager().setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return getEntityManager().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        getEntityManager().lock(entity, lockMode);
    }

    @Override
    public void refresh(Object entity) {
        getEntityManager().refresh(entity);
    }

    @Override
    public void clear() {
        getEntityManager().clear();
    }

    @Override
    public boolean contains(Object entity) {
        return getEntityManager().contains(entity);
    }

    @Override
    public Query createQuery(String qlString) {
        return getEntityManager().createQuery(qlString);
    }

    @Override
    public Query createNamedQuery(String name) {
        return getEntityManager().createNativeQuery(name);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return getEntityManager().createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return getEntityManager().createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return getEntityManager().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public void joinTransaction() {
        getEntityManager().joinTransaction();
    }

    @Override
    public Object getDelegate() {
        return getEntityManager().getDelegate();
    }

    @Override
    public void close() {
        getEntityManager().close();
    }

    @Override
    public boolean isOpen() {
        return getEntityManager().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

}
