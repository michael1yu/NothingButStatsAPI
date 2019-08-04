package com.michael.yu.nbawiki;

import com.michael.yu.nbawiki.servlets.*;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

@Configuration
public class WebConfig {

    @Bean
    public ServletRegistrationBean<HttpServlet> getPlayerStatsServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new GetPlayerStatsServlet());
        bean.addUrlMappings("/get_player_stats");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> refreshPlayersServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new RefreshPlayersServlet());
        bean.addUrlMappings("/refresh_players");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> refreshPlayerStatsServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new RefreshPlayerStatsServlet());
        bean.addUrlMappings("/refresh_player_stats");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> refreshTeamsServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new RefreshTeamsServlet());
        bean.addUrlMappings("/refresh_teams");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> queryCurrentPlayersServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new QueryCurrentPlayersServlet());
        bean.addUrlMappings("/query_current_players");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> getPlayerTeamServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new GetPlayerTeamServlet());
        bean.addUrlMappings("/get_player_team");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> getPlayerInfoServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new GetPlayerInfoServlet());
        bean.addUrlMappings("/get_player_info");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> getTeamsServlet(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new GetTeamsServlet());
        bean.addUrlMappings("/get_teams");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> checkBasicAuth(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new CheckAuthServlet());
        bean.addUrlMappings("/auth");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> pruneDatabase(){
        ServletRegistrationBean<HttpServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new PruneDatabaseServlet());
        bean.addUrlMappings("/prune_database");
        return bean;
    }
}
