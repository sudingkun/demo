package top.sudk.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.sudk.bean.Book;

import java.util.List;

/**
 * @author we
 */
@Mapper
public interface BookMapper {


    /**
     * 列表
     *
     * @return {@link List}<{@link Book}>
     */
    List<Book> list();


    @Select("select count(*) from book")
    Long count();
}