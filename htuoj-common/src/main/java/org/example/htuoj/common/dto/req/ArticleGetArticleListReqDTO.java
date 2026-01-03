package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Article;

@Data
public class ArticleGetArticleListReqDTO extends Page<Article> {

    private Integer categoryId;

    private Long userId;
    private String searchContent;
    private Integer targetType;
}
