package cn.e3mall.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mchange.v2.async.StrandedTaskReporting;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TreeNode;
import cn.e3mall.protal.service.ContentCatService;
/**
 * 商品分类的controller
 * @author 98448
 *
 */
@Controller
public class ContentCatController {
    //注入service
	@Autowired
  private ContentCatService  contentCatService;
	/**
	 * 内容分类页面的Tree的查询
	 * @param perentId
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<TreeNode> getContentCategoryList(@RequestParam(defaultValue="0",name="id")Long perentId ){
	List<TreeNode>list=	contentCatService.getContentCategoryList(perentId);
	return list;
		
	}
	/**
	 * 添加商品内容分类的方法
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result   addContentCategory( Long parentId ,String name){
		E3Result result=contentCatService.addContentCat(parentId,name);
		return result ;
		
	}
	/**
	 * 重命名商品分类的名称
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateContentCategory(Long id ,String name){
		E3Result result=contentCatService.updateContentCategory(id,name);
		return result ;
	}
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3Result deleteContentCategory(Long id){
		E3Result result=contentCatService.deleteContentCategory(id);
		return result ;
	}
	
}
