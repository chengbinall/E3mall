package cn.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 这是处理异常的类。
 * @author 98448
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
        private static final  Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handel,
			Exception e) {
		   //输出错误信息
		LOGGER.debug(e.getMessage());
		LOGGER.info("系统出现异常情况了哦");
		LOGGER.error("异常信息如下：" +e);
		//返回一个错误的页面
		ModelAndView mAndView = new ModelAndView();
		mAndView.setViewName("error/exception");
		return mAndView;
	}

}
