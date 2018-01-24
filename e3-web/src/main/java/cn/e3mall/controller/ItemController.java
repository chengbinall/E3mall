package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.DataGridResult;
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
 @RequestMapping("/item/list")
 @ResponseBody
 public  DataGridResult findItmList(Integer page ,Integer rows){
	 DataGridResult result=	 itemService.findItmList(page,rows);
  return result;	 
 }
}