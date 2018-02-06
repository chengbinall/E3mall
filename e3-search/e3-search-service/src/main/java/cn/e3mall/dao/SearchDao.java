package cn.e3mall.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.e3mall.pojo.SeachItem;
import cn.e3mall.pojo.SearchResult;

public interface SearchDao {

	SearchResult<SeachItem> query(SolrQuery query) throws Exception;

}
