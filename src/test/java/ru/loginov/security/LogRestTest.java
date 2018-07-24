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
import ru.loginov.security.dto.*;
import ru.loginov.security.service.LogServiceImpl;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.filter.FilterMatchMode;
import ru.loginov.utils.sort_filter.sort.SortField;

import javax.servlet.Filter;

import java.net.URLEncoder;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogRestTest {

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
    public void getAllTestJSON() throws Exception{

        List<FilterField> filterFields = new ArrayList<>();
        //filterFields.add(new FilterField("date",FilterMatchMode.startWith,"2017-12-26"));
        filterFields.add(new FilterField("username",FilterMatchMode.equals,"admin"));

        String params = "first=0&rows=100&sortField=date&sortOrder=1&filters="+URLEncoder.encode(JsonUtils.toJSON(filterFields), "UTF-8");

        MvcResult result = mockMVC.perform(
                get("/api/log/records?"+params,false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        LogResponseDTOTest logResponseDTO = JsonUtils.fromJSON(result.getResponse().getContentAsString(),LogResponseDTOTest.class);
        Assert.assertNotNull(logResponseDTO);
        Assert.assertTrue(logResponseDTO.getData().length>0);
    }


    @Test
    public void getAllTest() throws Exception{

        List<FilterField> filterFields = new ArrayList<>();
        //filterFields.add(new FilterField("date",FilterMatchMode.START_WITH,"2017-11-04"));
        filterFields.add(new FilterField("messageType",FilterMatchMode.equals,"ERROR"));

        List<SortField> sortFields = new ArrayList<>();
        //sortFields.add(new SortField("messageType",1));
        sortFields.add(new SortField("username",1));
        sortFields.add(new SortField("date",-1));

        LogResponseDTO logResponseDTO = new LogServiceImpl().getLog(0,100,sortFields, filterFields);
        Assert.assertTrue(logResponseDTO.getData().length>0);
    }

    @Test
    public void getColumnOptionsTest() throws Exception{

        MvcResult result = mockMVC.perform(
                get("/api/log/columnOptions",false)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .headers(AuthenticationRestTest.authenticationHeaders(mockMVC))
        )
                .andExpect(status().is(200))
                .andReturn();

        Assert.assertNotNull(result);

        LogColumnOptionsDTO columnOptionsDTO = JsonUtils.fromJSON(result.getResponse().getContentAsString(),LogColumnOptionsDTO.class);
        Assert.assertNotNull(columnOptionsDTO);
        Assert.assertTrue(columnOptionsDTO.getMessageType().length>0);
        Assert.assertTrue(columnOptionsDTO.getUsername().length>0);
        Assert.assertTrue(columnOptionsDTO.getEvent().length>0);
        Assert.assertTrue(columnOptionsDTO.getStructure().length>0);
    }
}
