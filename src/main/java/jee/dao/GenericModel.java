/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package jee.dao;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

public abstract class GenericModel<E> {

    protected abstract EntityManager getEntityManager();
    protected abstract Class<E> getEntityClass();
    
	
    public void save(E obj) throws Exception{
        getEntityManager().persist(obj);
    }
    
    public void update(E obj) throws Exception{
        getEntityManager().merge(obj);
    }
    
    public boolean delete(E obj) throws Exception{
        try{
            getEntityManager().remove(obj);
            return true;
        }catch(Exception ex){ 
            return false;
        }
    }
    
    public List<E> findNamedQueries(String nquery) {
        List<E> found =  getEntityManager().createNamedQuery(nquery).getResultList();
        return found;
    }
    
    public List<E> find(String query) {
        List<E> found =  getEntityManager().createQuery(query).getResultList();
        return found;
    }
    
    public List<E> findAll() {
        
        String table = "";
        
        Entity mdl = getEntityClass().getAnnotation(Entity.class);
            
        if(mdl.name().equals("")){
            table = getEntityClass().getSimpleName();
        }else{
            table = mdl.name();
        }
        
        List<E> found =  getEntityManager().createQuery("SELECT t FROM "+table+" t").getResultList();
        
        return found;
    }
    
    public E findById(Long id) {
        E found =  (E) getEntityManager().find(getEntityClass(), id);
        return found;
    }

}
