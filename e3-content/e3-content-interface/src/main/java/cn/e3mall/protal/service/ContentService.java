package cn.e3mall.protal.service;

import cn.e3mall.pojo.DataGridResult;

public interface ContentService {

	DataGridResult findContentbyCategoryId(Integer page, Integer rows, Long categoryId);

}
