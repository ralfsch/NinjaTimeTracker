/**
 * Copyright (C) 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import models.Booking;
import models.User;
import ninja.jpa.UnitOfWork;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class UserDao {
    
    @Inject
    Provider<EntityManager> entityManagerProvider;
    
    @UnitOfWork
    public boolean isUserAndPasswordValid(String username, String password) {
        
        if (username != null && password != null) {
            
            EntityManager entityManager = entityManagerProvider.get();
            
            Query q = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam");
            User user;
            try {
            	user = (User) q.setParameter("usernameParam", username).getSingleResult();
            }
            catch(NoResultException nodata)
            {
            	user = null;
            }

            
            if (user != null) {
                
                if (user.getPassword().equals(password)) {

                    return true;
                }
                
            }

        }
        
        return false;
 
    }

    @UnitOfWork
    public User getUser(Long id) {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM User x WHERE x.id = :idParam");
        User user = (User) q.setParameter("idParam", id).getSingleResult();        
        
        return user;
        
    }
    

}
