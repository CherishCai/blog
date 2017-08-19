package cn.cherish.blog.web;

import cn.cherish.blog.dal.entity.Article;
import cn.cherish.blog.service.ArticleService;
import cn.cherish.blog.util.CheckMobile;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class BlogController extends ABaseController{

	private final ArticleService articleService;

    @Autowired
    public BlogController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping({"", "/list"})
    public ModelAndView list(
            @RequestParam(required = false) String search,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
            , HttpServletRequest request) {

        if (pageNumber < 1) pageNumber = 1;
        if (pageSize < 1) pageSize = 1;

        ModelAndView modelAndView = new ModelAndView("blog/list");
        if (CheckMobile.check(request.getHeader("USER-AGENT").toLowerCase())) {
            //手机端
            log.info("手机端访问");
        }

        try {
            Page<Article> page;
            if (StringUtils.isBlank(search)) {
                // 查询数据库
                Map<String, Object> searchParams = new HashMap<>();
                page = articleService.findAllAndSort(
                        searchParams, pageNumber, pageSize, null, null);
            } else {
                // 查询搜索引擎
                log.info("【查询搜索引擎】 search : {}", search);
                page = articleService.search(search, pageNumber, pageSize);
                modelAndView.addObject("search", search.trim());
            }
            modelAndView.addObject("articles", page.getContent());
            modelAndView.addObject("pageInfo", page);
        } catch (Exception e) {
            log.error("{}", Throwables.getStackTraceAsString(e));
        }
        return modelAndView;
    }

    @GetMapping("/detail")
    public String  detailA(@RequestParam Long id) {
        return "redirect:/blog/" + id; //跳转到下面那个路径
    }

	@GetMapping("/{articleId}")
	public ModelAndView detail(@PathVariable Long articleId, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("blog/detail");
        if (CheckMobile.check(request.getHeader( "USER-AGENT" ).toLowerCase())) {
            //手机端
			log.info("手机端访问");
        }

		Article article = articleService.findById(articleId);
		modelAndView.addObject(article);
		return modelAndView;
	}


	/**
	 * 类别下的所有文章，默认时间倒叙
	 */
	@GetMapping("/category/{categoryId}")
	public Map typeList(@PathVariable Long categoryId){

		return getReturnMap(Boolean.TRUE, null, null);
	}


}
