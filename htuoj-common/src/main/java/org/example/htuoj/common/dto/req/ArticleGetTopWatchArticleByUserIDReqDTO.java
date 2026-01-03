package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Article;

@Data
public class ArticleGetTopWatchArticleByUserIDReqDTO extends Page<Article> {

    private Long userId;
}
