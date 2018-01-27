package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.Constant;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
//注入service
@Autowired
 public  ItemService itemService;
 @RequestMapping("/itm/{Itmid}")
 @ResponseBody
 public  TbItem findItmById(@PathVariable long Itmid){
	 TbItem tbItem= itemService.findItmById(Itmid);
  return tbItem;	 
 }
 /**
  * 查询商品列表的方法
  * @param page
  * @param rows
  * @return
  */
 @RequestMapping("/item/list")
 @ResponseBody
 public  DataGridResult findItmList(Integer page ,Integer rows){
	 DataGridResult result=	 itemService.findItmList(page,rows);
  return result;	 
 }
 /**
  * 添加商品的方法
  */
 @RequestMapping("/item/save")
 @ResponseBody
 public E3Result save(String desc,TbItem tbItem){
	      E3Result result= new E3Result();
		Integer status= itemService.save(desc,tbItem);
		if(status==1){
			 result.setStatus(Constant.SAVE_TBITEM_STATUS_1);
		}else{
			 result.setStatus(Constant.SAVE_TBITEM_STATUS_0);
		}
		return result;
 }
}
