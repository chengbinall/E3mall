package cn.e3mall.protal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//这是跳转到首页的controller
@Controller
public class ShowIndexController {
      //需要注意的是这里拦截的是这个路径是因为我们直接访问的时候，会默认跳转到index.html
	//而访问index.html的时候就会被这里拦截到，然后这个方法就跳转到index.jsp页面了哦。
	@RequestMapping("/*.html")
	public String showIndex(){
		return "index";
	}
}
