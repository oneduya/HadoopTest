package cn.itcast.hdfs_api;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HdfsApiDemo {

    /*
      小文件的合并
     */
    @Test
    public void mergeFile() throws URISyntaxException, IOException, InterruptedException {
        //1:获取FileSystem（分布式文件系统）
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(),"root");

        //2:获取hdfs大文件的输出流
        FSDataOutputStream outputStream = fileSystem.create(new Path("/big_txt.txt"));

        //3:获取一个本地文件系统
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());

        //4:获取本地文件夹下所有文件的详情
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("D:\\input"));
        
        //5:遍历每个文件，获取每个文件的输入流
        for (FileStatus fileStatus : fileStatuses) {
            FSDataInputStream inputStream = localFileSystem.open(fileStatus.getPath());

            //6:将小文件的数据复制到大文件
            IOUtils.copy(inputStream, outputStream);
            IOUtils.closeQuietly(inputStream);
        }

        //7:关闭流
        IOUtils.closeQuietly(outputStream);
        localFileSystem.close();
        fileSystem.close();
    }

    /*
     文件的上传
     */
    @Test
    public void uploadFile() throws URISyntaxException, IOException {
        Configuration conf = new Configuration();
        //这个必须加，在访问非本机集群的时候要用到
        conf.set("dfs.client.use.datanode.hostname", "true");
        //1:获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://10.7.25.5:9000"), conf);

        //2:调用方法，实现上传
        fileSystem.copyFromLocalFile(new Path("D://pom.xml"), new Path("/"));

        //3:关闭FileSystem
        fileSystem.close();


    }


    /*
    实现文件的下载:方式2
     */
    @Test
    public void downloadFile2() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.client.use.datanode.hostname", "true");
        //1:获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://10.7.25.5:9000"), conf,"root");
        //2:调用方法，实现文件的下载

        fileSystem.copyToLocalFile(new Path("/wordcount/countTest.txt"), new Path("D://a4.txt"));

        //3:关闭FileSystem
        fileSystem.close();
    }

    /*
      实现文件的下载
     */
    @Test
    public void downloadFile() throws URISyntaxException, IOException {
        //1:获取FileSystem
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://10.7.25.5:9000"), new Configuration());

        //2:获取hdfs的输入流
        FSDataInputStream inputStream = fileSystem.open(new Path("/wordcount/countTest.txt"));

        //3:获取本地路径的输出流
        FileOutputStream outputStream = new FileOutputStream("D://a.xml");

        //4:文件的拷贝
        IOUtils.copy(inputStream, outputStream);
        //5:关闭流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        fileSystem.close();
    }


    /*
        hdfs创建文件夹
     */
    @Test
    public void mkdirsTest() throws URISyntaxException, IOException {
        //1:获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://10.7.25.5:9000"), new Configuration());
        //2:创建文件夹
        boolean bl = fileSystem.mkdirs(new Path("/aaa"));
        //fileSystem.create(new Path("/aaa/bbb/ccc/a.txt"));
        //fileSystem.create(new Path("/aaa2/bbb/ccc/a.txt"));
        //System.out.println(bl);

        //3: 关闭FileSystem
        fileSystem.close();

    }



    /*
     hdfs文件的遍历
     */
    @Test
    public void listFiles() throws URISyntaxException, IOException {
        Configuration conf = new Configuration();
        //这个必须加，在访问非本机集群的时候要用到
        conf.set("dfs.client.use.datanode.hostname", "true");
        //1:获取FileSystem实例
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://10.7.25.5:9000"), conf);

        //2:调用方法listFiles 获取 /目录下所有的文件信息
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"), true);

        //3:遍历迭代器
        while (iterator.hasNext()){
            LocatedFileStatus fileStatus = iterator.next();

            //获取文件的绝对路径 : hdfs://node01:8020/xxx
            System.out.println(fileStatus.getPath() + "----" +fileStatus.getPath().getName());

            //文件的block信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println("block数:"+blockLocations.length);
        }
    }
    /*
    获取FileSystem；方式4
    */
    @Test
    public void getFileSystem4() throws URISyntaxException, IOException {
        FileSystem fileSystem = FileSystem.newInstance(new URI("hdfs://10.7.25.5:9000"), new Configuration());

        System.out.println(fileSystem);

    }
    /*
    获取FileSystem；方式3
   */
    @Test
    public void getFileSystem3() throws IOException {
        Configuration configuration = new Configuration();
        //指定文件系统类型
        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        //获取指定的文件系统
        FileSystem fileSystem = FileSystem.newInstance(configuration);

        System.out.println(fileSystem);
    }
     /*
      获取FileSystem；方式2
     */
     @Test
     public void getFileSystem2() throws URISyntaxException, IOException {
         FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());

         System.out.println(fileSystem);
     }

    /*
      获取FileSystem；方式1
     */
    @Test
    public void getFileSystem1() throws IOException {
        //1:创建Configuration对象
        Configuration configuration = new Configuration();

        //2:设置文件系统的类型
        configuration.set("fs.defaultFS", "hdfs://node01:8020");

        //3:获取指定的文件系统
        FileSystem fileSystem = FileSystem.get(configuration);

        //4:输出
        System.out.println(fileSystem);
    }

    @Test
    public void urlHdfs() throws IOException {

        //1:注册url
        URL.setURLStreamHandlerFactory( new FsUrlStreamHandlerFactory());
        //2:获取hdfs文件的输入流
        InputStream inputStream = new URL("hdfs://node01:8020/a.txt").openStream();

        //3:获取本地文件的输出流
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\hello2.txt"));

        //4:实现文件的拷贝
        IOUtils.copy(inputStream, outputStream);

        //5:关流
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);

    }
}
