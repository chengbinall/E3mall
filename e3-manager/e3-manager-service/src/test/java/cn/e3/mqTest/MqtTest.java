package cn.e3.mqTest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 这是测试spring和mq整合的测试类哦。
 * @author 98448
 *
 */
public class MqtTest {

	@Test
	public void topicTest(){
		//加载spring配置文件
ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-MQ.xml");
	//获得模板对象
    JmsTemplate  jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
    MessageCreator messageCreator= new MessageCreator() {
		@Override
		public Message createMessage(Session session) throws JMSException {
			TextMessage textMessage = session.createTextMessage("this is spring-mq test");
			return textMessage;
		}
	};
	//下面的这个对象就负责发送信息。
	jmsTemplate.send(messageCreator);
	}
}
