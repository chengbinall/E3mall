package cn.e3mall.service;

import java.util.List;

import cn.e3mall.pojo.TreeNode;

public interface ItemCatService {

	List<TreeNode> getIntemCatList(long perentId);

}
