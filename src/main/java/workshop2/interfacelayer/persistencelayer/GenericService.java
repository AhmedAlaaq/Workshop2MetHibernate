/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.persistencelayer;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Al-Alaaq(Egelantier)
 */
public interface GenericService {

    public <T> List<T> fetchAllAsList(Class<T> entityClass);

    public <T> Optional<T> fetchById(Class<T> entityClass, Long id);

}
