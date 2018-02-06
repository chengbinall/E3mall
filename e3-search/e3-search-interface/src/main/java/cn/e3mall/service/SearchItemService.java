package cn.e3mall.service;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.SeachItem;
import cn.e3mall.pojo.SearchResult;

public interface SearchItemService {

	E3Result importItem();

	SearchResult<SeachItem> search(Integer page, String keyword, int rows) throws Exception;

}
