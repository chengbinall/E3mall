package cn.e3mall.serviceImpl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	@Override
	public TbItem findItmById(long itmId) {
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itmId);
		return tbItem;
	}
//这里使用分页mybatis的分页插件哦。
	@Override
	public DataGridResult findItmList(Integer page, Integer rows) {
		//设置分页的数据
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example= new TbItemExample();
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
		long itemId = IDUtils.genItemId();
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
		if(insertSelective!=1||insert!=1){
			return 1;
		}
		return 0;
		
		
	}
}
