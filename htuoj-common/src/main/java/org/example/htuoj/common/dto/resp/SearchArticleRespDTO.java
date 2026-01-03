package org.example.htuoj.common.dto.resp;

import lombok.Data;
import org.example.htuoj.common.dao.ArticleDoc;

import java.util.List;

@Data
public class SearchArticleRespDTO {
    private Integer total;
    private List<ArticleDoc> records;
}
