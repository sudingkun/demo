package top.sudk.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import top.sudk.bean.Book;
import top.sudk.bean.RequestDataHelper;
import top.sudk.mapper.BookMapper;
import top.sudk.service.BookService;

import java.util.List;

/**
 * @author we
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    @Override
    public List<Book> list(String userName) {
        RequestDataHelper.set(userName);
        List<Book> list = bookMapper.list();
        RequestDataHelper.remove();
        return list;
    }



    @Override
    @Scheduled(cron = "0/5 * * * * ?")
    public void cron() {
        log.info("book size {}", bookMapper.count());
    }
}
