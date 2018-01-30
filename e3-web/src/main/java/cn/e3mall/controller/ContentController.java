package cn.e3mall.controller;
/**
 * 商品内容的controller
 * @author 98448
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.protal.service.ContentService;
@Controller
public class ContentController{
@Autowired
private ContentService contentService;
/**
 * 通过分类id查询商品内容的集合，并且分页显示
 * @param page
 * @param rows
 * @param categoryId
 * @return
 */
 @RequestMapping("/content/query/list")
 @ResponseBody
public DataGridResult  findContentbyCategoryId(Integer page ,Integer rows ,Long categoryId){
		//调用服务层
		DataGridResult dataGridResult=contentService.findContentbyCategoryId(page,rows,categoryId);
	return dataGridResult;

}
}
