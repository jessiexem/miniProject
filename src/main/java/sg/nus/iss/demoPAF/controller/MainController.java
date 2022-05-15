package sg.nus.iss.demoPAF.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.service.UserService;
import javax.servlet.http.HttpSession;
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
            logger.severe("login failure");
            mav.addObject("message", "Error: %s".formatted(ex.getReason()));
            mav.setStatus(HttpStatus.UNAUTHORIZED);
            mav.setViewName("loginfailure");
            ex.printStackTrace();
            return mav;

        }

        return mav;
    }

}
