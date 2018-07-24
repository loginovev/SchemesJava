package ru.loginov.references.scheme;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.loginov.references.scheme.dto.SchemeDTO;
import ru.loginov.references.scheme.dto.SchemeResponseDTO;
import ru.loginov.security.AuthenticationRestTest;
import ru.loginov.security.dto.ErrorDescriptionDTOTest;
import ru.loginov.utils.JsonUtils;

import javax.servlet.Filter;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchemeRestTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    //Mock to consume spring mvc rest controllers
    private MockMvc mockMVC;

    @Before
    public void setup() {
        this.mockMVC = MockMvcBuilders
                .webAppContextSetup(this.context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    private UUID newScheme() throws Exception{

        SchemeDTO schemeDTO = new SchemeDTO();
        schemeDTO.setId(new UUID(0L,0L));
        schemeDTO.setTitle("New scheme");
        schemeDTO.setSchemeAsJSON("{}");

        MvcResult result = mockMVC.perform(
                put("/api/scheme/new",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(schemeDTO))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);
        UUID uuid = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UUID.class);
        Assert.assertNotNull(uuid);

        return uuid;
    }

    @Test
    @Transactional
    public void getAllSchemeTest() throws Exception{

        for(long i=0;i<10;i++){
            newScheme();
        }

        MvcResult result = mockMVC.perform(
                get("/api/scheme/list?first=0&rows=10",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        SchemeResponseDTO schemeResponseDTO = JsonUtils.fromJSON(result.getResponse().getContentAsString(),SchemeResponseDTO.class);
        Assert.assertNotNull(schemeResponseDTO);
    }

    @Test
    @Transactional
    public void getSchemeTest() throws Exception{

        UUID uuid = newScheme();

        MvcResult result = mockMVC.perform(
                get("/api/scheme/get?id="+uuid,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        SchemeDTO schemeDTO = JsonUtils.fromJSON(result.getResponse().getContentAsString(),SchemeDTO.class);
        Assert.assertNotNull(schemeDTO);
    }

    @Test
    @Transactional
    public void newSchemeTest() throws Exception{
        newScheme();
    }

    @Test
    @Transactional
    public void updateSchemeTest() throws Exception{

        UUID uuid = newScheme();

        SchemeDTO schemeDTO = new SchemeDTO();
        schemeDTO.setId(uuid);
        schemeDTO.setTitle("New scheme");
        schemeDTO.setSchemeAsJSON("{}");

        mockMVC.perform(
                post("/api/scheme/update",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(schemeDTO))
        )
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void deleteSchemeTest() throws Exception{

        UUID uuid = newScheme();

        mockMVC.perform(
                delete("/api/scheme/delete?id="+uuid,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void customSchemeNotFoundExceptionTest() throws Exception{

        UUID uuid = new UUID(0L,0L);

        MvcResult result = mockMVC.perform(
                get("/api/scheme/get?id="+uuid,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();
        Assert.assertNotNull(result);

        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"SCHEME-NOT-FOUND");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

}
