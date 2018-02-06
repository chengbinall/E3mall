package cn.e3mall.mapper;

import java.util.List;

import cn.e3mall.pojo.SeachItem;

public interface SearchItemMapper {

	List<SeachItem> getSearItemList();
    SeachItem  findSearchItemById(Long itmId);
    SeachItem  findSearchItemByIdAndStats(Long itmId);
}
