package cn.cherish.blog.web;

import cn.cherish.blog.dto.ArticleSearchDto;
import cn.cherish.blog.entity.Article;
import cn.cherish.blog.services.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/article")
public class ArticleController extends ABaseController{

	@Autowired
	private ArticleService articleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("article/list");
		return modelAndView;
	}

	@GetMapping("/list")
	@ResponseBody
	public Map<String, Object> list(ArticleSearchDto articleSearchDto) {

		try {
			Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("LIKE_title", articleSearchDto.getTitle());
            //searchParams.put("EQ_categoryId", articleSearchDto.getCategoryId());
            Page<Article> articles = articleService.searchAllByPageSort(
					//Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType
					searchParams,articleSearchDto.getPageNumber(),articleSearchDto.getPageSize(),articleSearchDto.getOrderColumn());
			return getReturnMap(Boolean.TRUE,articleSearchDto.getDraw(),articles);
		}catch (Exception e){
			log.error(e.getMessage());
			return getReturnMap(Boolean.FALSE,"系统繁忙",null);
		}

	}

	@GetMapping("/{articleId}")
	public ModelAndView detail(@PathVariable Long articleId) {

		ModelAndView modelAndView = new ModelAndView("article/form");

		Article article = articleService.findById(articleId);
		modelAndView.addObject(article);
		return modelAndView;
	}

    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{articleId}")
	public Map delArticle(@PathVariable Long articleId){

		articleService.delete(articleId);
		String msg = null;
		return getReturnMap(Boolean.TRUE, msg, null);
	}

	/**
	 * 返回添加文章的页面
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/form")
	public ModelAndView add(){
		ModelAndView modelAndView = new ModelAndView("article/add");

		modelAndView.addObject("postUrl","/article/add");
		modelAndView.addObject("headTitle","添加新文章");
		return modelAndView;
	}

	/**
	 * 附带文章数据的页面
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/form/{articleId}")
	public ModelAndView update(@PathVariable Long articleId){
		ModelAndView modelAndView = new ModelAndView("article/form");

		modelAndView.addObject("postUrl","/article/update");
		modelAndView.addObject("headTitle","修改文章");
		modelAndView.addObject(articleService.findById(articleId));
		return modelAndView;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ModelAndView add(Article article){

        ModelAndView modelAndView = new ModelAndView("redirect:/article");
        try {
            articleService.saveArticle(article);
        } catch (Exception e) {
            log.error(e.getMessage());
            modelAndView.setViewName("/form/");
            modelAndView.addObject("errorMsg", "反正有错");
        }
        return modelAndView;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/update")
	public ModelAndView update(Article article){
		ModelAndView modelAndView = new ModelAndView("redirect:/article");
		try {
			articleService.updateArticle(article);
		} catch (Exception e) {
			log.error(e.getMessage());
			modelAndView.setViewName("/form/"+article.getId());
			modelAndView.addObject("errorMsg", "反正有错");
		}
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
