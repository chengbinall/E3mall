package cn.e3mall.controller;
//debug
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TreeNode;
import cn.e3mall.service.ItemCatService;

@Controller
public class ItemCatController {
@Autowired
private ItemCatService itemCatService;
 @RequestMapping("/item/cat/list")
 @ResponseBody
 public List<TreeNode> getIntemCatList(@RequestParam(value="id" ,defaultValue="0") long perentId){
	List<TreeNode> list= itemCatService.getIntemCatList(perentId);
	return list;
 }
}
