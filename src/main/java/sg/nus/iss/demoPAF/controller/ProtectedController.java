package sg.nus.iss.demoPAF.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sg.nus.iss.demoPAF.model.Quiz;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.model.Word;
import sg.nus.iss.demoPAF.service.QuizService;
import sg.nus.iss.demoPAF.service.UserService;
import sg.nus.iss.demoPAF.service.WordService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/protected")
public class ProtectedController {

    @Autowired
    private WordService wordService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    private static final Logger logger = Logger.getLogger(ProtectedController.class.getName());

    @GetMapping("/login/{view}")
    @PostMapping("/login/{view}")
    public ModelAndView post(@PathVariable String view, HttpSession sess) {

        String username = (String) sess.getAttribute("username");
        logger.info(">>>>view" + view);

        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("login"+view);
        mvc.addObject("message", "You have successfully login.");
        mvc.addObject("username",username);
        mvc.setStatus(HttpStatus.OK);

        return mvc;
    }

    @PostMapping("/search")
    public ModelAndView searchWord (@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {
        logger.entering("ProtectedController","searchWord-POST");

        String term = payload.getFirst("search");
        sess.setAttribute("searchTerm",term);

        logger.info("searchTerm: "+term);

        String username = (String) sess.getAttribute("username");

        Optional<List<Word>> opt = wordService.searchWord(term);

        return ProtectedController.searchWordModelAndView(term,username,opt);

    }

    @GetMapping("/search")
    public ModelAndView searchWord(HttpSession sess) {
        logger.entering("ProtectedController","searchWord-GET");

        String term = (String) sess.getAttribute("searchTerm");
        logger.info("searchTerm: "+term);

        String username = (String) sess.getAttribute("username");

        Optional<List<Word>> opt = wordService.searchWord(term);

        return ProtectedController.searchWordModelAndView(term,username,opt);
    }

    public static ModelAndView searchWordModelAndView(String term, String username, Optional<List<Word>> opt) {
        ModelAndView mav = new ModelAndView();

        if(opt.isEmpty()) {
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("username",username);
            mav.setViewName("404");
            return mav;
        }

        List<Word> wordList = opt.get();
        logger.info("--------controller: wordList retrieved");


        mav.addObject("word",term);
        mav.addObject("wordList",wordList);
        mav.addObject("username",username);
        mav.setViewName("result");
        return mav;
    }

    @PostMapping("/favourite")
    public ModelAndView addFavourite(@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {

        logger.entering("ProtectedController","addFavourite-POST");

        String favWord = payload.getFirst("favWord");
        sess.setAttribute("favWord",favWord);

        logger.info(">>>>>>>>>>favWord: "+favWord);

        String username = (String) sess.getAttribute("username");

        ModelAndView mav = new ModelAndView();


        // get userId
        Optional<User> optUser = userService.findUserByUsername(username);

        if (optUser.isEmpty()) {
            logger.severe(">>> addFavourite(): no user with username found");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            mav.setViewName("error");
            return mav;
        }

        User user = optUser.get();

        //to add favWord to db
        boolean isAdded = wordService.addFavWord(user.getUserId(),favWord);

        //display favourite list from database
        Optional<List<String>> optFavList = wordService.getAllFavouriteByUser(user.getUserId());

        if (optFavList.isPresent()) {
            List<String> favList = optFavList.get();
            mav.addObject("favList",favList);
            sess.setAttribute("favList",favList);
        }

        sess.setAttribute("isAdded",isAdded);

        mav.addObject("username",username);
        mav.addObject("isAdded",isAdded);
        mav.addObject("favWord",favWord);
        mav.setViewName("userfavourite");

        return mav;
    }

    @GetMapping("/favourite")
    public ModelAndView addFavourite(HttpSession sess) {

        logger.entering("ProtectedController","addFavourite-GET");

        String favWord = (String) sess.getAttribute("favWord");
        String username = (String) sess.getAttribute("username");
        boolean isAdded = (boolean) sess.getAttribute("isAdded");
        List<String> favList = (List<String>) sess.getAttribute("favList");

        ModelAndView mav = new ModelAndView();

        if (favList != null) {
            mav.addObject("favList",favList);
        }

        mav.addObject("username",username);
        mav.addObject("isAdded",isAdded);
        mav.addObject("favWord",favWord);
        mav.setViewName("userfavourite");

        return mav;
    }

    @GetMapping("/activity/home")
    public ModelAndView showActivityHome(HttpSession sess) {
        logger.entering("ProtectedController","showActivityHome");

        String username = (String) sess.getAttribute("username");

        ModelAndView mav = new ModelAndView();
        mav.addObject("username",username);
        mav.setViewName("activityhome");
        return mav;
    }

    @PostMapping("/quiz/game")
    public ModelAndView getQuiz(@RequestBody MultiValueMap<String,String> payload,HttpSession sess) {

        logger.entering("ProtectedController","getQuiz - POST");

        String quizArea = payload.getFirst("area");
        Integer quizLevel = Integer.parseInt(payload.getFirst("level"));

        String username = (String) sess.getAttribute("username");

        Optional<List<Quiz>> opt = quizService.getQuiz(quizArea,quizLevel);

        sess.setAttribute("quizArea",quizArea);
        sess.setAttribute("quizLevel",quizLevel);

        return ProtectedController.getQuizModelAndView(opt,sess,quizArea,quizLevel,username);
    }

    @GetMapping("/quiz/game")
    public ModelAndView getQuiz(HttpSession sess) {

        logger.entering("ProtectedController","getQuiz - GET");
        String username = (String) sess.getAttribute("username");
        String quizArea = (String) sess.getAttribute("quizArea");
        Integer quizLevel = (Integer) sess.getAttribute("quizLevel");

        List<Quiz> quizList = (List<Quiz>) sess.getAttribute("quizList");
        List<Integer> correctAnswers = (List<Integer>) sess.getAttribute("correctAnswers");


        ModelAndView mvc = new ModelAndView();

        if (quizList==null || correctAnswers == null) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mvc;
        }

        mvc.setViewName("game");

        mvc.addObject("quizList", quizList);
        mvc.addObject("correctAnswers",correctAnswers);
        mvc.addObject("area",quizArea.toUpperCase());
        mvc.addObject("level",quizLevel);
        mvc.addObject("username",username);
        mvc.setStatus(HttpStatus.OK);

        return mvc;
    }

    public static ModelAndView getQuizModelAndView(Optional<List<Quiz>> opt, HttpSession sess,
                                                   String quizArea, Integer quizLevel, String username) {

        ModelAndView mvc = new ModelAndView();

        if (opt.isEmpty()) {
            mvc.setViewName("error");
            mvc.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mvc;
        }

        List<Quiz> quizList = opt.get();
        logger.info("----quizList gotten.");

        List<Integer> correctAnswers = new ArrayList<>();
        int i=1;
        for (Quiz quiz : quizList) {
            correctAnswers.add(quiz.getAnswer());
        }

        sess.setAttribute("quizList",quizList);
        sess.setAttribute("correctAnswers",correctAnswers);

        mvc.setViewName("game");

        mvc.addObject("quizList", quizList);
        mvc.addObject("correctAnswers",correctAnswers);
        mvc.addObject("area",quizArea.toUpperCase());
        mvc.addObject("level",quizLevel);
        mvc.addObject("username",username);
        mvc.setStatus(HttpStatus.OK);

        return mvc;
    }

    @PostMapping("/quiz/result")
    public ModelAndView submitQuizAnswer(@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {

        logger.entering("ProtectedController","submitQuizAnswer - POST");

        quizService.calculateQuizScore(payload, sess);

        return ProtectedController.submitQuizAnswerModelAndView(sess);

    }

    @GetMapping("/quiz/result")
    public ModelAndView submitQuizAnswer(HttpSession sess) {

        logger.entering("ProtectedController","submitQuizAnswer - GET");

        return ProtectedController.submitQuizAnswerModelAndView(sess);
    }

    public static ModelAndView submitQuizAnswerModelAndView(HttpSession sess) {

        String username = (String) sess.getAttribute("username");
        String quizArea = (String) sess.getAttribute("quizArea");
        Integer quizLevel = (Integer) sess.getAttribute("quizLevel");
        Integer score = (Integer) sess.getAttribute("userScore");

        List<Quiz> quizList = (List<Quiz>) sess.getAttribute("quizList");

        List<Integer> correctAnswers = (List<Integer>) sess.getAttribute("correctAnswers");

        List<Integer> userInput = (List<Integer>) sess.getAttribute("userInput");


        logger.info("userInput:  " + userInput);
        logger.info("correctAns: "+correctAnswers);

        ModelAndView mvc = new ModelAndView();

        mvc.setViewName("quizResult");
        mvc.addObject("score",score);
        mvc.addObject("quizList", quizList);
        mvc.addObject("correctAnswers",correctAnswers);
        mvc.addObject("area",quizArea.toUpperCase());
        mvc.addObject("level",quizLevel);
        mvc.addObject("username",username);
        mvc.addObject("userInput",userInput);
        mvc.setStatus(HttpStatus.OK);

        return mvc;
    }

}
