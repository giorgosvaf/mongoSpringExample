/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ubitech.mongo.actions;

/**
 *
 * @author giovaf
 */
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

    public int max_size = 1000000;

    public Person findByAge(int age);

    public List<Person> findByName(String name);

}
