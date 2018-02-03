package cn.e3mall.protal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.protal.service.ContentService;
//这是跳转到首页的controller
@Controller
public class ShowIndexController {
	//需要注意的是这个分类id必须和数据库的对应哦。下面表示的是首页轮播图这个分类id
	@Value("${protal.index.carousel.contentCatId}")
	private Long carouselContentCatId;
	@Autowired
	private ContentService contentService;
      //需要注意的是这里拦截的是这个路径是因为我们直接访问的时候，会默认跳转到index.html
	//而访问index.html的时候就会被这里拦截到，然后这个方法就跳转到index.jsp页面了哦。
	@RequestMapping("/index")
	public String showIndex(Model model){
		//需要注意的是返回的集合名称最好和页面的保持一致。下面这个方法，首页里面的每个模块依次调用就可以了。
	List<TbContent> ad1List=contentService.findTbcontentListByContentCatId(carouselContentCatId);
	model.addAttribute("ad1List", ad1List);
		return "index";
	}
}
