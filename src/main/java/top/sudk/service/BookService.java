package top.sudk.service;

import top.sudk.bean.Book;

import java.util.List;

/**
 * @author we
 */
public interface BookService {

    /**
     * 获取列表
     *
     * @param userName 用户名
     * @return {@link List}<{@link Book}>
     */
    List<Book> list(String userName);


    void cron();

}
