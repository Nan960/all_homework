package com.kas.blog.service;

import com.kas.blog.pojo.Article;
import org.springframework.data.domain.Page;

public interface ArticleService {

    Page<Article> getList(Integer pageNum, Integer pageSize);
}
