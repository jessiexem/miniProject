package sg.nus.iss.demoPAF.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;

        //Get the HTTP Session
        HttpSession session = httpReq.getSession();
        String username = (String) session.getAttribute("username");

        System.out.printf(">>>>>> url: %s\n", httpReq.getRequestURI());
        System.out.printf(">>>> \t name: %s\n", username);

        //redirect to home page if user is not authenticated or alr logged out.
        if ((null == username) || (username.trim().length() <= 0)) {
            httpResp.sendRedirect("/");
            return;
        }


        //forward it
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
