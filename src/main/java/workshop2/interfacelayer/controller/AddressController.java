/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import static org.hibernate.internal.util.collections.CollectionHelper.arrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.domain.Address;
import workshop2.interfacelayer.DatabaseConnection;
import workshop2.interfacelayer.dao.AddressDao;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.persistencelayer.GenericDaoImpl;
import workshop2.interfacelayer.persistencelayer.PersistenceService;
import workshop2.interfacelayer.view.AccountView;
import workshop2.interfacelayer.view.AddressView;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */
public class AddressController {
    private static final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final AddressView addressView;
    private Address address;
    private final PersistenceService addressDao;
    private EntityManager entityManager;
    
    public AddressController(AddressView addressView) {       
        
          this.addressView = addressView;

        entityManager = DatabaseConnection.getInstance().getEntityManager();

        addressDao = new PersistenceService();
    }
    
    AddressController(AddressView addressView, PersistenceService addressDao) {

        this.addressView = addressView;

        this.addressDao = addressDao;

    }
    
    public void createAddress(CustomerController customerController) {        
        // We first need a valid customer to link to the new address
        addressView.showConstructAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip creating the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        addressView.showListOfAddresses(listAddresses);
        if (listAddresses.size() == 3) {
            addressView.showNoAddressCanBeAdded();
            return;
        }        
        Optional<Address> optionalAddress = addressView.constructAddress(customerId, getAvailableAddressTypes());   
        if (optionalAddress.isPresent()) {
            // If the address type already exists we have to abort
            for (Address address : listAddresses) {
                if (optionalAddress.get().getAddressType() == address.getAddressType()) {
                    addressView.showAddressTypeExists();
                    return;
                }
            }
            addressDao.createObject(optionalAddress.get());
        }       
    }
    
    public void deleteAddress(CustomerController customerController) {
        // We first need a valid customer to find the address to be deleted
        addressView.showDeleteAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip deleting the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        Optional<Address> optionalAddress = addressView.selectAddressToDelete(listAddresses);
        if (optionalAddress.isPresent()) {
            addressDao.deleteObject(optionalAddress.get());
        }            
    }
    
    public void updateAddress(CustomerController customerController) {
        // We first need a valid customer to find the address to be deleted
        addressView.showUpdateAddressStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip deleting the address
            return;
        }
        List<Address> listAddresses = listAllAddressesFromCustomer(customerId);
        Optional<Address> optionalAddress = addressView.selectAddressToUpdate(listAddresses);
        if (optionalAddress.isPresent()) {
            addressDao.updateObject(optionalAddress.get());
        }            
        
    }
    
   
    
    List<Address> listAllAddressesFromCustomer(int customerId) {

        return addressDao.findAllAddressByCustomerId(customerId);
        
               
    }
    
    List<String> getAvailableAddressTypes() {
        List<String> addressType = null;
        addressType.add("HUIS");
        addressType.add("WERK");
        
        return addressType;
    }
    
}
