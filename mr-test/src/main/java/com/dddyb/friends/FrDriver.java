package com.dddyb.friends;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * @Description: //TODO
 * @ClassName: FrDriver
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-27 09:49
 */
public class FrDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.set("dfs.client.use.datanode.hostname", "true");
        //1:获取Job对象
        Job job1 = Job.getInstance(configuration, "common_friends_step1_job");
        //2:设置job任务
        //第一步:设置输入类和输入路径
        job1.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job1, new Path("hdfs://master:9000/friends_input"));
        //第二步:设置Mapper类和数据类型
        job1.setMapperClass(Step1Mapper.class);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);
        //第三,四,五,六
        //第七步:设置Reducer类和数据类型
        job1.setReducerClass(Step1Reducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        //第八步:设置输出类和输出的路径
        job1.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job1, new Path("hdfs://master:9000/friends_output1"));
        //3:等待job任务结束
        boolean bl = job1.waitForCompletion(true);
        if (bl) {
            configuration = new Configuration();
            configuration.set("dfs.client.use.datanode.hostname", "true");
            //1:获取Job对象
            Job job2 = Job.getInstance(configuration, "common_friends_step2_job");

            //2:设置job任务
            //第一步:设置输入类和输入路径
            job2.setInputFormatClass(TextInputFormat.class);
            TextInputFormat.addInputPath(job2, new Path("hdfs://master:9000/friends_output1"));

            //第二步:设置Mapper类和数据类型
            job2.setMapperClass(Step2Mapper.class);
            job2.setMapOutputKeyClass(Text.class);
            job2.setMapOutputValueClass(Text.class);

            //第三,四,五,六

            //第七步:设置Reducer类和数据类型
            job2.setReducerClass(Step2Reducer.class);
            job2.setOutputKeyClass(Text.class);
            job2.setOutputValueClass(Text.class);

            //第八步:设置输出类和输出的路径
            job2.setOutputFormatClass(TextOutputFormat.class);
            TextOutputFormat.setOutputPath(job2, new Path("hdfs://master:9000/friends_output2"));

            //3:等待job任务结束
            boolean b1 = job2.waitForCompletion(true);
            System.exit(b1 ? 0 : 1);
        }
        else {
            System.exit(1);
        }
    }
}
