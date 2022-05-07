package sg.nus.iss.demoPAF;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sg.nus.iss.demoPAF.filters.AuthenticationFilter;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> registerFilters() {

        //create an instance of AuthenticationFilter
        AuthenticationFilter authfilter = new AuthenticationFilter();

        //create an instance of Registration Filter
        FilterRegistrationBean<AuthenticationFilter> regFilter = new FilterRegistrationBean<>();

        regFilter.setFilter(authfilter);
        regFilter.addUrlPatterns("/protected/*");

        return regFilter;
    }
}
