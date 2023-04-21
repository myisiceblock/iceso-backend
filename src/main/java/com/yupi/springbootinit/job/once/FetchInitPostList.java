package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.esdao.PostEsDao;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获得初始数据
 */
// 取消注释后，每次启动 SpringBoot 项目时会执行一次 run 方法
@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 1. 获取数据
        String json = "{\"current\":1,\"pageSize\":10,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url).body(json).execute().body();

        // 2. Json 转对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        log.info("FetchInitPostList start -------- total {}", postList.size());
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1647221078658940930L);
            postList.add(post);
        }
        // 3. 数据入库
        boolean saveBatch = postService.saveBatch(postList);
        if(saveBatch) {
            log.info("FetchInitPostList end -------- total: {}", postList.size());
        }  else {
            log.error("FetchInitPostList end -------- error");
        }
    }
}
