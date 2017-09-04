/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.persistencelayer;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import javax.persistence.NoResultException;

import javax.persistence.Query;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import workshop2.domain.Address;
import workshop2.domain.Product;

import workshop2.interfacelayer.DatabaseConnection;



/**

 *

 * @author Al-Alaaq(Egelantier)

 */

public class PersistenceService {

    private static final Logger log = LoggerFactory.getLogger(PersistenceService.class);

    private final EntityManager entityManager;

    private final GenericDaoImpl addressDao, productDao;

    

    public PersistenceService() {

        entityManager = DatabaseConnection.getInstance().getEntityManager();

        addressDao = new GenericDaoImpl(Address.class, entityManager);
        productDao = new GenericDaoImpl(Address.class, entityManager);

    }

    

    public void createObject(Object obj) {

        try {

            entityManager.getTransaction().begin();

            // Retrieve the accountType            

           /* AccountType accountType = (AccountType)accountTypeDao.findById(accountTypeId); */               

            // Add the AccountType to the given Account (temporary workarround)

            /*account.setAccountType(accountType); */      

            addressDao.persistent(obj);            

            entityManager.getTransaction().commit();            

        } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");            

            // Exception doorgooien of FailedTransaction oid opgooien?

        } finally {

            // Always clear the persistence context to prevent increasing memory ????

            entityManager.clear();

        }

    }
    public void deleteObject(Object obj){
        try {

            entityManager.getTransaction().begin();    

            addressDao.delete(obj);            

            entityManager.getTransaction().commit(); 
            } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");            

            // Exception doorgooien of FailedTransaction oid opgooien?

        } finally {

            // Always clear the persistence context to prevent increasing memory ????

            entityManager.clear();

        }

        
    }
    
    
     public void updateObject(Object obj){
        try {

            entityManager.getTransaction().begin();    

            addressDao.update(obj);            

            entityManager.getTransaction().commit(); 
            } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");            

            // Exception doorgooien of FailedTransaction oid opgooien?

        } finally {

            // Always clear the persistence context to prevent increasing memory ????

            entityManager.clear();

        }

        
    }

    

    public Optional<Address> findAddressByCustomerId(int id) {

        Address adres;
        String sql = "select i from Address i  where Customer.id = :id";

        try {

            adres = (Address)addressDao.findAddressByCustomerId(id, sql);

        } catch(NoResultException ex) {

            log.debug("The id for customer {} is not found in the database", id);

            return Optional.empty();

        }

        return Optional.ofNullable(adres);

    }
    
    public List<Address> findAllAddressByCustomerId(int id){
        
        List<Address> addresses = null;
        String sql = "select i from Address i  where Customer.id = :id";

        try {

           return addresses = addressDao.findAllAddressByCustomerId(id, sql);

        } catch(NoResultException ex) {
              log.debug("The id for customer {} is not found in the database", id);
              return null;
        }
        
    }
    
    public List<Product> getAllProductsAsList(){
        List<Product> products = null;
      
        return products = productDao.findAllProducts();
        
    }



}
