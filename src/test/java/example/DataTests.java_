package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class DataTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testSubmitData() throws Exception {
        String jsonContent = "{\"key\":\"value\"}";

        mockMvc.perform(post("/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testUpdateData() throws Exception {
        String jsonContent = "{\"key\":\"value\"}";

        String response = mockMvc.perform(post("/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String id = response.replaceAll("[^0-9]", ""); // Simplistic way to extract ID, adjust as necessary

        String updateContent = "{\"newKey\":\"newValue\"}";

        mockMvc.perform(patch("/data/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.document.newKey").value("newValue"));
    }
}
