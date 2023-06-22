//package top.sudk.util;
//
//
//import org.springframework.stereotype.Component;
//import org.aspectj.lang.annotation.*;
//import org.aspectj.lang.JoinPoint;
//
// 回滚测试2！！！
//@Aspect
//@Component
//public class DynamicAspect {
//
//
//    @Before("execution(* top.sudk.service.impl.BookServiceImpl.list(..))")
//    public void printParam(JoinPoint joinPoint) {
//        System.out.println("DynamicAspect printParam run !!!!");
//        Object[] args = joinPoint.getArgs();
//        for (Object arg : args) {
//            System.out.println(arg);
//        }
//    }
//
//    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)")
//    public void addLog() {
//        System.out.println("DynamicAspect addLog run !!!!");
//    }
//
//}
