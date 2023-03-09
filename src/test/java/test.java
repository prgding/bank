import me.ding.service.AccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class test {
	@Test
	public void testSelect() {
		// Load Spring configuration file
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		AccountService accountService = applicationContext.getBean("accountService", AccountService.class);
		try {
			accountService.transfer("act001","act002",10000);
			System.out.println("转账成功");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
