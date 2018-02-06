package cn.MQ.test;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

//这是测试mq的测试类
public class QueueTest {
   /** 
    * 单点模式的消息的提供者测试   
 * @throws JMSException 
    */
	@Test
	public void QueueTest() throws JMSException{
		//第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//	第二步：使用ConnectionFactory对象创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		//第三步：开启连接，调用Connection对象的start方法。
		connection.start();
		//第四步：使用Connection对象创建一个Session对象,需要注意的是我们一般设置的是自动应答，但是还可以试着手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象
		Queue queue = session.createQueue("test-queue");
		//第六步：使用Session对象创建一个Producer对象(提供消费的对象)。
		MessageProducer producer = session.createProducer(queue);
		//第七步：通过session对象创建一个Message对象，创建一个TextMessage对象。
		Message message= session.createTextMessage("this is queue test");
		//第八步：使用Producer对象发送消息。
		producer.send(message);
		//第九步：关闭资源。
		producer.close();
		connection.close();
		session.close();
		
	}
        @Test
       public void TocpicTest() throws JMSException{
        	//创建一个指定IP地址和端口号的connectionFaction对象
        	ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        	//创建一个connection对象
        	Connection connection = connectionFactory.createConnection();
        	//开启连接
        	connection.start();
        	//创建一个session对象
        	Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        	//创建一个topic对象
        	Topic topic = session.createTopic("test-topic");
        	//创建一个消息对象 
        	 TextMessage textMessage = session.createTextMessage("this is topic test----ack");
        	//创建一个消息的提供者
        	 MessageProducer producer = session.createProducer(topic);
        	 //发送信息
        	 producer.send(textMessage);
        	 //关闭资源
        	 session.close();
        	 connection.close();
        	 producer.close();
        }
	@Test
	public void consumerTest() throws Exception{
		//创建一个connectionFaction对象,指定端口和地址
		ConnectionFactory connectionFactory  = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	//创建一个queue对象
		Queue queue = session.createQueue("test-queue");
		//创建一个consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		//设置一个监听器
		consumer.setMessageListener(new MessageListener(){
			@Override
			public void onMessage(Message message) {
				//强制转换为TestMessage
			TextMessage textMessage =(TextMessage)message;
			try {
				System.out.println(textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
			}});
		//系统阻塞
		try {
			System.out.println("系统启动监听了");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//释放资源
		System.out.println("系统结束了哦");
		consumer.close();
		session.close();
		connection .close();
	}
	@Test
	public void topicConsumerTest5() throws Exception {
		//创建一个connectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//创建connection对象
		  Connection connection = connectionFactory.createConnection();
			//注意这里设置一个clentId 这是持久化的第一步,这个id可以自定义。但是不能重复
			connection.setClientID("clientId2");
			//开启连接
			connection.start();
			//创建session对象
		final Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			//创建一个topic对象
		Topic topic = session.createTopic("test-topic");
			//需要注意的是这里就必须创建一个持久化的消费者
		MessageConsumer consumer = session.createDurableSubscriber(topic, "test11");
			//设置监听器
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				// 8、在监听器中接收消息。并打印消息
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
					int i = 1/0;
					//手动应答服务器
					message.acknowledge();
				} catch (JMSException e) {
					e.printStackTrace();
					//发生异常让服务器重新发送消息
					try {
						session.recover();
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			/*
				// 转换为文本消息
				TextMessage textMessage =(TextMessage)message;
				try {
					System.out.println(textMessage.getText());
                   int i=1/0;					//如果处理成功手动应答
					message.acknowledge();
				} catch (JMSException e) {
					e.printStackTrace();
						try {
							session.recover();
						} catch (JMSException e1) {
							e.printStackTrace();
						}
				}
				
			*/}
		});
			System.out.println("消费者3 启动了");
			//系统阻塞
			System.in.read();
	System.out.println("系统关闭了");		
	System.out.println("消费者关闭。。。");
	// 10、关闭资源
	consumer.close();
	session.close();
	connection.close();	
	
		
	}
	@Test
	public void testDurableTopicConsumerClientAckNowledge() throws Exception {
		// 1、创建一个ConnectionFactory对象。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		// 2、使用ConnectionFactory对象获得一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//持久化订阅需要设置一个clientid
		connection.setClientID("clientId1");
		// 3、开启连接
		connection.start();
		// 4、使用Connection对象创建一个Session对象
		//设置手动应答模式
		final Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		// 5、使用Session创建一个Destnation对象，queue、topic，现在应该使用topic
		Topic topic = session.createTopic("test-topic");
		// 6、基于destnation对象创建一个consumer对象
		//MessageConsumer consumer = session.createConsumer(topic);
		MessageConsumer consumer = session.createDurableSubscriber(topic, "test11");
		// 7、设置一个监听器，等待服务端推送消息。
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// 8、在监听器中接收消息。并打印消息
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
					int i = 1/0;
					//手动应答服务器
					message.acknowledge();
				} catch (Exception e) {
					e.printStackTrace();
					//发生异常让服务器重新发送消息
					try {
						session.recover();
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		// 9、系统阻塞
		System.out.println("消费者3已经启动。。。。");
		System.in.read();
		System.out.println("消费者关闭。。。");
		// 10、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
