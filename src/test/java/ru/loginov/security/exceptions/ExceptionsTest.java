package ru.loginov.security.exceptions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ru.loginov.security.AuthenticationRestTest;
import ru.loginov.security.dto.ErrorDescriptionDTOTest;
import ru.loginov.security.dto.LoginDTO;
import ru.loginov.security.dto.UserDTOTest;
import ru.loginov.security.service.AuthenticationServiceImpl;
import ru.loginov.utils.JsonUtils;

import javax.servlet.Filter;

import java.util.ArrayList;
import java.util.TreeMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionsTest {

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

    @Test
    @Transactional
    public void CustomUsernameNotFoundExceptionTest() throws Exception{

        LoginDTO loginDTO = new LoginDTO("unknown","");

        MvcResult result = mockMVC.perform(
                post("/api/authenticate",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonUtils.toJSON(loginDTO))
        )
                .andExpect(status().is(403))
                .andReturn();
        Assert.assertNotNull(result);

        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"USER-NOT-FOUND");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void CustomDeleteYourOwnSessionException() throws Exception{

        MvcResult result = mockMVC.perform(
                delete("/api/session/delete?username=admin",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"DELETE-YOUR-OWN-SESSION");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void CustomSecretHeaderNotFoundException() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/list",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"SECRET-HEADER-NOT-FOUND");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void CustomUserNotAuthenticatedException() throws Exception{

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationServiceImpl.HEADER_SECURITY_LOGIN,"admin1");
        headers.set(AuthenticationServiceImpl.HEADER_SECURITY_DIGIT,"");

        MvcResult result = mockMVC.perform(
                get("/api/user/list",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(headers)
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"USER-NOT-AUTHENTICATED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void CustomWrongSecretTokenException() throws Exception{

        HttpHeaders headersAdmin = AuthenticationRestTest.authenticationHeaders(mockMVC);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationServiceImpl.HEADER_SECURITY_LOGIN,"admin");
        headers.set(AuthenticationServiceImpl.HEADER_SECURITY_DIGIT,"");

        MvcResult result = mockMVC.perform(
                get("/api/user/list",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(headers)
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"WRONG-SECRET-TOKEN");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    public void CustomErrorDescriptionNotFoundException() throws Exception{

    }

    @Test
    @Transactional
    public void CustomUserIsBannedException() throws Exception{

        UserDTOTest userDTOTest = new UserDTOTest("BannedUser","","","123321","",new ArrayList<>(),new TreeMap<>(),true);

        mockMVC.perform(
                put("/api/user/new",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(200));

        LoginDTO loginDTO = new LoginDTO(userDTOTest.getUsername(),userDTOTest.getPassword());

        MvcResult result = mockMVC.perform(
                post("/api/authenticate",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonUtils.toJSON(loginDTO))
        )
                .andReturn();


        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"USER-IS-BANNED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void CustomUserHasAlreadyRegisteredException() throws Exception{

        UserDTOTest userDTOTest = new UserDTOTest("UserIsRegistered","","","123321","",new ArrayList<>(),new TreeMap<>(),true);

        mockMVC.perform(
                put("/api/user/new",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(200));

        MvcResult result = mockMVC.perform(
                put("/api/user/new",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(400))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"USER-HAS-ALREADY-REGISTERED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),400);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

}
