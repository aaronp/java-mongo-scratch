package example;

import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends MongoRepository<Document, String> {
}
