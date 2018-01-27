package cn.e3mall.FastDFStest;

import java.io.IOException;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFStest {
	@Test
	public void fastDFStest() throws Exception{
		//这是测试通过fastDFS进行文件上传的。
		//1）把FastDFS的java客户端添加到工程中。
		//2）创建一个配置文件。需要配置tracker服务器的ip地址和端口号
		//3）加载配置文件,需要注意的是他必须是绝对路径哦，不支持classpath的路径
		ClientGlobal.init("E:/eclipse工作空间/e3mall/e3-web/src/main/resources/conf/Tracker.properties");
		//4）创建一个TrackerClient对象，直接new就可以了
		TrackerClient client = new TrackerClient();
		//5）使用Trackerclient创建一个TrackerServer对象
		TrackerServer trackerServer = client.getConnection();
		//6）创建一个StorageClient对象，构造参数TrackerServer、StorageServer（null）
		StorageServer storageServer=null;
		StorageClient  storageClient = new StorageClient(trackerServer, storageServer);
		//7）使用StorageClient上传文件
		String[] appender_file = storageClient.upload_appender_file("E:/娱乐/图片/timg.jpg", "jpg", null);
		//8）返回路径和文件名
		for (String string : appender_file) {
			System.out.println(string);
		}
	}

}
