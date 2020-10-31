package com.dddyb.topn;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * @Description: //TODO
 * @ClassName: OrderGroupComparator
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-29 19:13
 */
public class OrderGroupComparator extends WritableComparator {
    // 2: 调用父类的有参构造
    public OrderGroupComparator() {
        super(OrderBean.class,true);
    }

    //3: 指定分组的规则(重写方法)
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        //3.1 对形参做强制类型转换
        OrderBean first = (OrderBean)a;
        OrderBean second = (OrderBean)b;
        //3.2 指定分组规则
        return first.getOrderId().compareTo(second.getOrderId());
    }
}
