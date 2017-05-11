package cn.cherish.blog.web;

import cn.cherish.blog.dal.entity.Article;
import cn.cherish.blog.service.ArticleService;
import cn.cherish.blog.util.CheckMobile;
import lombok.extern.slf4j.Slf4j;
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
			@RequestParam(required = false,defaultValue = "20") Integer pageSize
			, HttpServletRequest request) {
	    //&startIndex=0&pageSize=20
		ModelAndView modelAndView = new ModelAndView("blog/list");
		if (CheckMobile.check(request.getHeader( "USER-AGENT" ).toLowerCase())) {
			modelAndView.setViewName("mobile/list");//手机端页面
		}

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
    public String  detailA(@RequestParam Long id) {

        return "redirect:/blog/" + id;//跳转到下面那个路径
    }

	@GetMapping("/{articleId}")
	public ModelAndView detail(@PathVariable Long articleId, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("blog/detail");
        if (CheckMobile.check(request.getHeader( "USER-AGENT" ).toLowerCase())) {
            modelAndView.setViewName("mobile/detail");//手机端页面
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
