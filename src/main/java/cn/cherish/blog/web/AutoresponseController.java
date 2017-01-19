package cn.cherish.blog.web;

import cn.cherish.blog.dto.BasicSearchDto;
import cn.cherish.blog.entity.Autoresponse;
import cn.cherish.blog.services.AutoresponseService;
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
@RequestMapping("/autoresponse")
public class AutoresponseController extends ABaseController {

    @Autowired
    private AutoresponseService autoresponseService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("autoresponse/list");
        return modelAndView;
    }

    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> list(BasicSearchDto basicSearchDto) {

        try {
            Map<String, Object> searchParams = new HashMap<>();
            Page<Autoresponse> autoresponses = autoresponseService.searchAllByPageSort(
                    //Map<String, Object> searchParams, int pageNumber, int pageSize, String sortType
                    searchParams,basicSearchDto.getPageNumber(),basicSearchDto.getPageSize(),basicSearchDto.getOrderColumn());
            return getReturnMap(Boolean.TRUE,basicSearchDto.getDraw(),autoresponses);
        }catch (Exception e){
            log.error(e.getMessage());
            return getReturnMap(Boolean.FALSE,"系统繁忙",null);
        }
    }

    /**
     * 返回添加的页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/form")
    public ModelAndView add(){
        ModelAndView modelAndView = new ModelAndView("autoresponse/add");

        modelAndView.addObject("postUrl","/autoresponse/add");
        modelAndView.addObject("headTitle","添加新回复");
        return modelAndView;
    }

    /**
     * 附带数据的页面
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/form/{autoresponseId}")
    public ModelAndView update(@PathVariable Long autoresponseId){
        ModelAndView modelAndView = new ModelAndView("autoresponse/form");

        modelAndView.addObject("postUrl","/autoresponse/update");
        modelAndView.addObject("headTitle","修改回复");
        modelAndView.addObject(autoresponseService.findById(autoresponseId));
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ModelAndView add(Autoresponse autoresponse){

        ModelAndView modelAndView = new ModelAndView("redirect:/autoresponse");
        try {
            autoresponseService.save(autoresponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            modelAndView.setViewName("/form/");
            modelAndView.addObject("errorMsg", "反正有错");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ModelAndView update(Autoresponse autoresponse){
        ModelAndView modelAndView = new ModelAndView("redirect:/autoresponse");
        try {
            autoresponseService.update(autoresponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            modelAndView.setViewName("/form/"+autoresponse.getId());
            modelAndView.addObject("errorMsg", "反正有错");
        }
        return modelAndView;
    }

}