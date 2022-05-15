package sg.nus.iss.demoPAF.filters;

import org.springframework.stereotype.Component;
import sg.nus.iss.demoPAF.controller.MainController;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AuthenticationFilter implements Filter {

    private final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;

        //Get the HTTP Session
        HttpSession session = httpReq.getSession();
        String username = (String) session.getAttribute("username");

        logger.info(">>>>>> url: %s\n".formatted(httpReq.getRequestURI()));
        logger.info(">>>> \t name: %s\n".formatted(username));

        //redirect to home page if user is not authenticated or alr logged out.
        if ((null == username) || (username.trim().length() <= 0)) {
            httpResp.sendRedirect("/");
            return;
        }


        //forward it
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
