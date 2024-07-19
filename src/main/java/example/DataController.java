import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping
    public ResponseEntity<String> createOnboarding(@RequestBody String jsonPayload) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection("onboarding");
        Document document = Document.parse(jsonPayload);
        collection.insertOne(document);
        return ResponseEntity.ok(document.getObjectId("_id").toString());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateOnboarding(@PathVariable String id, @RequestBody String jsonPayload) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection("onboarding");
        Document updatedDocument = Document.parse(jsonPayload);

        Document existingDocument = collection.find(eq("_id", new org.bson.types.ObjectId(id))).first();
        if (existingDocument == null) {
            return ResponseEntity.notFound().build();
        }

        for (String key : updatedDocument.keySet()) {
            collection.updateOne(eq("_id", new org.bson.types.ObjectId(id)), set(key, updatedDocument.get(key)));
        }

        return ResponseEntity.ok(id);
    }
}
