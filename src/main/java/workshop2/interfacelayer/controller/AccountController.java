/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;



import java.util.List;

import java.util.Optional;

import javax.persistence.EntityManager;

import javax.persistence.NoResultException;

import javax.persistence.Query;

import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.CriteriaQuery;

import workshop2.domain.Account;

import workshop2.interfacelayer.view.AccountView;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import workshop2.interfacelayer.DatabaseConnection;

import workshop2.interfacelayer.persistencelayer.GenericDaoImpl;

import workshop2.interfacelayer.view.Validator;



/**

 *

 * @author hwkei

 */

public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);   

    private final AccountView accountView;

    private Account account;

    private final GenericDaoImpl<Account> accountDao;

    

    private EntityManager entityManager;

    //private EntityTransaction et;

    

    // Public constructor only requires accountView parameter

    public AccountController(AccountView accountView) {

        this.accountView = accountView;

        entityManager = DatabaseConnection.getInstance().getEntityManager();

        accountDao = new GenericDaoImpl<>(Account.class, entityManager);

    }

    

    // Package private constructor can be injected with accountView AND AccountDao for testing

    AccountController(AccountView accountView, GenericDaoImpl<Account> accountDao) {

        this.accountView = accountView;

        this.accountDao = accountDao;

    }

    

    public void createAccount() {

        accountView.showNewAccountScreen();

        

        String name = accountView.requestUsernameInput();

        if (name == null) return; // User interupted createAccount proces

        String password = accountView.requestPasswordInput();

        if (password == null) return; // User interupted createAccount proces

        

        // create a password hash from this password and store this in the database

        password = PasswordHash.generateHash(password);

        

        // Get requested account type from user

        // TODO: Dit gaat uit van kennis van het databaseID, moet worden aangepast!!!!

        Integer accountTypeId = accountView.requestAccountType(getAvailableAccountTypes());

        if (accountTypeId == null) return;  // User interupted createAccount proces

        

        try {

            entityManager.getTransaction().begin();

          

            account = (Account)accountDao.findById(accountTypeId.longValue());

                

            // Prepare the account with the validated values and add it to the database

            account = new Account(name, password, accountTypeId);        

            accountDao.persistent(account);

            

            entityManager.getTransaction().commit();

            

        } catch (Exception ex) {

            entityManager.getTransaction().rollback();

            System.out.println("Transactie is niet uitgevoerd!");            

            // Exception doorgooien of FailedTransaction oid opgooien?

        } finally {

            // Always clear the persistence context to prevent increasing memory

            entityManager.clear();

        }

    }

       

    public void updateAccount() {

        // Prompt for which account to update

        List<Account> accountList = listAllAccounts();

        int accountListSize = accountList.size();

        

        Integer index = accountView.requestAccountIdToUpdateInput(accountListSize);

        if (index == null) return;

        

        Account accountBeforeUpdate = accountList.get(index);

        accountView.showAccountToBeUpdated(accountBeforeUpdate);

        int Id = accountBeforeUpdate.getId();

        String newName = accountView.requestUpdateUsernameInput();

        if (newName == null) {

            newName = accountBeforeUpdate.getUsername();

        }

        String newPassword = accountView.requestUpdatePasswordInput();

        if (newPassword == null) {

            newPassword = accountBeforeUpdate.getPassword();

        } else {

            // create a password hash from his password and store this in the database

            newPassword = PasswordHash.generateHash(newPassword);

        }

        Integer newAccountType = accountView.requestUpdateAccountType(getAvailableAccountTypes());

        if (newAccountType == null) {

            newAccountType = accountBeforeUpdate.getAccountTypeId();

        }

        

        Account accountAfterUpdate = new Account(newName, newPassword, newAccountType);

        //Promp for confirmation of the selected update

        accountView.showAccountUpdateChanges(accountBeforeUpdate, accountAfterUpdate);

        Integer confirmed = accountView.requestConfirmationToUpdate();

        if (confirmed == null || confirmed == 2){

            return;

        }

        else {

            log.debug(accountAfterUpdate.toString());

//            accountDao.updateAccount(accountAfterUpdate);

            GenericDaoImpl gdi = new GenericDaoImpl(Account.class, DatabaseConnection.getInstance().getEntityManager());

            gdi.update(accountAfterUpdate);

        }

    }

    

    public void changeOwnPassword(String userName) {

        Optional<Account> optionalAccount = accountDao.findAccountByUserName(userName);

        if (!optionalAccount.isPresent()) {

            log.error("Gebruiker {} niet gevonden in de database!", userName);

            return;

        }

        account = optionalAccount.get();

        String oldPassword = accountView.requestOldPasswordInput();

        if (validateAccount(userName, oldPassword)) {

            String newPassword = accountView.requestNewPasswordInput();

            if (newPassword != null && Validator.isValidPassword(newPassword)) {

                // create a password hash from his password and store this in the database

                newPassword = PasswordHash.generateHash(newPassword);

                account.setPassword(newPassword);

//                accountDao.updateAccount(account);

                GenericDaoImpl gdi = new GenericDaoImpl(Account.class, DatabaseConnection.getInstance().getEntityManager());

                gdi.update(account);

            }

        } else {

            accountView.showInvalidOldPassword();

        }

    }

    

    public void deleteAccount() {

        //Prompt for which account to delete

        List<Account> accountList = listAllAccounts();

        int accountListSize = accountList.size();

        log.debug("accountListSize is " + accountListSize);

        Integer index = accountView.requestAccountIdInput(accountListSize);

        if (index == null) return;

        Long id = accountList.get(index).getId();

        

        //Retreive the account to delete from the database

//        Optional<Account> optionalAccount = accountDao.findAccountById(id);

        GenericDaoImpl gdi = new GenericDaoImpl(Account.class, DatabaseConnection.getInstance().getEntityManager());

        account = (Account)gdi.findById(id);

//        if (optionalAccount.isPresent()) account = optionalAccount.get();

        

        

        //Promp for confirmation if this is indeed the account to delete

        accountView.showAccountToBeDeleted(account);

        Integer confirmed = accountView.requestConfirmationToDelete(account);

        if (confirmed == null || confirmed == 2){

        }

        else {

            gdi.delete(account);

//            accountDao.deleteAccount(account);!

        }

    }

        

    public boolean validateAccount(String userName, String password) {

        

        Account resultAccount;

        try {

            Query queryAccountByUserName = entityManager.createNamedQuery("findAccountByUserName");

            queryAccountByUserName.setParameter("username", userName);

            resultAccount = (Account)queryAccountByUserName.getSingleResult();

            log.debug("Gebruiker {} is succesvol gevalideerd", userName);

        } catch(NoResultException ex) {

            return false;

        }

        return PasswordHash.validatePassword(password, resultAccount.getPassword());

    }

    

    public Integer getUserRole(String userName) {

        Account resultAccount;

        try {

            Query queryAccountByUserName = entityManager.createNamedQuery("findAccountByUserName");

            queryAccountByUserName.setParameter("username", userName);

            resultAccount = (Account)queryAccountByUserName.getSingleResult();

        } catch(NoResultException ex) {

            return null;

        }

            // TODO: Moet nog worden aangepast!!!!!!

            if (resultAccount.getAccountType().toString().equals("admin")) return 1;

            if (resultAccount.getAccountType().toString().equals("medewerker")) return 2;

            if (resultAccount.getAccountType().toString().equals("klant")) return 3;

        

        return 0;

    }

    

    public List<String> getAvailableAccountTypes() {

        return accountDao.getAllAccountTypesAsList();

    }

    

    public List<Account> listAllAccounts() {

        List<Account> accountList;

        accountList = accountDao.getAllAccountsAsList();        

        accountView.showListOfAllAccounts(accountList);        

        return accountList;

    }

    

    public Optional<Account> selectAccountByUser() {

        return accountView.selectAccount(listAllAccounts());

    }

    

    /**

     * Sets a new datasetype to be used. This option is only used for demonstration

     * purposes as this will also replace the database that is being used

     * @return 

     */

    public boolean setDatabaseType() {

        Integer newType = accountView.requestDatabaseTypeInput(DatabaseConnection.getInstance().getDatabaseType());

        if (newType == null) return false; // nothing changed

        if (newType == 1) {

            DatabaseConnection.getInstance().setDatabaseType("MYSQL");

        } else {

            DatabaseConnection.getInstance().setDatabaseType("MONGO");

        }

        // Databasetype has been changed

        return true;

    }

    

    /**

     * Sets the connection pool on or off. This option is only used for demonstration

     * purposes

     */

    public void setConnectionPool() {

        boolean newPoolSetting = accountView.requestConnectionPoolInput(DatabaseConnection.getInstance().getUseConnectionPool());

        DatabaseConnection.getInstance().useConnectionPool(newPoolSetting);

    }

    

    public void showCurrentDatabaseSettings() {

        

    }

}
