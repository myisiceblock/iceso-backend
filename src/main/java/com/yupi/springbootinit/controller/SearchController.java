package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.manager.SearchFacade;
import com.yupi.springbootinit.model.dto.search.SearchQueryRequest;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;

/**
 * 图片接口
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @Resource
    private PostService postService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchQueryRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }

    @GetMapping("/getSearchPrompt")
    public BaseResponse<List<String>> getSearchPrompt(String keyword) {
        return ResultUtils.success(postService.getSearchPrompt(keyword));
    }
}
