package sg.nus.iss.demoPAF.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sg.nus.iss.demoPAF.model.Quiz;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.service.QuizService;
import sg.nus.iss.demoPAF.service.UserService;
import sg.nus.iss.demoPAF.service.WordService;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerTest {

    @Autowired
    UserService userSvc;

    @Autowired
    WordService wordService;

    @Autowired
    QuizService quizService;

    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    void shouldReturnLoginPage(){
        RequestBuilder req = MockMvcRequestBuilders.get("/");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(view().name("login")).andReturn();
        } catch (Exception ex) {
            fail("cannot perform mvc invocation", ex);
            return;
        }
    }

    @Test
    @Order(2)
    void shouldReturnLoginPageAfterLogout(){
        RequestBuilder req = MockMvcRequestBuilders.get("/authenticate/logout");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(view().name("login")).andReturn();
        } catch (Exception ex) {
            fail("cannot perform mvc invocation", ex);
            return;
        }
    }

    @Test
    @Order(3)
    void shouldReturnRegisterResultPageAndRegisterUser(){
        RequestBuilder req = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username","tester95")
                .param("firstName","tester")
                .param("lastName","tan")
                .param("email","tester@gmail.com")
                .param("password","abc123")
                .param("gender","F");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(view().name("registerResult")).andReturn();
        } catch (Exception ex) {
            fail("cannot perform mvc invocation", ex);
            return;
        }

        Optional<User> opt = userSvc.findUserByUsername("tester95");
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    @Order(4)
    void shouldNotRegisterUserWithExistingUsername(){
        RequestBuilder req = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username","tester95")
                .param("firstName","tester")
                .param("lastName","tan")
                .param("email","tester@gmail.com")
                .param("password","abc123")
                .param("gender","F");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(view().name("registerResult"))
                    .andReturn();

        } catch (Exception ex) {
            fail("cannot perform mvc invocation", ex);
            return;
        }

    }

    @Test
    @Order(5)
    void shouldReturnLoginSuccess() {
        RequestBuilder req = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "tester95")
                .param("password", "abc123");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

        } catch (Exception e) {
            fail("failed to login and redirect to login success page");
        }
    }

    @Test
    @Order(5)
    void shouldReturnLoginFailure() {
        RequestBuilder req = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "minion")
                .param("password", "abc123");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(view().name("loginfailure"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to login and redirect to login success page");
        }
    }

    @Test
    @Order(5)
    void shouldReturnProtectedLoginSuccess() {
        RequestBuilder req = MockMvcRequestBuilders.get("/protected/login/success")
                .sessionAttr("username", "tester95");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("loginsuccess"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to login");
        }
    }

    @Test
    @Order(6)
    void shouldSearchWord() {
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/search")
                .sessionAttr("username", "tester95")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("search","hello");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("result"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to login");
        }

    }

    @Test
    @Order(7)
    void shouldGetSearchResultPage() {
        RequestBuilder req = MockMvcRequestBuilders.get("/protected/search")
                .queryParam("term","happy")
                .sessionAttr("username", "tester95");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("result"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to search");
        }

    }

    @Test
    @Order(8)
    void shouldReturnSearchNotFound() {
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/search")
                .sessionAttr("username", "tester95")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("search","sssss");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(view().name("404"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform invocation");
        }

    }

    @Test
    @Order(9)
    void shouldAddFavourite() {
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/favourite")
                .sessionAttr("username", "tester95")
                .sessionAttr("searchTerm","hello")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("favWord","hello");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("userfavourite"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform add favourite");
        }
    }

    @Test
    @Order(10)
    void shouldNotAddFavouriteWhenUserDoesNotExist() {
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/favourite")
                .sessionAttr("username", "minion")
                .sessionAttr("searchTerm","hello")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("favWord","hello");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(view().name("error"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform add favourite");
        }
    }

    @Test
    @Order(11)
    void shouldGetFavourite() {
        RequestBuilder req = MockMvcRequestBuilders.get("/protected/favourite")
                .sessionAttr("username", "tester95");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("favourite"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform invocation");
        }
    }

    @Test
    @Order(12)
    void shouldGetActivityHome() {
        RequestBuilder req = MockMvcRequestBuilders.get("/protected/activity/home")
                .sessionAttr("username", "tester95");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("activityhome"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform invocation");
        }
    }

    @Test
    @Order(13)
    void shouldGetQuizGame() {
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/quiz/game")
                .sessionAttr("username", "tester95")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("area","sat")
                .param("level","10");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("game"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform get quiz game");
        }
    }

    @Test
    @Order(14)
    void shouldGetQuizResult() {
        RequestBuilder req = MockMvcRequestBuilders.post("/protected/quiz/result")
                .sessionAttr("username", "tester95")
                .sessionAttr("quizArea","sat")
                .sessionAttr("quizLevel",10)
                .sessionAttr("quizList",new ArrayList<Quiz>())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("correctAnswers","[2, 1, 2, 2, 2, 1, 2, 2, 1, 2]")
                .param("answer1","1")
                .param("answer2","1")
                .param("answer3","1")
                .param("answer4","1")
                .param("answer5","1")
                .param("answer6","1")
                .param("answer7","1")
                .param("answer8","1")
                .param("answer9","1")
                .param("answer10","1");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("quizResult"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform get quizResult");
        }
    }

    @Test
    @Order(15)
    void shouldGetDashboard() {
        RequestBuilder req = MockMvcRequestBuilders.get("/protected/dashboard")
                .sessionAttr("username", "tester95");


        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("dashboard"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform invocation");
        }
    }

    @Test
    @Order(16)
    void shouldGetAboutUs() {
        RequestBuilder req = MockMvcRequestBuilders.get("/protected/about")
                .sessionAttr("username", "tester95");

        MvcResult result = null;
        try {
            result = mvc.perform(req).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("aboutus"))
                    .andReturn();

        } catch (Exception e) {
            fail("failed to perform invocation");
        }
    }


    @AfterAll
    void deleteUserTester() {

        Optional<User> opt = userSvc.findUserByUsername("tester95");
        if (opt.isEmpty()) {
            fail("Tester95 does not exist");
        }

        int userId = opt.get().getUserId();
        wordService.deleteAllFavouriteByUser(userId);
        quizService.deleteQuizActivity(userId);
        userSvc.deleteUser(userId);
    }
}
