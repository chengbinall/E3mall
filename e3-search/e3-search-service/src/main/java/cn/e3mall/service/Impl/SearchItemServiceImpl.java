package cn.e3mall.service.Impl;


import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.dao.SearchDao;
import cn.e3mall.mapper.SearchItemMapper;
import cn.e3mall.pojo.Constant;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.SeachItem;
import cn.e3mall.pojo.SearchResult;
import cn.e3mall.service.SearchItemService;
@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	  private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	@Autowired
	private SearchDao searchDao;
	@Override
	public E3Result importItem() {
		  try {
			// 1 .首先查询出，我们需要导入到索引库里面的域字段的值。
List<SeachItem> items=	searchItemMapper.getSearItemList();
			  // 2   通过spring容器获取solrservice对象
if(items!=null&&items.size()>0){
//  3 遍历查询出的集合，依次创建SolrInputDocument对象，然后依次添加域字段   和值
	for (SeachItem seachItem : items) {
		//创建文本对象
		SolrInputDocument document = new SolrInputDocument();
		//依次个域字段赋值
		document.addField("id", seachItem.getId());
		document.addField("item_category_name", seachItem.getCategory_name());
		document.addField("item_image", seachItem.getImage());
		document.addField("item_price", seachItem.getPrice());
		document.addField("item_sell_point", seachItem.getSell_point());
		document.addField("item_title", seachItem.getTitle());
		//提交到索引库。
		solrServer.add(document);
		solrServer.commit();
	}
	//返回状态值200
	return E3Result.ok("1111");		
}
//没有查询到数据就直接返回失败的状态值
return E3Result.build(Constant.RESULT_STATUS_0, "添加失败了");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				solrServer.rollback();
			}  catch (Exception e1) {
				e1.printStackTrace();
			}
			return E3Result.build(Constant.RESULT_STATUS_0, "添加失败了");
		}
	}
	@Override
	public SearchResult<SeachItem> search(Integer page, String keyword, int rows) throws Exception {
	     // 创建query对象
		SolrQuery query = new SolrQuery();
	    //封装查询条件。包括分页的数据
		query.setQuery(keyword);
		if(page<1){
			page=1;
		}
		//设置分页起始页
		query.setStart((page-1)*rows);
		//设置每页显示的记录数
		query.setRows(rows);
		//设置默认查询的域字段
		query.set("df", "item_title");
	     //设置高亮显示的字段和开启高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		//设置高亮前缀
		query.setHighlightSimplePre("<em style=\"color:red\">");
	    // 设置后缀
		query.setHighlightSimplePost("</em>");
	   //开始查询
	SearchResult<SeachItem>result= searchDao.query(query);
	 int totalPage= result.getRecourdCount()/rows;
	 if(result.getRecourdCount()%rows>0){
		 totalPage++; 
	 }
	//封装数据到
	result.setTotalPages(totalPage);
	 //返回数据
		return result;
	}
}
