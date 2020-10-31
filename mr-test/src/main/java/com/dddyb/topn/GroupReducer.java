package com.dddyb.topn;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Description: //TODO
 * @ClassName: GroupReducer
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-29 19:29
 */
public class GroupReducer extends Reducer<OrderBean,Text, Text, NullWritable> {

    @Override
    protected void reduce(OrderBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int i = 0;
        for (Text value : values) {
            context.write(value, NullWritable.get());
            i++;
            if(i >= 1){
                break;
            }
        }
    }
}
