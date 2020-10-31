package com.dddyb.myoutputformat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Description: //TODO
 * @ClassName: MyOutputFormatMapper
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-29 15:06
 */
public class MyOutputFormatMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        context.write(value, NullWritable.get());
    }
}
