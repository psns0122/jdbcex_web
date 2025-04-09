package com.ssg.jdbcex.todo.controller;

import com.ssg.jdbcex.todo.dto.MemberDTO;
import com.ssg.jdbcex.todo.service.MemberService;
import lombok.extern.java.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/login")
@Log
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login get .......");
        req.getRequestDispatcher("/WEB-INF/todo/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login post .......");

        // 서버에서 패킷으로 전달받은 리퀘스트에서 데이터를 낚아챔
        String mid = req.getParameter("mid");
        String mpwd = req.getParameter("mpwd");
        String auto = req.getParameter("auto"); // 체크박스는 on 이라는 값을 전달한다

        boolean rememberMe = auto != null && auto.equals("on");

        // 로그인 성공시 투두 리스트 조회 가능
        // String str = mid + mpwd;

        try {
            // 멤버 서비스 처리
            MemberDTO memberDTO = MemberService.INSTANCE.login(mid, mpwd);

            // rememberMe가 체크된 UUID를 생성해서 DB에 저장
            if (rememberMe) {
                String uuid = UUID.randomUUID().toString();
                MemberService.INSTANCE.updateUuid(mid, uuid);
                memberDTO.setUuid(uuid);

                // 브라우저에게 remember-me 로 정보를 전달할 쿠키를 생성
                Cookie rememberCookie = new Cookie("remember-me", uuid);
                rememberCookie.setPath("/");
                rememberCookie.setMaxAge(60*60*24*7);
                resp.addCookie(rememberCookie);
            }
            
            HttpSession session = req.getSession();
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect(req.getContextPath() + "/todo/list");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/login?result=error");
        }
    }
}
