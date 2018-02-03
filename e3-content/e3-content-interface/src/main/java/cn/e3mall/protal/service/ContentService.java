package cn.e3mall.protal.service;

import java.util.List;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	DataGridResult findContentbyCategoryId(Integer page, Integer rows, Long categoryId);

	E3Result saveContent(TbContent tbContent);

	E3Result updateContent(TbContent tbContent);

	E3Result deleteContent(String ids);
      /**
       * 通过商品内容分类的id 查询该分类下面的所有的对应的商品内容。
       * @param carouselContentCatId   
       * @return
       */
	List<TbContent> findTbcontentListByContentCatId(Long carouselContentCatId);


}
