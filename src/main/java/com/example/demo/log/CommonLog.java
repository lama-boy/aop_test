package com.example.demo.log;

import java.time.LocalDateTime;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.cmnutil.BaseLogAop;
import com.example.demo.cmnutil.LoggingUtil;
import com.example.demo.dto.LogDto;

import jakarta.servlet.http.HttpServletRequest;


@Aspect
@Component
public class CommonLog extends BaseLogAop{

   @Autowired
   private HttpServletRequest request;
   
   
   @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) " +
           "&& execution(* com.example.demo..*.*(..)) ")
   public void adminGetMappingMethods() {}

   @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) && execution(* demo.controller..*.*(..))")
   public void adminPostMappingMethods() {}

   @Pointcut("execution(public * *insert*(..)) || execution(public * *Insert*(..))")
   public void insertMethods() {}

   @Pointcut("execution(public * *update*(..)) || execution(public * *Update*(..))")
   public void updateMethods() {}

   @Pointcut("execution(public * *delete*(..)) || execution(public * *Delete*(..))")
   public void deleteMethods() {}

   @Pointcut("execution(public * *login*(..))")
   public void loginMethods() {}


   @After("adminGetMappingMethods()")
   public void viewLog(JoinPoint joinPoint) {
	   System.out.println("adminGetMappingMethod");
       Map<String, Object> params = LoggingUtil.getMethodParams(joinPoint);
       logAction(joinPoint, "view", params, request);
   }

   @After("adminPostMappingMethods() && insertMethods()")
   public void insertLog(JoinPoint joinPoint) {
	   System.out.println("adminPostMappingMethod");
       Map<String, Object> params = LoggingUtil.getMethodParams(joinPoint);
       logAction(joinPoint, "insert", params, request);
   }

   @After("adminPostMappingMethods() && updateMethods()")
   public void updateLog(JoinPoint joinPoint) {
	   System.out.println("adminUpdateMethod");
       Map<String, Object> params = LoggingUtil.getMethodParams(joinPoint);
       logAction(joinPoint, "update", params, request);
   }

   @After("adminPostMappingMethods() && deleteMethods()")
   public void deleteLog(JoinPoint joinPoint) {
	   System.out.println("adminDeleteMappingMethod");
       Map<String, Object> params = LoggingUtil.getMethodParams(joinPoint);
       logAction(joinPoint, "delete", params, request);
   }

   @After("adminPostMappingMethods() && loginMethods()")
   public void loginLog(JoinPoint joinPoint) {
       Map<String, Object> params = LoggingUtil.getMethodParams(joinPoint);
       logAction(joinPoint, "login", params, request);
   }

   @Override
   protected void insertLog(Object logReq) {
       if (logReq instanceof LogDto) {
           //adminLogService.insertAdminLog((AdminLogReq) logReq);
           System.out.println(logReq.toString());
       } else {
           throw new IllegalArgumentException("Invalid log request type");
       }
   }

   @Override
   protected Object createLogReq(String action, String userId, LocalDateTime currentTime, Map<String, Object> params, HttpServletRequest request) {
       LogDto dto = new LogDto();
       dto.setJob_name(action);
       dto.setWrite_ip(request.getRemoteAddr());
       dto.setJob_url(request.getRequestURI());
       dto.setWrite_id(userId);
       dto.setWrite_time(currentTime);
       dto.setForm_value(params.toString());
       return dto;
   }
	
}
