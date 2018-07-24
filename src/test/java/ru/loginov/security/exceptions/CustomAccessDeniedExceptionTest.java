package ru.loginov.security.exceptions;

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
import ru.loginov.security.AuthenticationRestTest;
import ru.loginov.security.dto.ErrorDescriptionDTOTest;
import ru.loginov.security.dto.UserDTOTest;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.filter.FilterMatchMode;

import javax.servlet.Filter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomAccessDeniedExceptionTest {

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

    //LogRest
    @Test
    @Transactional
    public void LogRest_getColumnOptionsTest() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/log/columnOptions",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void LogRest_getLogTest() throws Exception{

        List<FilterField> filterFields = new ArrayList<>();
        //filterFields.add(new FilterField("date",FilterMatchMode.START_WITH,"2017-11-04"));
        filterFields.add(new FilterField("messageType", FilterMatchMode.equals,"ERROR"));

        String params = "first=0&rows=100&sortField=username&sortOrder=-1&filters="+ URLEncoder.encode(JsonUtils.toJSON(filterFields), "UTF-8");

        MvcResult result = mockMVC.perform(
                get("/api/log/records?"+params,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }
    //LogRest

    //SessionRest
    @Test
    public void SessionRest_getAllTest() throws Exception{

        String params = "first=0&rows=5&sortField=username&sortOrder=-1";

        MvcResult result = mockMVC.perform(
                get("/api/session/list?"+params,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    public void SessionRest_deleteSessionTest() throws Exception{

        MvcResult result = mockMVC.perform(
                delete("/api/session/delete?username=admin",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }
    //SessionRest

    //UsersRest
    @Test
    @Transactional
    public void UsersRest_getAllUsersTest() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/list?first=0&rows=10",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void UsersRest_updateUserTest() throws Exception{

        UserDTOTest userDTOTest = new UserDTOTest();

        MvcResult result = mockMVC.perform(
                post("/api/user/update",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void UsersRest_newUserTest() throws Exception{

        UserDTOTest userDTOTest = new UserDTOTest();

        MvcResult result = mockMVC.perform(
                put("/api/user/new",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }

    @Test
    @Transactional
    public void UsersRest_deleteUserTest() throws Exception{

        MvcResult result = mockMVC.perform(
                delete("/api/user/delete?username=Ringo",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC))
        )
                .andExpect(status().is(403))
                .andReturn();

        Assert.assertNotNull(result);
        String jsonString = result.getResponse().getContentAsString();
        Assert.assertNotEquals(jsonString,"");

        ErrorDescriptionDTOTest errorDescriptionDTOTest = JsonUtils.fromJSON(jsonString,ErrorDescriptionDTOTest.class);

        Assert.assertEquals(errorDescriptionDTOTest.getDescription(),"ACCESS-DENIED");
        Assert.assertEquals(errorDescriptionDTOTest.getStatus(),403);
        Assert.assertNotNull(errorDescriptionDTOTest.getMessage());
    }
    //UsersRest

}
