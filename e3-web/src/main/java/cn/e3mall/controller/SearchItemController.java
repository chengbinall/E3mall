package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.service.SearchItemService;
/**
 * 同步收搜内容到索引库的类
 * @author 98448
 *
 */
@Controller
public class SearchItemController {
@Autowired
public  SearchItemService searchItemService;
@RequestMapping("/index/item/import")
@ResponseBody
public E3Result  importItems(){
	E3Result result=searchItemService.importItem();
	return result;
}
	
}
