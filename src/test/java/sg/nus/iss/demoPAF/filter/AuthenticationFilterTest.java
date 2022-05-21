package sg.nus.iss.demoPAF.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import sg.nus.iss.demoPAF.filters.AuthenticationFilter;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationFilterTest {

    private final AuthenticationFilter filter = new AuthenticationFilter();

    @Test
    public void AuthenticationFilterTest() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        assertEquals("/", res.getRedirectedUrl());
    }
}
