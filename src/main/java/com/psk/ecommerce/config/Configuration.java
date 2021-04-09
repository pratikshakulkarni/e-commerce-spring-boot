package com.psk.ecommerce.config;

import com.psk.ecommerce.entity.Product;
import com.psk.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@org.springframework.context.annotation.Configuration
public class Configuration implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public Configuration(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] theUnSupportedActions = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions));

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnSupportedActions));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        //expose entity Ids

        //get entity classes from entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create an array of entity types
        List<Class> entityClasses = new ArrayList<>();

        //populate the arraylist (entity type for entities)
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
            System.out.println(tempEntityType.getJavaType());
        }

        //expose the entity IDS for array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
        System.out.println("Domain types : " + domainTypes);
    }
}
