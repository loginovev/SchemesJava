package ru.loginov.security;

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

import ru.loginov.security.dto.UserDTOTest;
import ru.loginov.security.dto.UserResponseDTOTest;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.filter.FilterMatchMode;
import ru.loginov.utils.sort_filter.sort.SortField;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.Filter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestTest {

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
    public void getAllUsers() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/list?first=1&rows=2",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        UserResponseDTOTest userResponseDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserResponseDTOTest.class);
        Assert.assertNotNull(userResponseDTOTest);
        Assert.assertTrue(userResponseDTOTest.getData().size()>0);
    }

    @Test
    public void getAllUsersFilter() throws Exception{

        List<FilterField[]> filters = new ArrayList<>();

        filters.add(new FilterField[]{new FilterField("username", FilterMatchMode.contains,"n")});
        filters.add(new FilterField[]{new FilterField("banned", FilterMatchMode.equals,false)});
        filters.add(new FilterField[]{new FilterField("username", FilterMatchMode.in,new String[]{"admin","Paul"})});

        filters.add(new FilterField[]{
                new FilterField("username", FilterMatchMode.contains,"n"),
                new FilterField("banned", FilterMatchMode.equals,false)
        });

        for (FilterField[] filterFields:filters) {
            String filterEncoded = URLEncoder.encode(JsonUtils.toJSON(filterFields),"UTF-8");

            MvcResult result = mockMVC.perform(
                    get("/api/user/list?first=0&rows=5&filters="+filterEncoded,false)
                            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
            )
                    .andExpect(status().is(200))
                    .andReturn();

            Assert.assertNotNull(result);

            UserResponseDTOTest userResponseDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserResponseDTOTest.class);
            Assert.assertNotNull(userResponseDTOTest);
            Assert.assertTrue(userResponseDTOTest.getData().size()>0);
        }
    }

    @Test
    public void getAllUsersSortTest() throws Exception{

        List<SortField> sortFields = new ArrayList<>();
        sortFields.add(new SortField("username",1));
        sortFields.add(new SortField("username",-1));

        for (SortField sortField:sortFields) {
            MvcResult result = mockMVC.perform(
                    get("/api/user/list?first=0&rows=5&sortField="+sortField.getSortField()+"&sortOrder="+sortField.getSortOrder(),false)
                            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
            )
                    .andExpect(status().is(200))
                    .andReturn();

            Assert.assertNotNull(result);

            UserResponseDTOTest userResponseDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserResponseDTOTest.class);
            Assert.assertNotNull(userResponseDTOTest);
            Assert.assertTrue(userResponseDTOTest.getData().size()>0);
        }
    }



    @Test
    @Transactional
    public void getUser() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/user?username=admin",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        UserDTOTest userDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserDTOTest.class);
        Assert.assertNotNull(userDTOTest);
    }

    @Test
    @Transactional
    public void updateUser() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/user?username=Paul",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        UserDTOTest userDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserDTOTest.class);
        Assert.assertNotNull(userDTOTest);

        userDTOTest.setPassword("1");
        userDTOTest.setFirstName("Sir Paul");
        userDTOTest.setBanned(true);

        mockMVC.perform(
                post("/api/user/update",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(200));


        result = mockMVC.perform(
                get("/api/user/user?username=Paul",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        UserDTOTest userDTOUpdated = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserDTOTest.class);
        Assert.assertNotNull(userDTOUpdated);

        Assert.assertTrue(userDTOUpdated.getFirstName().equals("Sir Paul"));
        Assert.assertTrue(userDTOUpdated.isBanned());
    }

    @Test
    @Transactional
    public void newUser() throws Exception{

        UserDTOTest userDTOTest = new UserDTOTest("Neo","Neo","Neo","123321","",new ArrayList<>(),new TreeMap<>(),false);

        mockMVC.perform(
                put("/api/user/new",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest))
        )
                .andExpect(status().is(200));
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception{

        MvcResult result = mockMVC.perform(
                delete("/api/user/delete?username=Paul",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

    }

    @Test
    @Transactional
    public void patchOptions() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/user?username=George",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        UserDTOTest userDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserDTOTest.class);
        Assert.assertNotNull(userDTOTest);

        userDTOTest.getOptions().put("language","en");

        mockMVC.perform(
                patch("/api/user/patchOptions",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationUserHeaders("George","2",mockMVC))
                        .content(JsonUtils.toJSON(userDTOTest.getOptions()))
        )
                .andExpect(status().is(200));

        result = mockMVC.perform(
                get("/api/user/user?username=George",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        userDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),UserDTOTest.class);
        Assert.assertNotNull(userDTOTest);
        Assert.assertTrue(userDTOTest.getOptions().get("language").equals("en"));
    }

    @Test
    public void getRoles() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/user/roles",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        String[] strings = JsonUtils.fromJSON(result.getResponse().getContentAsString(),String[].class);
        Assert.assertNotNull(strings);
        Assert.assertTrue(strings.length>0);
    }
}
