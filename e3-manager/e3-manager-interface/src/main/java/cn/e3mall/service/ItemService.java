package cn.e3mall.service;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

public interface ItemService {

public	TbItem findItmById(long itmId);

public DataGridResult findItmList(Integer page, Integer rows);

public Integer  save(String desc, TbItem tbItem) ;

}
