package com.dddyb.topn;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;



/**
 * @Description: //TODO
 * @ClassName: GroupPatitioner
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-29 19:08
 */
public class OrderPartition extends Partitioner<OrderBean, Text> {
    @Override
    public int getPartition(OrderBean orderBean, Text text, int i) {
        return (orderBean.getOrderId().hashCode() & 2147483647) % i;
    }
}
