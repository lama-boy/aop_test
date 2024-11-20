package com.example.demo.cmnutil;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class LoggingUtil {
    public static Map<String, Object> getMethodParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(org.springframework.web.bind.annotation.RequestParam.class)) {
                org.springframework.web.bind.annotation.RequestParam requestParam = parameters[i].getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
                String paramName = !requestParam.name().isEmpty() ? requestParam.name() : parameters[i].getName();
                handleParamValue(params, paramName, args[i]);

            } else if (isPrimitiveOrWrapper(args[i].getClass()) || args[i] instanceof String) {
                String paramValue = args[i].toString();
                if (paramValue.length() > 500) {
                    params.put(parameters[i].getName(), "Data too long to log");
                } else {
                    params.put(parameters[i].getName(), args[i]);
                }
            } else {
                extractFields(params, args[i]);
            }
        }

        return params;
    }

    public static void handleParamValue(Map<String, Object> params, String paramName, Object arg) {
        if (arg == null) {
            params.put(paramName, "null");
        } else {
            String paramValue = arg.toString();
            if (paramValue.length() > 500) {
                params.put(paramName, "Data too long to log");
            } else {
                params.put(paramName, paramValue);
            }
        }
    }


    public static void extractFields(Map<String, Object> params, Object arg) {
        Field[] fields = arg.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName().toLowerCase();
                if (isSecretField(fieldName)) {
                    params.put(field.getName(), "********");  // 비밀번호 필드는 별표로 마스킹
                } else {
                    params.put(field.getName(), field.get(arg));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean isSecretField(String fieldName) {
        return fieldName.contains("password") ||
                fieldName.contains("pw") ||
                fieldName.contains("secret") ||
                fieldName.contains("token") ||
                fieldName.contains("key");
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Integer.class ||
                type == Long.class ||
                type == Short.class ||
                type == Double.class ||
                type == Float.class ||
                type == Boolean.class ||
                type == Character.class ||
                type == Byte.class;
    }

    public static String sanitizeUserAgent(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }

        StringBuilder sb = new StringBuilder();

        // 브라우저 정보 추출
        if (userAgent.contains("Edg/")) {
            sb.append("Edge ");
        } else if (userAgent.contains("Chrome") && !userAgent.contains("Edg/")) {
            sb.append("Chrome ");
        } else if (userAgent.contains("Firefox")) {
            sb.append("Firefox ");
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome") && !userAgent.contains("Edg/")) {
            sb.append("Safari ");
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            sb.append("IE ");
        } else {
            sb.append("Other ");
        }

        // 운영체제 정보 추출
        if (userAgent.contains("Windows")) {
            sb.append("Windows ");
        } else if (userAgent.contains("Mac OS X")) {
            sb.append("MacOS ");
        } else if (userAgent.contains("Linux")) {
            sb.append("Linux ");
        } else if (userAgent.contains("Android")) {
            sb.append("Android ");
        } else if (userAgent.contains("iOS")) {
            sb.append("iOS ");
        } else {
            sb.append("Other ");
        }

        // 모바일 여부
        if (userAgent.contains("Mobile")) {
            sb.append("Mobile");
        } else {
            sb.append("Desktop");
        }

        return sb.toString().trim();
    }
}
