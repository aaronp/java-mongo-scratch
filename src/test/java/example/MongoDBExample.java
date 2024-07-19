package example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Optional;

public class MongoDBExample {

    /**
     * This little badger contains the logic we care about for inserting/patching docs
     */
    public static class Data {

        public static Bson createUpdates(String jsonString) {
            Document updateDoc = Document.parse(jsonString);
            return Updates.combine(updateDoc.entrySet().stream()
                    .map(entry -> Updates.set(entry.getKey(), entry.getValue()))
                    .toArray(Bson[]::new));
        }


        final MongoCollection<Document> collection;
        public Data(MongoCollection<Document> c) {
            collection = c;
        }

        public String insert(String jason) {
            final Document doc = Document.parse(jason);
            final InsertOneResult result = collection.insertOne(doc);
            final BsonValue objectId = result.getInsertedId();
            return objectId.asObjectId().getValue().toHexString();
        }

        public Optional<Document> get(String stringId) {
            final ObjectId id = new ObjectId(stringId);
            final FindIterable<Document> found = collection.find(Filters.eq("_id", id));
            final MongoCursor<Document> iter = found.iterator();
            if (!iter.hasNext()) {
                return Optional.empty();
            } else {
                final Document only = iter.next();
                if (iter.hasNext()) {
                    throw new RuntimeException("BUG: mongo db is broken - multiple results returned for the same ID");
                }
                return Optional.of(only);
            }
        }

        public UpdateResult patch(String id, String jason) {
            final Bson updates = createUpdates(jason);
            return collection.updateOne(Filters.eq("_id", new ObjectId(id)), updates);
        }

    }

    public static void main(String[] args) {
        // Replace the URI string with your MongoDB deployment's connection string
        String uri = "mongodb://localhost:27017";

        try(MongoClient  mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("testdb");

            // Create the collection (it will create the collection if it doesn't exist)
            final Data data = new Data(database.getCollection("foo"));
            String id = data.insert("{ \"a\" : 123 }");

            // read back our initial doc
            Optional<Document> readBack = data.get(id);
            Document doc = readBack.get();
            Object a = doc.get("a");
            assert(a.toString().equals("123"));
            System.out.println(doc.toJson());

            UpdateResult patched = data.patch(id, "{ \"b\" : [4,5,6] }");
            assert(patched.getMatchedCount() == 1L);

            // read back our patched doc
            Optional<Document> readBackPatched = data.get(id);
            Document docPatched = readBackPatched.get();
            String patchedJson = docPatched.toJson();
            System.out.println(patchedJson);

            String expected = "\"a\": 123, \"b\": [4, 5, 6]";
            assert(patchedJson.contains(expected));

        }
    }
}
