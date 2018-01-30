package cn.e3mall.protal.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.Constant;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import cn.e3mall.pojo.TreeNode;
import cn.e3mall.protal.service.ContentCatService;
@Service
public class ContentCatServiceImpl implements ContentCatService {
   @Autowired
   private TbContentCategoryMapper tbContentCategoryMapper;
	@Override
	public List<TreeNode> getContentCategoryList(Long perentId) {
		//1 创建查询对象
		TbContentCategoryExample  example = new TbContentCategoryExample();
		//2 将  perent_id 等于前台传递的id作为条件进行查询，而且查询的条件还要加上一个如果status必须是1 就表示没有删除
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(perentId);
		//查询的对象必须是没有逻辑删除的字段
		criteria.andStatusEqualTo(Constant.E3MALL_STATUS_1);
		//开启查询
		List<TbContentCategory> treeNodeList = tbContentCategoryMapper.selectByExample(example);
		//3 .遍历返回的结果的集合 ，依次判断是否是父节点，如果是就返回closed否则就返回open 
		 List<TreeNode> list = new  ArrayList<>();
		 for (TbContentCategory tbContentCat : treeNodeList) {
			      TreeNode treeNode = new TreeNode();
			      treeNode.setId(tbContentCat.getId());
			      treeNode.setText(tbContentCat.getName());
			      //closed表示不是叶子点，open表示没有子节点。
			      treeNode.setState(tbContentCat.getIsParent()?"closed":"open");
			      list.add(treeNode);
		}
		//4 返回结果集
		return list;
	}
	@Override
	public E3Result addContentCat(Long parentId, String name) {
		 //1 创建商品分类对象
		TbContentCategory tbContentCategory = new TbContentCategory();
	    // 2  补全该对象的参数
		tbContentCategory.setCreated(new Date());
		//需要注意的是我们默认设置该节点不是父节点 false 代表插入数据库就是0  
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setSortOrder(1);
	    //	1-正常，2-下架，3-删除
		tbContentCategory.setStatus(Constant.E3MALL_STATUS_1);
		tbContentCategory.setUpdated(new Date());
	   //  3  需要注意的是添加到数据库的时候，默认添加该节点就是一个子节点的，而且必须返回该节点的id ,因此在mapper里面必须修改插入语句
	  int insertSelective = tbContentCategoryMapper.insertSelective(tbContentCategory);
		// 4  通过penenId查询数据库，然后查询该节点是否是父节点，如果不是就将giant节点修改为父节点
	  TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(parentId);
	  //判断该节点的状态是否是父节点
	  if(!category.getIsParent()){
		  //如果不是就将该值修改为父节点.0 在数据库里面为false  非0 的都是true
		  category.setIsParent(true);
		  //然后将修改后的值更新到数据库里面
		  tbContentCategoryMapper.updateByPrimaryKeySelective(category);
	  }
	  
	   //  5  返回包含了该节点id的 E3Result对象
	  if(insertSelective==1){
		  //添加成功就返回状态200
		return  E3Result.ok(tbContentCategory);
	  }else{
		  //添加失败就返回状态0
		  E3Result result = E3Result.ok();
		  result.setStatus(Constant.RESULT_STATUS_0);
		  return result;
	  }
	}
	@Override
	public E3Result updateContentCategory(Long id, String name) {
		//创建一个商品分类对象，然后给name属性赋值。
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setName(name);
		tbContentCategory.setId(id);
		//然后调用更新的方法，需要注意的是这个调用的是判断了字段为null的情况的
		int i = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		//判断返回值是否是1  1 就表示修改成功了哦。
		if(i!=1){
			//修改不成功，返回的状态值为0 
			E3Result result = E3Result.ok();
			result.setStatus(Constant.RESULT_STATUS_0);
			return result;
		}
		//修改成功，返回状态值为200
		return E3Result.ok(tbContentCategory);
	}
	/**
	 * 需要注意的是我们这里是逻辑删除的方式。因此我们在查询的时候，就必须加上一个条件如果状态不是
	 */
	@Override
	public E3Result deleteContentCategory(Long id) {
		// 1  创建查询条件对象
		TbContentCategoryExample  example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		// 2  添加查询条件  ，即是perentId=id，并且状态是正常状态的
	     criteria.andParentIdEqualTo(id);
	     criteria.andStatusEqualTo(Constant.E3MALL_STATUS_1);
	     //3  开始查询数据库，获得该节点的子节点的集合
	     List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
	     //4   判断该子节点的集合 不能为空
	     if(list!=null&&list.size()>0){
	    	 // A  如果 不为空就遍历该集合通过tbContentCategory对象的id再次递归调用本方法。
	    	 for (TbContentCategory tbContentCategory : list) {
	    		 //递归调用
	    		 deleteContentCategory(tbContentCategory.getId());
			}
	    	 //将该父节点的状态设置为删除
	    	 TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
	    	 //设置状态为删除  3 
	    	 contentCategory.setStatus(Constant.E3MALL_STATUS_3);
	    	 // 5  修改数据库里面的该对象。
	    	 tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
	     }else{
	    	 // B  如果为空 表示该节点就是叶子点，修改此节点的状态为3 也就是逻辑删除
	    	 TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
	    	 //设置状态为删除  3 
	    	 contentCategory.setStatus(Constant.E3MALL_STATUS_3);
	    	 // 5  修改数据库里面的该对象。
	    	 tbContentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
	    	 // 6  创建查询条件对象，查询删除的这个对象的父节点的子节点的集合
	 		TbContentCategoryExample  example1 = new TbContentCategoryExample();
			Criteria criteria1 = example1.createCriteria();
		     criteria1.andParentIdEqualTo(contentCategory.getParentId());
		     criteria1.andStatusEqualTo(Constant.E3MALL_STATUS_1);
		   //7  开始查询数据库，获得子节点的集合
		     List<TbContentCategory> list1 = tbContentCategoryMapper.selectByExample(example1);
	    	//判断该集合是否为空  
		     if(list1==null||list1.size()==0){
	    		 //如果为空 就 修改父节点的状态为叶子点
	    		 TbContentCategory tbContentCategory= new TbContentCategory();
	    		 tbContentCategory.setId(contentCategory.getParentId());
	    		 tbContentCategory.setIsParent(false);
	    		 tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
	    	 }
	     }
		return E3Result.ok(id);
	}
}
