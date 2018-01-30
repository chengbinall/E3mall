package cn.e3mall.controller;
/**
 * 商品的controller
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.Constant;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
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
			//添加成功了。
			 result.setStatus(Constant.SAVE_TBITEM_STATUS_1);
		}else{
			 result.setStatus(Constant.SAVE_TBITEM_STATUS_0);
		}
		return result;
 }
 /**
  * 查询商品描述的方法。
  */
 @RequestMapping("/query/item/desc")
 @ResponseBody
 public E3Result  findItemDesc(Long itemId){
	 E3Result result= new E3Result();
	 //调用服务层
	 if(itemId!=null){
		TbItemDesc itemDesc=itemService.findItemDesc(itemId);
		 result.setStatus(Constant.SAVE_TBITEM_STATUS_1);
		 result.setData(itemDesc);
		 return result;
	 }
	 //操作失败了。
	 result.setStatus(Constant.SAVE_TBITEM_STATUS_0);
	 return result;
 }
 /**
  * 修改商品的方法
  */
 @RequestMapping("/rest/item/update")
 @ResponseBody
 public E3Result update(String desc,TbItem tbItem){
	      E3Result result= new E3Result();
		Integer status= itemService.update(desc,tbItem);
		if(status==1){
			//添加成功了。
			 result.setStatus(Constant.SAVE_TBITEM_STATUS_1);
		}else{
			 result.setStatus(Constant.SAVE_TBITEM_STATUS_0);
		}
		return result;
 }
 /**
  * 删除商品的方法
  */
 @RequestMapping("/rest/item/delete")
 @ResponseBody
 public E3Result delete(String ids){
	 try {
		 E3Result result= itemService.delete(ids);
		 return result;
	} catch (Exception e) {
		E3Result result= new E3Result();
		//刪除商品失敗了
		result.setStatus(Constant.RESULT_STATUS_0);
		 return result;
	}
 }
}
