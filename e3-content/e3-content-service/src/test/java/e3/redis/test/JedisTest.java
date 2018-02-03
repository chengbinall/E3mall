package e3.redis.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 这是测试Redis的测试类哦。
 * @author 98448
 *
 */
public class JedisTest {

	/**
	 * 这是使用单机版连接Redis的测试类
	 */
   @Test
  public void JedisTest(){
	   //创建jedis对象,参数是需要连接的reds服务器的IP地址和端口号, "6379"
	   Jedis jedis = new Jedis("192.168.25.128", 6379);
	   //然后通过对象设置和获取
	   jedis.set("test", "123");
	   //然后获取
	  Long incr = jedis.incr("test");
	   //关闭资源
	   jedis.close();
	   System.out.println(incr);
   }
   /**
    * 这是使用连接池进行测试单机版的Redis
    */
   @Test
   public void jedispoolTest(){
	   //创建Redis连接池对象
	   JedisPool pool = new JedisPool("192.168.25.128", 6379);
	   //通过pool对象获得操作Redis的对象jedis
	   Jedis jedis = pool.getResource();
	   Long decr = jedis.decr("test");
	   System.out.println(decr);
   }
   /**
    * 这是测试连接集群版的Redis的测试方法
    */
   @Test
   public void jedisCluster(){
	   //添加集群里面的Redis服务器的地址
	  Set<HostAndPort>nodes= new HashSet<>();
	  nodes.add(new HostAndPort("192.168.25.133", 7001));
	  nodes.add(new HostAndPort("192.168.25.133", 7002));
	  nodes.add(new HostAndPort("192.168.25.133", 7003));
	  nodes.add(new HostAndPort("192.168.25.133", 7004));
	  nodes.add(new HostAndPort("192.168.25.133", 7005));
	  nodes.add(new HostAndPort("192.168.25.133", 7006));
	// 创建连接对象
	  JedisCluster jedisCluster = new JedisCluster(nodes);
	  //通过连接对象直接对Redis数据库进行操作
	  jedisCluster.set("test", "1");
	  String test = jedisCluster.get("test");
	  Long incr = jedisCluster.incr("test");
	  System.out.println(test+" >>>>"+incr);
   }

}
