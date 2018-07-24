package ru.loginov.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.loginov.security.dto.LoginDTO;
import ru.loginov.security.dto.SessionDTOAuthenticationTest;
import ru.loginov.security.service.AuthenticationServiceImpl;
import ru.loginov.utils.JsonUtils;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationRestTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void indexAccess() {
        String body = this.restTemplate.getForObject("/", String.class);
        assertThat(body).contains("<!doctype html>");
    }

    @Test
    public void authenticateTest() throws Exception{

        LoginDTO loginDTO = new LoginDTO("admin","123");

        MvcResult result = mockMVC.perform(
                post("/api/authenticate",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonUtils.toJSON(loginDTO))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        SessionDTOAuthenticationTest sessionDTOAuthenticationTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),SessionDTOAuthenticationTest.class);
        Assert.assertNotNull(sessionDTOAuthenticationTest);
        Assert.assertNotNull(sessionDTOAuthenticationTest.getUsername());
        Assert.assertNotNull(sessionDTOAuthenticationTest.getSecurityToken());
        Assert.assertEquals(sessionDTOAuthenticationTest.getAuthorities().size(),1);
    }

    @Test
    public void logoutTest() throws Exception{

        HttpHeaders headers = AuthenticationRestTest.authenticationHeaders(mockMVC);

        String params = "username="+headers.get(AuthenticationServiceImpl.HEADER_SECURITY_LOGIN).get(0)+"&securityToken="+headers.get(AuthenticationServiceImpl.HEADER_SECURITY_DIGIT).get(0);

        MvcResult result = mockMVC.perform(
                get("/api/logout?"+params,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(headers)
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);
    }

    public static HttpHeaders authenticationHeaders(MockMvc mockMVC) throws Exception{
        return AuthenticationRestTest.authenticationUserHeaders("admin","123",mockMVC);
    }

    public static HttpHeaders authenticationUserHeaders(String username, String password, MockMvc mockMVC) throws Exception{

        LoginDTO loginDTO = new LoginDTO(username,password);

        MvcResult result = mockMVC.perform(
                post("/api/authenticate",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(JsonUtils.toJSON(loginDTO))
        )
                .andReturn();

        SessionDTOAuthenticationTest sessionAdmin = JsonUtils.fromJSON(result.getResponse().getContentAsString(),SessionDTOAuthenticationTest.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationServiceImpl.HEADER_SECURITY_LOGIN,sessionAdmin.getUsername());
        headers.set(AuthenticationServiceImpl.HEADER_SECURITY_DIGIT,sessionAdmin.getSecurityToken());

        return headers;
    }
}
