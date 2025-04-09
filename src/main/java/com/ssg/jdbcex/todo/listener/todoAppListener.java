package com.ssg.jdbcex.todo.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Log4j2
public class todoAppListener implements ServletContextListener {

    /// 서블릿 컨텍스트란 : 웹 어플리케이션 내에서 공통으로 사용하는 공용 메모리 공간
    /// 이 공간에 무언가를 저장하면 다같이 공용으로 사용할 수 있게 되는 것
    /// 전역값, 전역변수의 개념

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 어플리케이션이 시작되기 전에 처리하는 작업
        log.info("test");
        log.info("test");
        log.info("test");

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("appName", "todoService");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 어플리케이션이 종료되기 전에 처리하는 작업
        log.info("삭제 되기 전에 하는 일 -------------------------");
        log.info("삭제 되기 전에 하는 일 -------------------------");
        log.info("삭제 되기 전에 하는 일 -------------------------");
        log.info("삭제 되기 전에 하는 일 -------------------------");
        log.info("삭제 되기 전에 하는 일 -------------------------");
        log.info("삭제 되기 전에 하는 일 -------------------------");
    }
}
