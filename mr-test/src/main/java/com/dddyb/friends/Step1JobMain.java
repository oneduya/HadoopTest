package com.dddyb.friends;

import com.dddyb.wordcount.JobMain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



/**
 * @Description: //TODO
 * @ClassName: Step1JobMain
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-26 21:14
 */
public class Step1JobMain extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        //1:获取Job对象
        Job job = Job.getInstance(super.getConf(),
                "common_friends_step1_job");
        //2:设置job任务
        //第一步:设置输入类和输入路径
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("hdfs://master:9000/common_friends_step1_input"));
        //第二步:设置Mapper类和数据类型
        job.setMapperClass(Step1Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //第三,四,五,六
        //第七步:设置Reducer类和数据类型
        job.setReducerClass(Step1Reducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //第八步:设置输出类和输出的路径
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path("file:///D:\\out\\common_friends_step1_out"));
        //3:等待job任务结束
        boolean bl = job.waitForCompletion(true);
        return bl ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        //启动job任务
        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }
}
