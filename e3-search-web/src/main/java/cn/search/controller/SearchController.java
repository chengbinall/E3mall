package cn.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.pojo.SeachItem;
import cn.e3mall.pojo.SearchResult;
import cn.e3mall.service.SearchItemService;

/**
 * 这是收搜的controller
 * @author 98448
 *
 */
@ Controller
public class SearchController {
	@Value("${search.rows}")
	private int rows;
     @Autowired
     private SearchItemService   searchItemService;
     @RequestMapping("/search")
     public String search(@RequestParam(defaultValue="1")Integer page,@RequestParam(defaultValue="") String keyword,Model model ) throws Exception{
      //处理乱码问题
    String	 keywords=new String(keyword.getBytes("iso8859-1"),"utf-8");
    	 SearchResult<SeachItem> result= searchItemService.search(page,keywords,rows);
    	 model.addAttribute("page", page);
    	 model.addAttribute("query", keywords);
    	 model.addAttribute("totalPages", result.getTotalPages());
    	 model.addAttribute("recourdCount", result.getRecourdCount());
    	 model.addAttribute("itemList", result.getItemList());
    	 return "search";
     }
	
	
	
	
	
}
