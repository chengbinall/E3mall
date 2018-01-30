package cn.e3mall.protal.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import cn.e3mall.protal.service.ContentService;
@Service
public class ContentServiceImpl implements ContentService {
@Autowired
private TbContentMapper  tbContentMapper;
	@Override
	public DataGridResult findContentbyCategoryId(Integer page, Integer rows, Long categoryId) {
		   //1 创建mybatis提供的分页对象
		  PageHelper  pageHelper = new PageHelper();
		  // 2  设置分页需要的参数
		  pageHelper.startPage(page, rows);
		  //创建查询对象
		  TbContentExample  example= new TbContentExample();
		  Criteria criteria = example.createCriteria();
		  //封装查询条件
		  criteria.andCategoryIdEqualTo(categoryId);
		  // 3  执行查询 ，返回的是  list<tbContent>
		    List<TbContent> list = tbContentMapper.selectByExample(example);
		    PageInfo<TbContent>pageInfo = new PageInfo<>(list);
		  // 4  创建DataGridResult  对象
		    DataGridResult dataGridResult= new DataGridResult();
		  // 5  封装返回页面需要的参数的值
		    dataGridResult.setRows(list);
		    //获得总记录数
		    Long total = pageInfo.getTotal();
		    dataGridResult.setTotal(total.intValue());
		 // 6  返回封装好的结果集。
		return dataGridResult;
	}

}
