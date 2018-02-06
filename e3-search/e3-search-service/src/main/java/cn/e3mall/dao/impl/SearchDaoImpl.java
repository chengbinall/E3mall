package cn.e3mall.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import cn.e3mall.dao.SearchDao;
import cn.e3mall.pojo.SeachItem;
import cn.e3mall.pojo.SearchResult;
@Repository
public class SearchDaoImpl implements SearchDao {
@Autowired 
private SolrServer solrServer;
	@Override
	public SearchResult<SeachItem> query(SolrQuery query) throws Exception {
		//开始查询索引库
		QueryResponse response = solrServer.query(query);
		//获得总商品数
		SolrDocumentList solrDocumentList = response.getResults();
		Long numFound =solrDocumentList.getNumFound();
		    int RecourdCount = numFound.intValue();
		List<SeachItem>items= new ArrayList<>();
		//获取商品列表
		for (SolrDocument solrDocument : solrDocumentList) {
			SeachItem seachItem = new SeachItem();
			seachItem.setId(solrDocument.get("id").toString());
			seachItem.setCategory_name(solrDocument.get("item_category_name").toString());
			seachItem.setImage(solrDocument.get("item_image").toString());
			seachItem.setPrice(Long.valueOf(solrDocument.get("item_price").toString()));
			seachItem.setSell_point(solrDocument.get("item_sell_point").toString());
			seachItem.setTitle(solrDocument.get("item_title").toString());
			//获取高亮显示的内容
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			  List<String> list = map.get(solrDocument.get("id")).get("item_title");
			  if(list!=null &&list.size()>0){
				  seachItem.setTitle(list.get(0));
			  }
			  items.add(seachItem);
		}
		//封装返回的数据
		SearchResult result= new SearchResult<>();
		result.setItemList(items);
		result.setRecourdCount(RecourdCount);
		return result;
	}

}
