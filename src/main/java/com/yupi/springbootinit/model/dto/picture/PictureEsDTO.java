package com.yupi.springbootinit.model.dto.picture;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 图片 ES 包装类
 */
@Document(indexName = "picture")
@Data
public class PictureEsDTO {

    private String title;

    private String url;
}
