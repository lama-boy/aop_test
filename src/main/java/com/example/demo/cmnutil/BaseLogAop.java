package com.example.demo.cmnutil;

import java.time.LocalDateTime;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public abstract class BaseLogAop {

    protected abstract void insertLog(Object logReq);

    protected abstract Object createLogReq(String action, String userId, LocalDateTime currentTime, Map<String, Object> params, HttpServletRequest request);
    
    @Autowired
    private HttpSession session;

    protected void logAction(JoinPoint joinPoint, String action, Map<String, Object> params, HttpServletRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();

        String userId = "Unknown";
        if(session.getAttribute("user_id") != null) {
        	userId = String.valueOf(session.getAttribute("user_id"));
        }

        Object logReq = createLogReq(action, userId, currentTime, params, request);

        try {
            insertLog(logReq);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
