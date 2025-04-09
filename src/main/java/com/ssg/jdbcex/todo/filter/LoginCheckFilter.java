package com.ssg.jdbcex.todo.filter;

import com.ssg.jdbcex.todo.dto.MemberDTO;
import com.ssg.jdbcex.todo.service.MemberService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

// 해당 패키지의 모든 자원은 필터를 거쳐야만 접근 가능
@WebFilter(urlPatterns = {"/todo/*"})
@Log4j2
public class LoginCheckFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 일반 서블릿에서 제공하는 필터
        log.info("Login.........doFilter..");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 서버에게 세션 정보 요청
        HttpSession session = req.getSession();
        // 세션 정보가 존재한다면
        if (session.getAttribute("loginInfo") != null)
        {
            log.info("세션에 로그인 정보가 남아있음");
            chain.doFilter(request, response);
            return;
        }

        // session에 loginInfo가 없다면 서버에서 쿠키를 확인
        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        // 세션에도 없고 쿠키도 없다면 -> 로그인 유도
        if (cookie == null) {
            log.info("세션에 로그인 정보도, remember-me 쿠키 정보도 없는 사용자");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 쿠키가 존재하는 상황이라면
        log.info("........cookie가 있는 상황................");
        String uuid = cookie.getValue();

        try {
            MemberDTO memberDTO = MemberService.INSTANCE.getByUuid(uuid);
            log.info("쿠키값으로 조회한 사용자 정보: " + memberDTO);

            if (memberDTO == null) {
                throw new Exception("Cookie value is not valid.");
            }

            // 회원 정보를 세션에 추가
            session.setAttribute("loginInfo", memberDTO);
            chain.doFilter(request, response);


        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

    }

    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        // 쿠키를 이름으로 비교, 찾으면 첫번재 값을 가져와서 result 에 대입
        Optional<Cookie> result = Arrays.stream(cookies)
                                        .filter(c -> c.getName().equals(name)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

}
