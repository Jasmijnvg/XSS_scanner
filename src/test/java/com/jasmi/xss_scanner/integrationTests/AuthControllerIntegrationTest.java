package com.jasmi.xss_scanner.integrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser(username = "testuser", roles = "INTERNALUSER")
public class AuthControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldLoginUser() throws Exception {
        String requestJson = """
                 {
                     "userName":"admindummy",
                     "password":"wachtwoord123"
                 }
                """;

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/xss_scanner_api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
