package cn.e3mall.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.mapper.SearchItemMapper;
import cn.e3mall.pojo.SeachItem;
import cn.e3mall.pojo.TbItem;
/**
 * 这是接收消息，然后调用服务层进行同步索引库的监听类。
 * @author 98448
 *
 */
public class ItemMessageListener implements MessageListener {
	@Autowired 
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		 //将message转换为文本对象消息.
		TextMessage textMessage =(TextMessage)message;
		//延迟几秒
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			//通过文本对象，获得传递的消息
			String text = textMessage.getText();
			//将文本对象，通过逗号，切割。
			String[] strings = text.split(",");
			//通过传递过来的商品id查询出数据库里面的商品信息。
			Long itemId=Long.valueOf(strings[1]) ;
			 SeachItem seachItem = searchItemMapper.findSearchItemById(itemId);
			//判断是进行删除索引库还是添加索引库操作
			if(strings[0].equals("add")){
			//添加索引库的业务	
			if(seachItem!=null){
				//创建文本对象
				SolrInputDocument document = new SolrInputDocument();
				//依次个域字段赋值
				document.addField("id", seachItem.getId());
				document.addField("item_category_name", seachItem.getCategory_name());
				document.addField("item_image", seachItem.getImage());
				document.addField("item_price", seachItem.getPrice());
				document.addField("item_sell_point", seachItem.getSell_point());
				document.addField("item_title", seachItem.getTitle());
				//提交到索引库。
				solrServer.add(document);
				solrServer.commit();
			}
			}else{
				//删除索引库的业务，需要注意的是删除必须要谨慎，因此我们在查询条件里面是根据id和stats为3来查询的。
		SeachItem delItem = searchItemMapper.findSearchItemByIdAndStats(itemId);
		if(delItem!=null){
			solrServer.deleteById(delItem.getId());
			solrServer.commit();
		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
