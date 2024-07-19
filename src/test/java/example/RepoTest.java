package example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class RepoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testInsertAndFind() {
//        MyEntity entity = new MyEntity();
//        entity.setId("123");
//        entity.setName("Test Name");


        mongoTemplate.save(entity);

        MyEntity found = mongoTemplate.findById("123", MyEntity.class);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Name");
    }
}
