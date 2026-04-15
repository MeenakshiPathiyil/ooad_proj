package unisync.web;

import model.resource.ListingType;
import model.resource.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import service.ResourceService;
import service.StudentService;
import service.TransactionService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UniSyncApiController.class)
class UniSyncApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private TransactionService transactionService;

    @Test
    void resources_returnsSuccessAndDataArray() throws Exception {
        Resource r = new Resource(
                1,
                "Test Book",
                "A sample listing",
                "Good",
                ListingType.SELL,
                null,
                null
        );

        when(resourceService.getAvailableResources()).thenReturn(List.of(r));

        mvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].title").value("Test Book"))
                .andExpect(jsonPath("$.data[0].resourceId").value(1));
    }
}
