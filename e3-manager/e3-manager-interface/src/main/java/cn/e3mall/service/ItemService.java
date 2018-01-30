package cn.e3mall.service;

import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

public	TbItem findItmById(long itmId);

public DataGridResult findItmList(Integer page, Integer rows);

public Integer  save(String desc, TbItem tbItem) ;

public TbItemDesc findItemDesc(Long itemId);

public Integer update(String desc, TbItem tbItem);

public E3Result delete(String ids);

}
