package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Article;

@Data
public class ArticleGetArticleListByCategoryReqDTO extends Page<Article> {
    private Long problemId;
    private Long userId;
    private Integer categoryId;
}
