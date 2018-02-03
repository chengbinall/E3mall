package cn.e3mall.protal.service.Impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.jedis.JedisClient;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.Constant;
import cn.e3mall.pojo.DataGridResult;
import cn.e3mall.pojo.E3Result;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
import cn.e3mall.protal.service.ContentService;
import cn.e3mall.utils.JsonUtils;
@Service
public  class ContentServiceImpl implements ContentService {
@Autowired
private TbContentMapper  tbContentMapper;
@Autowired
private JedisClient jedisClient;
@Value("${redis.protal.key}")
private String hashKey;
	@Override
	public DataGridResult findContentbyCategoryId(Integer page, Integer rows, Long categoryId) {
		   //1 创建mybatis提供的分页对象
		  PageHelper  pageHelper = new PageHelper();
		  // 2  设置分页需要的参数
		  pageHelper.startPage(page, rows);
		  //创建查询对象
		  TbContentExample  example= new TbContentExample();
		  Criteria criteria = example.createCriteria();
		  //封装查询条件
		  criteria.andCategoryIdEqualTo(categoryId);
		  // 3  执行查询 ，返回的是  list<tbContent>
		    List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
		    PageInfo<TbContent>pageInfo = new PageInfo<>(list);
		  // 4  创建DataGridResult  对象
		    DataGridResult dataGridResult= new DataGridResult();
		  // 5  封装返回页面需要的参数的值
		    dataGridResult.setRows(list);
		    //获得总记录数
		    Long total = pageInfo.getTotal();
		    dataGridResult.setTotal(total.intValue());
		 // 6  返回封装好的结果集。
		return dataGridResult;
	}
	@Override
	public E3Result saveContent(TbContent tbContent) {
		   // 1  对比页面提交的参数和数据库的字段是否对应，如果页面提交的参数补全 就手动补全字段
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		   // 2   使用逆向工程，将对象插入到数据库对应的表格里面
		int insertSelective = tbContentMapper.insertSelective(tbContent);
		   // 3  返回结果集
		if(insertSelective!=1){
			E3Result result = E3Result.ok();
			result.setStatus(Constant.RESULT_STATUS_0);
			return result;
		}
		 try {
			//同步缓存，因为就是设置该key的过期时间为1秒.
			   jedisClient.expire(hashKey, 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//这里带参数的OK方法里面会自动赋值状态值为200。
		return E3Result.ok(insertSelective);
	}
	@Override
	public E3Result updateContent(TbContent tbContent) {
	         //  1  对比页面提交的参数和数据库的字段是否对应，如果页面提交的参数不全，就手动补全字段
		tbContent.setUpdated(new Date());
	        // 2   使用逆向工程，通过商品的id进行修改
	int selective = tbContentMapper.updateByPrimaryKeySelective(tbContent);
	        //  3  返回结果集
	if(selective!=1){
		//修改失败
		E3Result result = E3Result.ok();
		result.setStatus(Constant.RESULT_STATUS_0);
		return result;
	}
	 try {
		//同步缓存，因为就是设置该key的过期时间为1秒.
		   jedisClient.expire(hashKey, 1000);
	} catch (Exception e) {
		e.printStackTrace();
	}
	//这里带参数的OK方法里面会自动赋值状态值为200。
	return E3Result.ok(selective);
	}
	@Override
	public E3Result deleteContent(String ids) {
	   // 1  通过逗号切割传入的ids  ，然后返回一个数组
		   String[] idsArray = ids.split(",");
	    //2   遍历数组，然后获得id  ，然后通过id删除数据库里面的数据
		   for (String id : idsArray) {
			tbContentMapper.deleteByPrimaryKey( Long.valueOf(id));
		}
		   try {
			//同步缓存，因为就是设置该key的过期时间为1秒.
			   jedisClient.expire(hashKey, 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    //3   返回结果集
		return E3Result.ok(idsArray);
	}
	/**
	 * 通过首页轮播图这个模块的id 查询出该商品分类下面的所有的商品内容的集合，并且有缓存的介入
	 */
	@Override
	public List<TbContent> findTbcontentListByContentCatId(Long carouselContentCatId) {
		 try {
			// 首先我们根据商品分类的id，查询Redis里面是否存储有该数据。
			String hget = jedisClient.hget(hashKey, carouselContentCatId.toString());//需要注意的是这里的字段，最好是不要写死。
			   if(StringUtils.isNotBlank(hget)){
				// A  有，我们就直接从Redis里面获取数据，然后返回
				   List<TbContent> list = JsonUtils.jsonToList(hget, TbContent.class);
				   int i= 1/0;
				   return list;
			   }
		} catch (Exception e) {
			e.printStackTrace();
		}
		   //  B   没有，我们就通过contentCatId 从数据库里面查询出对应分类下面的商品分类的集合。
		          //创建查询条件对象
		   TbContentExample example = new TbContentExample();
		   Criteria criteria = example.createCriteria();
		          //添加查询条件
		   criteria.andCategoryIdEqualTo(carouselContentCatId);
		   //开始查询数据库
		   List<TbContent> list = tbContentMapper.selectByExample(example);
		     try {
						// 1.2      将查询出来的结果以hash数据存入Redis里面，需要注意的是需要将list集合转换为json数据，也就是string。
						   jedisClient.hset(hashKey, carouselContentCatId.toString(), JsonUtils.objectToJson(list));
					} catch (Exception e) {
						e.printStackTrace();
					}
		   //1.1   然后将查询出的集合，返回给表现层，
		return list;
	}

}
