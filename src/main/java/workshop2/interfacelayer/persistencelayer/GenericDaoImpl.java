/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.persistencelayer;

import com.sun.xml.internal.bind.v2.model.core.ID;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import workshop2.domain.Account;

/**
 *
 * @author Ahmed-Al-Alaaq(Egelantier)
 */
public class GenericDaoImpl<T> {

    @PersistenceContext
    protected EntityManager em;
    protected Class<T> entityClass;

    public GenericDaoImpl(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    public void persistent(T object) {

        em.persist(object);
    }

    public Optional<T> findById(long id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<T> findAll(Class<T> entityClass) {
        CriteriaQuery<T> c
                = em.getCriteriaBuilder().createQuery(entityClass);
        c.select(c.from(entityClass));
        return em.createQuery(c).getResultList();
    }

    public T update(T object) {

        return em.merge(object);

    }

    public void delete(T object) {

        em.remove(object);

    }

}
