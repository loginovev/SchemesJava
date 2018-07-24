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
import org.springframework.web.context.WebApplicationContext;
import ru.loginov.security.dto.SessionResponseDTOTest;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.filter.FilterMatchMode;

import javax.servlet.Filter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionRestTest {

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
    public void getAllTest() throws Exception{

        AuthenticationRestTest.authenticationUserHeaders("John","321",mockMVC);
        AuthenticationRestTest.authenticationUserHeaders("Paul","1",mockMVC);
        AuthenticationRestTest.authenticationUserHeaders("George","2",mockMVC);
        AuthenticationRestTest.authenticationUserHeaders("Ringo","3",mockMVC);

        List<FilterField> filterFields = new ArrayList<>();
        filterFields.add(new FilterField("username", FilterMatchMode.endWith,"n"));

        String params = "first=0&rows=5&sortField=username&sortOrder=-1&filters="+ URLEncoder.encode(JsonUtils.toJSON(filterFields), "UTF-8");

        MvcResult result = mockMVC.perform(
                get("/api/session/list?"+params,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        SessionResponseDTOTest sessionResponseDTOTest = JsonUtils.fromJSON(result.getResponse().getContentAsString(),SessionResponseDTOTest.class);
        Assert.assertNotNull(sessionResponseDTOTest);
        Assert.assertTrue(sessionResponseDTOTest.getData().size()>0);
    }
}
