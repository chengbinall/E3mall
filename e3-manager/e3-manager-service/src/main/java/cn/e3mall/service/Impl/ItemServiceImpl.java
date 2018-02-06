package cn.e3mall.service.Impl;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.Constant;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import cn.e3mall.utils.IDUtils;
@Service
public class ItemServiceImpl implements ItemService {
     //注入mapper
	@Autowired
	public  TbItemMapper  tbItemMapper;
	@Autowired
	public  TbItemDescMapper  tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Override
	public TbItem findItmById(long itmId) {
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itmId);
		return tbItem;
	}
//这里使用分页mybatis的分页插件哦。
	@Override
	public DataGridResult findItmList(Integer page, Integer rows) {
		//判断是否为null，如果是null就设置默认值
		if(page==null){
			page=1;
		}
		if(rows==null){
			rows=30;
		}
		//设置分页的数据
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example= new TbItemExample();
		example.setOrderByClause("updated DESC ,created DESC");
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//获取分页的结果
		PageInfo<TbItem> info = new PageInfo<>(list);
		//获得查询出的总记录数。
		Long total = info.getTotal();
		DataGridResult result = new DataGridResult();
		result.setRows(list);
		result.setTotal(total.intValue());
		return result;
	}
	@Override
	public Integer save(String desc, TbItem tbItem){
		//生成商品的id，同过工具类生成
		final long itemId = IDUtils.genItemId();
		tbItem.setId(itemId);
		//手动添加商品里面没有缺少的参数值
		tbItem.setCreated(new Date());
		tbItem.setUpdated(new Date());
		tbItem.setStatus(Constant.TBITEM_STATUS_1);
		//添加商品到数据库,需要注意的是下面的插入是如果该值为null就不插入数据库的哦
		int insert = tbItemMapper.insertSelective(tbItem);
		//创建商品描述表需要的pojo
		TbItemDesc tbItemDesc =new TbItemDesc();
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setUpdated(new Date());
		//添加商品描述到数据库
		int insertSelective = tbItemDescMapper.insertSelective(tbItemDesc);
		if(insertSelective==1&&insert==1){
			//添加成功了哦
		//在返回之前通过mq方送一条消息。messageCreator
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage = session.createTextMessage("add,"+itemId);
					return textMessage;
				}
			});
			return 1;
		}
		return 0;
		
		
	}
	@Override
	public TbItemDesc findItemDesc(Long itemId) {
		//通过商品id查询商品描述信息
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		return itemDesc;
	}
	@Override
	public Integer update(String desc, TbItem tbItem) {
		//这里面的逻辑和添加商品的逻辑差不多的，但是主键id是已经存在的哦。只需要添加一个修改时间就可以了哦。
		//设置商品的修改时间
		tbItem.setUpdated(new Date());
		//修改商品表,需要注意的是这里的修改是如果上传的没有这个值就不修改数据库的该字段哦
		int updateItem = tbItemMapper.updateByPrimaryKeySelective(tbItem);
		//创建商品描述对象
		final TbItemDesc itemDesc = new TbItemDesc();
		//赋值参数
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(tbItem.getId());
		itemDesc.setUpdated(new Date());
		//修改商品描述表
		int updateBItemDesc = tbItemDescMapper.updateByPrimaryKeySelective(itemDesc);
		if(updateBItemDesc==1&&updateItem==1){
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage = session.createTextMessage("add,"+itemDesc.getItemId());
					return textMessage;
				}
			});
			return 1;
		}
		return 0;
	}
	/**
	 * 修改商品的狀態為刪除的方法哦
	 */
	@Override
	public E3Result delete(String ids) {
		//1  将传入的商品id的字符串通过逗号切割，
		String[] itmIds = ids.split(",");
        //2 遍历该數組，获得id。
		for (String id : itmIds) {
			TbItem  tbItem = new TbItem();
			tbItem.setId(Long.valueOf(id));
			//修改商品的狀態為刪除    商品状态，1-正常，2-下架，3-删除
			tbItem.setStatus(Constant.TBITEM_STATUS_3);
			tbItem.setUpdated(new Date());
			 // 4然后 通过调用itemMapper的updateByPrimaryKeySelective 方法因为这个sql语句里面只修改数据库里面的商品状态的 
			int selective = tbItemMapper.updateByPrimaryKeySelective(tbItem);
			if(selective!=1){
				//表示修改失敗了
				E3Result result= new E3Result();
				//刪除商品失敗了
				result.setStatus(Constant.RESULT_STATUS_0);
				return result;
			}
		}
		//返回的狀態值時200 表示刪除成功了,然后遍历这个集合，依次发送消息。。
		for (String id : itmIds) {
			final String itmId=id;
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage =session.createTextMessage("del,"+itmId);
					return textMessage;
				}
			});
		}
		
		return E3Result.ok(ids);
	}
}
