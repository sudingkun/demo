package top.sudk.controller;

import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import top.sudk.bean.Book;
import top.sudk.util.ScriptUtil;
import top.sudk.service.BookService;

import java.util.List;

/**
 * @author we
 */
@RestController
@RequestMapping("/script")
@RequiredArgsConstructor
public class TestController {

    private final ApplicationContext applicationContext;

    private final BookService bookService;


    @GetMapping("/book/list")
    public List<Book> list(@RequestParam String userName) {
        return bookService.list(userName);
    }




    @PutMapping("modifyBean")
    public void modifyBean() {
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        Object test = SpringUtil.getBean("bookServiceImpl");
        SpringUtil.unregisterBean("bookServiceImpl");

        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(test);
         proxyFactory.addAspect(ScriptUtil.createScriptClass());
        Object proxy = proxyFactory.getProxy();


        SpringUtil.registerBean("bookServiceImpl", proxy);
        defaultListableBeanFactory.applyBeanPostProcessorsAfterInitialization(proxy, "bookServiceImpl");
    }


}
