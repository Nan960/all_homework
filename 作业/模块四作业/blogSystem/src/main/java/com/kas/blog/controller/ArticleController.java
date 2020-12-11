package com.kas.blog.controller;

import com.kas.blog.pojo.Article;
import com.kas.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping("/getList")
    public ModelAndView getList(Integer pageNum){
        ModelAndView mv = new ModelAndView();
        if (pageNum == null){
            pageNum = 0;
        }
        Integer pageSize = 2;
        Page<Article> pageInfo = articleService.getList(pageNum, pageSize);
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("client/index");
        return mv;
    }
}
