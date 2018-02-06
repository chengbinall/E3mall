package cn.e3mall.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是返回收搜首页需要的参数的pojo类。
 * @author 98448
 *
 */
public class SearchResult<T>  implements Serializable{
private int 	totalPages ;
private int     recourdCount;
private List<T>itemList = new ArrayList<>() ;
public int getTotalPages() {
	return totalPages;
}
public void setTotalPages(int totalPages) {
	this.totalPages = totalPages;
}
public int getRecourdCount() {
	return recourdCount;
}
public void setRecourdCount(int recourdCount) {
	this.recourdCount = recourdCount;
}
public List<T> getItemList() {
	return itemList;
}
public void setItemList(List<T> itemList) {
	this.itemList = itemList;
}
}                            
