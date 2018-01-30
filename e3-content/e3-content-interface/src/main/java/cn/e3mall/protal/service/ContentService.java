package cn.e3mall.protal.service;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	DataGridResult findContentbyCategoryId(Integer page, Integer rows, Long categoryId);

	E3Result saveContent(TbContent tbContent);

	E3Result updateContent(TbContent tbContent);

	E3Result deleteContent(String ids);

}
