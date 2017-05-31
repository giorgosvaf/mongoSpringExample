package eu.ubitech.mongo.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.Tuple2;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private PersonRepository repository;
    
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //put values in MongoDB
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

        //Find max values for each key
        List<Person> data = repository.findAll();

        long startTime = System.currentTimeMillis();
        SparkConf conf = new SparkConf().setMaster("local").setAppName("SparkAndMongo");
        JavaSparkContext context = new JavaSparkContext(conf);
        JavaRDD<Person> rdd = context.parallelize(data);

        JavaPairRDD<String, Integer> keyValuePairs = rdd.mapToPair(obj -> {
            Person person = obj;
            return new Tuple2(person.getName(), person.getAge());
        });

        JavaPairRDD<String, Integer> result = keyValuePairs.reduceByKey((v1, v2) -> Math.max(v1, v2));

        result.collect().stream().forEach((element) -> {
            LOGGER.log(Level.INFO, "Logging an INFO-level message: {0}", "Max result: (" + element._1 + ", " + element._2 + ")");
        });
        long endTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Logging an INFO-level message: {0}", "Max values found. Time :" + (endTime - startTime));
    }
}
