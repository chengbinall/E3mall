package cn.e3mall.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {
     //注入mapper
	@Autowired
	public  TbItemMapper  tbItemMapper;

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
}
