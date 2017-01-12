package cn.cherish.blog.web;

import cn.cherish.blog.entity.Article;
import cn.cherish.blog.services.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/blog")
public class BlogController extends ABaseController{

	@Autowired
	private ArticleService articleService;

	@GetMapping
	public ModelAndView list(
	        @RequestParam(required = false,defaultValue = "") Integer title,
             @RequestParam(required = false,defaultValue = "0") Integer startIndex,
             @RequestParam(required = false,defaultValue = "20") Integer pageSize) {
	    //&startIndex=0&pageSize=20
		ModelAndView modelAndView = new ModelAndView("blog/list");

		try {
			Map<String, Object> searchParams = new HashMap<>();
            //searchParams.put("LIKE_title", title);
            //searchParams.put("EQ_categoryId", articleSearchDto.getCategoryId());
            Page<Article> articles = articleService.searchAllByPageSort(
					//Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType
					searchParams,startIndex/pageSize+1 , pageSize, null);
			modelAndView.addObject("articles",articles.getContent());
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return modelAndView;

	}

    @GetMapping("/detail")
    public ModelAndView detailA(@RequestParam Long id) {

        ModelAndView modelAndView = new ModelAndView("blog/detail");

        Article article = articleService.findById(id);
        modelAndView.addObject(article);
        return modelAndView;
    }

	@GetMapping("/{articleId}")
	public ModelAndView detail(@PathVariable Long articleId) {

		ModelAndView modelAndView = new ModelAndView("blog/detail");

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
