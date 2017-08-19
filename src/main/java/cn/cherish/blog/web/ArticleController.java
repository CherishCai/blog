package cn.cherish.blog.web;

import cn.cherish.blog.dal.entity.Article;
import cn.cherish.blog.service.ArticleService;
import cn.cherish.blog.web.req.ArticleSearchReq;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/article")
public class ArticleController extends ABaseController{

	private final ArticleService articleService;

	@Autowired
	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	/**
	 * 返回列表页面
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/list")
	public ModelAndView list() {
		return new ModelAndView("article/list");
	}
	/**
	 * 返回添加页面
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/add")
	public ModelAndView add(){
		return new ModelAndView("article/add");
	}
	/**
	 * 返回更新页面
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/update/{articleId}")
	public ModelAndView for_update(@PathVariable Long articleId) {
		ModelAndView modelAndView = new ModelAndView("article/edit");
		Article article = articleService.findById(articleId);
		modelAndView.addObject("article", article);
		return modelAndView;
	}

	@GetMapping("/page")
	@ResponseBody
	public Map<String, Object> list(ArticleSearchReq articleSearchDto) {
		try {
			Map<String, Object> searchParams = new HashMap<>();
			if (StringUtils.isNotBlank(articleSearchDto.getTitle())) {
				searchParams.put("LIKE_title", articleSearchDto.getTitle());
			}
			if (!Objects.isNull(articleSearchDto.getCategoryId())) {
				searchParams.put("EQ_categoryId", articleSearchDto.getCategoryId());
			}
			Page<Article> articles = articleService.findAllAndSort(
					searchParams, articleSearchDto.getPageNumber(), 
					articleSearchDto.getPageSize(), "id",
					Sort.Direction.DESC);

			return getReturnMap(Boolean.TRUE, articleSearchDto.getDraw(), articles);
		}catch (Exception e){
			log.error(Throwables.getStackTraceAsString(e));
			return getReturnMap(Boolean.FALSE,"系统繁忙",null);
		}

	}

	/**
	 * 删除
	 */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{articleId}")
	public Map delArticle(@PathVariable Long articleId){
		articleService.delete(articleId);
		return getReturnMap(Boolean.TRUE, "成功删除", null);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add")
	public ModelAndView add(Article article){
        ModelAndView modelAndView = new ModelAndView("redirect:/article/list");
        try {
            articleService.saveArticle(article);
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
        }
        return modelAndView;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/update")
	public ModelAndView update(Article article){
		ModelAndView modelAndView = new ModelAndView("redirect:/article/list");
		try {
			articleService.updateArticle(article);
		} catch (Exception e) {
			log.error(Throwables.getStackTraceAsString(e));
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
