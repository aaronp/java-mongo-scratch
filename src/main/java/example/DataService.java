package example;

import com.example.myapp.model.UpdatedDocument;
import com.example.myapp.repository.DataRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public String saveData(Map<String, Object> payload) {
        Document document = new Document(payload);
        return dataRepository.save(document).getObjectId("_id").toString();
    }

    public UpdatedDocument updateData(String id, Map<String, Object> payload) {
        Document existingDocument = dataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        existingDocument.putAll(payload);
        dataRepository.save(existingDocument);

        UpdatedDocument updatedDocument = new UpdatedDocument();
        updatedDocument.setDocument(existingDocument);

        return updatedDocument;
    }
}
