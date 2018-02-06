package cn.e3.consumer;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 这是测试topic消息的接收的测试类哦
 * @author 98448
 *
 */
public class TopicConsumerTest {

	@Test
	public void test(){
		//加载spring配置文件
ApplicationContext applicationContext =new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-MQ.xml");
   //系统阻塞
try {
	System.in.read();
} catch (IOException e) {
	e.printStackTrace();
}
	}
}
