package com.dddyb.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
  四个泛型解释:
    KEYIN :K1的类型
    VALUEIN: V1的类型

    KEYOUT: K2的类型
    VALUEOUT: V2的类型
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    List<String> data = new ArrayList<>();
    String a;

    //map方法就是将K1和V1 转为 K2和V2
    /*
      参数:
         key    : K1   行偏移量
         value  : V1   每一行的文本数据
         context ：表示上下文对象
     */
    /*
      如何将K1和V1 转为 K2和V2
        K1         V1
        0   hello,world,hadoop
        15  hdfs,hive,hello
       ---------------------------

        K2            V2
        hello         1
        world         1
        hdfs          1
        hadoop        1
        hello         1
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Text text = new Text();
        LongWritable longWritable = new LongWritable();
        //1:将一行的文本数据进行拆分
        String[] split = value.toString().split(",");
        //2:遍历数组，组装 K2 和 V2

        //3:将K2和V2写入上下文
        text.set(a);
        longWritable.set(1);
        context.write(text, longWritable);
    }

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        loadData();
        super.setup(context);
    }

    private void loadData() {
        BufferedReader br = null;
        try {
            InputStream ips = WordCountMapper.class.getClassLoader().getResourceAsStream("data/iris.arff");
            if (null == ips) {
                ips = WordCountMapper.class.getResourceAsStream("data/iris.arff");
            }
            br = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData2() {
        URL resource = WordCountMapper.class.getClassLoader().getResource("data/iris.arff");
    }
}
