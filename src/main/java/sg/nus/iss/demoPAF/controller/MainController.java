package sg.nus.iss.demoPAF.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    UserService userSvc;

    @GetMapping("/")
    public String showHomePage() {
        return "login";
    }

    @GetMapping("/authenticate/logout")
    public ModelAndView getLogout(HttpSession sess) {
        System.out.println("in getLogoutController");
        sess.invalidate();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }


    @PostMapping("/authenticate")
    public ModelAndView userLogin(@ModelAttribute User user, HttpSession sess) {
        System.out.println("in the UserController");

        ModelAndView mav = new ModelAndView();

        boolean login = false;
        try {
            login = userSvc.userLogin(user);

            //not successful
            if (!login) {
                mav.addObject("message", ("Incorrect username or password. " +
                        "Please check your credentials for %s").formatted(user.getUsername()));
                mav.setStatus(HttpStatus.UNAUTHORIZED);
                mav.setViewName("loginfailure");
                return mav;
            }

            //redirect to the protected page
            sess.setAttribute("username",user.getUsername());
            mav = new ModelAndView("redirect:/protected/login/success");

        } catch (UserLoginException ex) {
            mav.addObject("message", "Error: %s".formatted(ex.getReason()));
            mav.setStatus(HttpStatus.UNAUTHORIZED);
            mav.setViewName("loginfailure");
            ex.printStackTrace();
            return mav;

        }

        return mav;
    }

    @PostMapping("/search")
    @GetMapping("/search")
    public ModelAndView searchWord(@RequestBody MultiValueMap<String,String> payload,HttpSession sess) {
        String term = payload.getFirst("search");
        String favWord = (String) sess.getAttribute("favWord");
        if(favWord!=null) {
            sess.removeAttribute("favWord");
        }
        sess.setAttribute("searchTerm",term);
        return new ModelAndView("redirect:/protected/search/result");
    }

    @PostMapping("/favourite")
    @GetMapping("/favourite")
    public ModelAndView addFavourite(@RequestBody MultiValueMap<String,String> payload,HttpSession sess) {
        String favWord = payload.getFirst("favWord");
        sess.setAttribute("favWord",favWord);
        return new ModelAndView("redirect:/protected/add/favourite");
    }
}
