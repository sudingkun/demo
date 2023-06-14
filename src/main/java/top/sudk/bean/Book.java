package top.sudk.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author we
 */

@Data
@TableName("book")
public class Book {

    private String isbn;
    private String bookName;
    private String press;
    private BigDecimal price;
    private BigDecimal discount;
    private String inventory;
    private String location;
    private LocalDateTime updateTime;

}
