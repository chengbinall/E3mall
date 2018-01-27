package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.utils.FastDFSClient;
import cn.e3mall.utils.JsonUtils;

/**
 * 这是通过fastDFS进行文件上传的controller
 * @author 98448
 *
 */
@Controller
public class FileuploadController {
	@Value("${image-service-url}")
	private String imgServieUrl;
	@RequestMapping(value="/pic/upload" ,produces="text/plan;charset=UTF-8")
	@ResponseBody
	public  String FileUpload(MultipartFile uploadFile){
		//需要注意的是下面的方式是直接读取配置文件里面的值
		Map map= new HashMap<>();
		try {
			//调用工具类继续上传图片到fastDFS
			FastDFSClient fastDFSClient= new FastDFSClient("classpath:/conf/Tracker.properties");
			//获得上传的文件名称的后缀
			String fileName=uploadFile.getOriginalFilename();
			String extName=fileName.substring(fileName.lastIndexOf(".")+1);
			//开始上传
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//补全返回的URL地址
			url= imgServieUrl+url;
			map.put("error", 0);
			map.put("url", url);
		} catch (Exception e) {
			map.put("error", 1);
			map.put("message", "上传图片失败了");
		}finally {
			String json = JsonUtils.objectToJson(map);
			return json;
		}
	}
}
