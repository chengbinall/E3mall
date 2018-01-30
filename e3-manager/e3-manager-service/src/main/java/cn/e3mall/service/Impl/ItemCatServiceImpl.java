package cn.e3mall.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;
@Service
public class ItemCatServiceImpl implements ItemCatService {
@Autowired
private TbItemCatMapper tbItemCatMapper;
	@Override
	public List<TreeNode> getIntemCatList(long perentId) {
		//封装查询条件
		//开始查询
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		 criteria.andParentIdEqualTo(perentId);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//判断是否还有子节点
		List<TreeNode>trList= new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			TreeNode node= new TreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			//需要注意的是没有子节点了就是open  如果有子节点就是closed  这里自动转换为布尔值了  0 是false  非0 的就是true哦
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			trList.add(node);
		}
		//返回数据
		return trList;
	}
}
