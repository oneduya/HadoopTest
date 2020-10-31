package com.dddyb.myoutputformat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Description: //TODO
 * @ClassName: MyOutputFormat
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-28 22:09
 */
public class MyOutputFormat extends FileOutputFormat<Text, NullWritable> {

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        MyRecordWriter myRecordWriter = null;
        try {
            FileSystem fileSystem = FileSystem.get(new URI("hdfs://master:9000"),taskAttemptContext.getConfiguration());
            FSDataOutputStream goodCommentsOutputStream = fileSystem.create(new Path("hdfs://master:9000/output/good_comments.txt"));
            FSDataOutputStream badCommentsOutputStream = fileSystem.create(new Path("hdfs://master:9000/output/bad_comments.txt"));
            myRecordWriter = new MyRecordWriter(goodCommentsOutputStream, badCommentsOutputStream);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return myRecordWriter;
    }
}
