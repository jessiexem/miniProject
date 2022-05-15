package sg.nus.iss.demoPAF.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sg.nus.iss.demoPAF.AppConfig;
import sg.nus.iss.demoPAF.filters.AuthenticationFilter;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;



@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {AppConfig.class})
public class ProtectedControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    HttpSession httpSession;


    @Test
    void searchWordPostShouldReturn200() {

        httpSession.setAttribute("username","user");
        Mockito.doReturn("user").when(httpSession).getAttribute("username");
        // Build the request
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/search")
//                .accept(MediaType.TEXT_HTML_VALUE)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .characterEncoding("utf-8")
                .param("search","test");

        // Call the controller
        MvcResult result = null;
        try {
//            result = mvc.perform(req).andReturn();
            result = mvc.perform(req).andDo(print())
                    .andExpect(view().name("result")).andReturn();
        } catch (Exception ex) {
            fail("cannot perform mvc invocation", ex);
            return;
        }

        // Get response
        MockHttpServletResponse resp = result.getResponse();
        try {
            String payload = resp.getContentAsString();
            assertNotNull(payload);
        } catch (Exception ex) {
            fail("cannot retrieve response payload", ex);
            return;
        }
    }

    @Test
    void searchWordGetShouldReturn200() throws Exception {

        Mockito.doReturn("user").when(httpSession).getAttribute("username");
        MvcResult mvcResult = (MvcResult) this.mvc.perform(MockMvcRequestBuilders.get("/protected/search"))
                .andDo(print())
                .andExpect(view().name("result"));

//        // Build the request
//        RequestBuilder req = MockMvcRequestBuilders.get("/protected/search")
//                .accept(MediaType.TEXT_HTML_VALUE);
//
//        // Call the controller
//        MvcResult result = null;
//        try {
//            result = mvc.perform(req).andReturn();
//        } catch (Exception ex) {
//            fail("cannot perform mvc invocation", ex);
//            return;
//        }
//
//        // Get response
//        MockHttpServletResponse resp = result.getResponse();
//        try {
//            String payload = resp.getContentAsString();
//            assertNotNull(payload);
//        } catch (Exception ex) {
//            fail("cannot retrieve response payload", ex);
//            return;
//        }
    }

}
