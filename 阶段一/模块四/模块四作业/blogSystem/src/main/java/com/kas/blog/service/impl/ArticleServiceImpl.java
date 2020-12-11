package com.kas.blog.service.impl;

import com.kas.blog.dao.ArticleDao;
import com.kas.blog.pojo.Article;
import com.kas.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Page<Article> getList(Integer pageNum, Integer pageSize) {
        return articleDao.findAll(PageRequest.of(pageNum, pageSize));
    }
}
