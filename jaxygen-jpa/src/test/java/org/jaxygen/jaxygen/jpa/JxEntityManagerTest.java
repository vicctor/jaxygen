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
package org.jaxygen.jaxygen.jpa;

import com.google.inject.Binder;
import com.google.inject.Module;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.jaxygen.frame.JaxygenApplication;
import org.jaxygen.jaxygen.jpa.entities.MyEntity;
import org.jaxygen.objectsbuilder.ObjectBuilderFactory;
import org.jaxygen.objectsbuilder.exceptions.ObjectCreateError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class JxEntityManagerTest implements Module {
    
    JaxygenApplication app;
    
    static class TestJPAConfig implements JxJPASetup {
        @Override
        public String getFactoryName() {
            return "test-persistence-unit";
        }
     
    }
    

    @Override
    public void configure(Binder binder) {
        binder.bind(JxJPASetup.class).toInstance(new TestJPAConfig());
    }
    
    
    
    @Before
    public void onStart() {
        app = new JaxygenApplication();
        app.start();
    }
    
    @After
    public void onStop() {
        app.stop();
    }


    @Test
    public void shall_createEntityManager() throws ObjectCreateError {
        // when
        EntityManager em = ObjectBuilderFactory.instance().create(EntityManager.class);
        
        // then
        Assertions.assertThat(em).isNotNull();
        
        javax.persistence.NamedStoredProcedureQuery n;
    }
    
    
    @Test
    public void shall_saveEntityInStorage() throws ObjectCreateError {
        // given
        EntityManager em = ObjectBuilderFactory.instance().create(EntityManager.class);
        MyEntity e = new MyEntity();
        e.setValue("My entity value");
        em.persist(e);
        
        // when
        MyEntity e2 = new MyEntity();
        e2.setValue("My entity value");
        em.persist(e2);
        
        // then
        Assertions.assertThat(e2.getId()).isPositive();       
    }

    
}
