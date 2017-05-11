package cn.cherish.blog.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Cherish on 2017/1/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchReq extends BasicSearchReq {

    private static final long serialVersionUID = 2431227415855670459L;

    private String title;
    private Long categoryId;

}
