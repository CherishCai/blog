package cn.cherish.blog.web;

import cn.cherish.blog.util.ValidateCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class BasicController {

	@GetMapping({"/","/index"})
	public String index(){
		return "index";
	}

	/**
	 * 登陆页面
	 */
	@GetMapping(value = "/login")
	public String login(){
		return "login";
	}

	/**
	 * 执行登陆
	 */
	@PostMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request){
		log.info("【执行登陆】 ");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");

		String code = (String) request.getSession().getAttribute("validateCode");
		String submitCode = (String)request.getAttribute("validateCode");

		//判断验证码
		if (StringUtils.isEmpty(submitCode) || !StringUtils.equals(code,submitCode.toLowerCase())) {
			log.debug("验证码不正确");
			modelAndView.addObject("validateCodeError", "验证码不正确");
			return modelAndView;
		}

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		return modelAndView;
	}

	@GetMapping("/403")
	public String unauthorizedRole(){
		log.debug("------没有权限-------");
		return "403";
	}
	
	/**
	 * 生成验证码
	 */
	@GetMapping(value = "/validateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
		request.getSession().setAttribute("validateCode", verifyCode);
		response.setContentType("image/jpeg");
		BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
		ImageIO.write(bim, "JPEG", response.getOutputStream());
	}


	@PostMapping("/imageUpload")
    @ResponseBody
	public Map upload(@RequestParam("editormd-image-file") MultipartFile multipartFile, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>(5);
        map.put("success", 0);//0 | 1, // 0 表示上传失败，1 表示上传成功
        map.put("message", "提示的信息，上传成功或上传失败及错误信息等。");

        if (!multipartFile.isEmpty()) {
            File directory = new File("/cherish");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {
                String originalFilename = multipartFile.getOriginalFilename();

                String newFIleName = System.currentTimeMillis()//UUID.randomUUID().toString()
                        + originalFilename.substring(originalFilename.lastIndexOf("."));
                multipartFile.transferTo(new File(directory, newFIleName));
//				FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),
//				new File(directory,newFIleName));
                String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath() + "/";
                map.put("success", 1);
                map.put("message", "掂过碌蔗！");
                map.put("url", basePath+"imageDownload?filename="+newFIleName);
            } catch (IOException e) {
                e.printStackTrace();
                map.put("success", 0);
                map.put("message", "错误!");
            }

        } // end if
       return map;
    }

    @GetMapping("/imageDownload")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        File file = new File("/cherish", filename);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(
        		FileUtils.readFileToByteArray(file),
                headers, HttpStatus.OK);
        /*byte[] b = null;
        response.setContentType("image/png");

        try (FileInputStream fis = new FileInputStream(file);
            OutputStream out = response.getOutputStream();){

            b = new byte[2048];
            int len = 0;
            while ((len = fis.read(b)) > 0){
                out.write(b);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }*/
    }


}
