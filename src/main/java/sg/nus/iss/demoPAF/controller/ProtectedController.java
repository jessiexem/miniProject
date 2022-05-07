package sg.nus.iss.demoPAF.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.model.Word;
import sg.nus.iss.demoPAF.service.UserService;
import sg.nus.iss.demoPAF.service.WordService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/protected")
public class ProtectedController {

    @Autowired
    private WordService wordService;


    @Autowired
    private UserService userService;

    @GetMapping("/search/{view}")
    @PostMapping("/search/{view}")
    public ModelAndView searchDictionary(@PathVariable String view, HttpSession sess) {
        System.out.println(">>>>> in searchDictionary() controller");
        System.out.println("should be result view: "+view);

        String term = (String) sess.getAttribute("searchTerm");
        System.out.println("searchTerm: "+term);

        String username = (String) sess.getAttribute("username");

        ModelAndView mav = new ModelAndView();

        Optional<List<Word>> opt = wordService.searchWord(term);

        if(opt.isEmpty()) {
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            mav.setViewName("404");
            return mav;
        }

        List<Word> wordList = opt.get();
        System.out.println("--------controller: wordList retrieved");


        mav.addObject("word",term);
        mav.addObject("wordList",wordList);
        mav.addObject("username",username);
        mav.setViewName(view);
        return mav;
    }

    @GetMapping("/add/{view}")
    @PostMapping("/add/{view}")
    public ModelAndView addFavourite(@PathVariable String view, HttpSession sess) {
        System.out.println(">>>>> in addFavourite() controller");
        System.out.println("should be favourite view: "+view);

        String favWord = (String) sess.getAttribute("favWord");

        System.out.println(">>>>>>>>>>favWord: "+favWord);

        String username = (String) sess.getAttribute("username");

        ModelAndView mav = new ModelAndView();


        // get userId
        Optional<User> optUser = userService.findUserByUsername(username);

        if (optUser.isEmpty()) {
            System.out.println(">>> addFavourite(): no user with username found");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            mav.setViewName("error");
            return mav;
        }

        User user = optUser.get();

        //to add favWord to db
        boolean isAdded = wordService.addFavWord(user.getUserId(),favWord);

        if (!isAdded) {
            System.out.println(">>> addFavourite(): failed to add word");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            mav.setViewName("error");
            return mav;
        }

        mav.addObject("username",username);
        mav.addObject("isAdded",isAdded);
        mav.addObject("favWord",favWord);
        mav.setViewName("user"+view);

        return mav;
    }

    @GetMapping("/login/{view}")
    @PostMapping("/login/{view}")
    public ModelAndView post(@PathVariable String view, HttpSession sess) {

        String username = (String) sess.getAttribute("username");
        System.out.println(">>>>view" + view);

        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("login"+view);
        mvc.addObject("message", "You have successfully login.");
        mvc.addObject("username",username);
        mvc.setStatus(HttpStatus.CREATED);
        mvc.setStatus(HttpStatus.OK);

        return mvc;
    }



    @GetMapping("/favourite")
    @PostMapping("/favourite")
    public ModelAndView addToFavourite(@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {
        System.out.println(">>>>> in addToFavourite() controller");
        String favWord = payload.getFirst("favWord");

        String username = (String) sess.getAttribute("username");

        ModelAndView mav = new ModelAndView();

        return mav;
    }
}
