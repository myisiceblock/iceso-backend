package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Post;
import javax.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 帖子服务测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class PostServiceTest {

    @Resource
    private PostService postService;

    @Test
    void searchFromEs() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setUserId(1L);
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        Assertions.assertNotNull(postPage);
    }

    /**
     * 根据title查询帖子表中的重复数据，并保留一个
     */
    @Test
    public void getPostByTitle() {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("title").groupBy("title").having("count(*)>1");
        List<Post> postList = postService.list(queryWrapper);
        // 根据title删除帖子表中的多余重复数据并且保留一条
        postList.forEach(post -> {
            QueryWrapper<Post> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("title", post.getTitle());
            queryWrapper1.orderByDesc("createTime");
            List<Post> posts = postService.list(queryWrapper1);
            posts.remove(0);
            posts.forEach(post1 -> {
                postService.removeById(post1.getId());
            });
        });
        System.out.println(postList);
    }

}