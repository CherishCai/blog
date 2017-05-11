package cn.cherish.blog;

import cn.cherish.blog.service.AutoresponseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

	@Autowired
	private AutoresponseService autoresponseService;

	@Test
	public void contextLoads() {
		autoresponseService.findByKeyword("help");
	}

}
