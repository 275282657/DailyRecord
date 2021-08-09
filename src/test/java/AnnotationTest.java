import com.cgp.Springbootmain;
import com.cgp.dailyrecord.aop.main.AnnotationDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * @Author chenguopeng
 * @Date 2021/8/9 11:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Springbootmain.class)
public class AnnotationTest {
	@Autowired
	private AnnotationDemo annotationDemo;

	@Test
	public void test1() {
		annotationDemo.annotationDemo();
	}
}
