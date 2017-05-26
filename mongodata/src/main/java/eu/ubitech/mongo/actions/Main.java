package eu.ubitech.mongo.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private PersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        HashMap<Integer, String> hmap = new HashMap<>();
        hmap.put(12, "Anthony");
        hmap.put(18, "George");
        hmap.put(30, "Nick");

        List<Person> persons = new ArrayList<>();

        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            persons.add(new Person((int) mentry.getKey(), (String) mentry.getValue()));
            repository.save(persons);
        }
    }

}
