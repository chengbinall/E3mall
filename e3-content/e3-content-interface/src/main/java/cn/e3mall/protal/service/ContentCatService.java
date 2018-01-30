package cn.e3mall.protal.service;

import java.util.List;

import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TreeNode;

public interface ContentCatService {

	List<TreeNode> getContentCategoryList(Long perentId);

	E3Result addContentCat(Long parentId, String name);

	E3Result updateContentCategory(Long id, String name);

	E3Result deleteContentCategory(Long id);

}
