package com.ssg.jdbcex.todo.controller;

import lombok.extern.log4j.Log4j2;
import com.ssg.jdbcex.todo.dto.TodoDTO;
import com.ssg.jdbcex.todo.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "todoReadController", value = "/todo/read")
@Log4j2
public class TodoReadController extends HttpServlet {

    private TodoService todoService = TodoService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // tno를 눌러서 해당하는 글을 조회
        try {
            Long tno = Long.parseLong(req.getParameter("tno"));

            TodoDTO todoDTO = todoService.get(tno);

            //모델 담기
            req.setAttribute("dto", todoDTO);

            // cookie 찾기 : 브라우저에서 전송된 쿠키가 있는지 확인한다는 뜻
            // 있다면 활용하고 없다면 내가 사용자 정의 쿠키를 만들겠다 -> findCookie 메서드의 내용
            Cookie viewTodoCookie = findCookie(req.getCookies(), "viewTodos");
            // 내가 방문했던 투두 목록을 쿠키값으로 저장하겠다
            String todoListStr = viewTodoCookie.getValue();

            boolean exists = false;
            if (todoListStr != null && todoListStr.indexOf(tno + "-") >= 0) {
                exists = true;
            }

            log.info("exists:" + exists);

            if (!exists) {
                // (기존 방문기록에 대한) 쿠키가 존재하지 않는다면
                // (쿠키 자체가 존재하지 않는다는 것이 아님에 유의)
                todoListStr += tno+"-";
                // 쿠키 굽기 시작
                viewTodoCookie.setValue(todoListStr);
                // 유효기간 : 24시간
                viewTodoCookie.setMaxAge(60*60*24);
                viewTodoCookie.setPath("/");
                resp.addCookie(viewTodoCookie);
                // 쿠키 굽기 종료
            }

            req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req, resp);

        }catch(Exception e){
            log.error(e.getMessage());
            throw new ServletException("read error");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        Cookie cookie = null;

        if (cookies != null && cookies.length > 0) {
            for(Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    cookie = c;
                    break;
                }
            }
        }

        if (cookie == null) {
            // 쿠키의 이름 : 쿠키의 값
            cookie = new Cookie(cookieName, "");
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24);
        }

        return cookie;
    }
}

