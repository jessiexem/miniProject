package sg.nus.iss.demoPAF.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.service.UserService;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class MainController {

    @Autowired
    UserService userSvc;

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    @GetMapping("/")
    public String showHomePage() {
        return "login";
    }

    @GetMapping("/authenticate/logout")
    public ModelAndView getLogout(HttpSession sess) {
        logger.entering("MainController","in getLogoutController");
        sess.invalidate();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }


    @PostMapping("/authenticate")
    public ModelAndView userLogin(@ModelAttribute User user, HttpSession sess) {
        logger.entering("MainController","in UserController");

        ModelAndView mav = new ModelAndView();

        boolean login = false;
        try {
            login = userSvc.userLogin(user);

            //not successful
            if (!login) {
                mav.addObject("message",
                        "Incorrect username or password. Please try again.");
                mav.setStatus(HttpStatus.UNAUTHORIZED);
                mav.setViewName("loginfailure");
                return mav;
            }

            //redirect to the protected page
            sess.setAttribute("username",user.getUsername());
            mav = new ModelAndView("redirect:/protected/login/success");

        } catch (UserLoginException ex) {
            logger.severe("login failure");
            mav.addObject("message", "Error: %s".formatted(ex.getReason()));
            mav.setStatus(HttpStatus.UNAUTHORIZED);
            mav.setViewName("loginfailure");
            ex.printStackTrace();
            return mav;

        }

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView showRegistration() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        mav.setStatus(HttpStatus.OK);
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute User user) {

        ModelAndView mav = new ModelAndView();

        //check if username already exists
        Optional<User> optUser = userSvc.findUserByUsername(user.getUsername());

        mav.setStatus(HttpStatus.CREATED);
        mav.setViewName("registerResult");

        //should not create if username already exists
        if (optUser.isPresent()) {
            logger.severe("Username already exists. Cannot create user.");
            mav.addObject("msg","Username already exists. Please try again.");
            mav.addObject("isAdded", false);
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return mav;
        }

        try {
            userSvc.createUser(user.getUsername(),user.getPassword(),user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getGender());

            mav.addObject("isAdded",true);
            mav.addObject("username", user.getUsername());
        } catch (Exception e) {
            logger.severe("Unable to create user");
            mav.addObject("isAdded", false);
            mav.addObject("msg","Unable to create account. Please try again.");
            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return mav;
    }

}
