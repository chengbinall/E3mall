package cn.e3.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
/**
 * 这是监听的topic消息的。
 * @author 98448
 *
 */
public class myMessageListener  implements MessageListener{

	@Override
	public void onMessage(Message message) {
		//转换message为文本的message
		TextMessage textMessage = (TextMessage)message;
		System.out.println(textMessage);
		//手动应答
		
		
	}

}
