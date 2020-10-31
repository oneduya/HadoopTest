package com.dddyb.afsfuture;

import com.dddyb.utils.IOFileUtil;
import com.dddyb.utils.IOUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * 
 * Class for parameters for fuzzy sets based on the Map-Reduce. 
 * 
 * Fuzzynumber default is 2; 
 * 
 * command:
 * 
 * $ hadoop jar *.jar package.classname dataname mapnumber fuzzynumber
 * 
 * @author yashuangMu
 *
 */

public class MaxAndMinDataPretrement {

	public static void main(String[] args) throws Exception {
		
		String dataName = args[0];
		int mapnumber = Integer.valueOf(args[1]);
		
		InputDataPretreatment inputdatas = new InputDataPretreatment();
		String input = inputdatas.getInputDataPathByName(dataName);
		String arrfInfor = inputdatas.getInputDataInforByName(dataName);

		Configuration conf = new Configuration();
		conf.set(IOUtil.DATA_INFOR, arrfInfor);
		conf.set(IOUtil.DATA_NAME, dataName);
		conf.setInt(IOUtil.MAP_NUMBER, mapnumber);
		
		MaxAndMinDataPretrement task = new MaxAndMinDataPretrement();
		String parameterspath = task.run(conf, input);	

		// save the result
		String path = IOUtil.LOCALPATH + IOUtil.OUTPUT;
		String filename = dataName + IOUtil.LINE + MaxAndMinDataPretrement.class.getSimpleName() + IOUtil.FILE;
		IOFileUtil.saveStringToLocal(path + filename, parameterspath.toString());
		
	}
	public MaxAndMinDataPretrement(){
		
	}
	
	public String run(Configuration config, String input) throws Exception {

		Job job1 = Job.getInstance(config, MaxAndMinDataPretrement.class.getSimpleName());
		job1.setJarByClass(MaxAndMinDataPretrement.class);
		job1.setMapperClass(MaxAndMinMapper.class);
		job1.setReducerClass(MaxAndMinReducer.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);

		FileSystem outfs = FileSystem.get(config);
		Path out_path = (new Path(input + IOUtil.MAX_MIN_OUTPUT));
		outfs.delete((out_path), true);		
		
		FileInputFormat.setInputPaths(job1, new Path(input + IOUtil.TEXT_TYPE));
		FileOutputFormat.setOutputPath(job1,new Path(input + IOUtil.MAX_MIN_OUTPUT));

		int mapnumber = config.getInt(IOUtil.MAP_NUMBER, -1);
		if (mapnumber > 0) {
			long size = IOFileUtil.getLengthOfFile(config, new Path(input));
			int block_size = (int) (size / mapnumber) + 1;
			FileInputFormat.setMaxInputSplitSize(job1, block_size);
			FileInputFormat.setMinInputSplitSize(job1, 1);
		}
		

		return job1.waitForCompletion(true) ? (input + IOUtil.MAX_MIN_OUTPUT) : null;
	}
	

	public static class MaxAndMinMapper extends Mapper<Object, Text, Text, Text> {

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			try {
				String instance[] =value.toString().split(",");

				Text R_out = new Text();
				Text R_out_val = new Text();
				
				for (int i = 0; i < instance.length; i++) {
					double max = Double.MIN_VALUE;
					double min = Double.MAX_VALUE;
					max = Double.max(Double.valueOf(instance[i]), max);
					min = Double.min(Double.valueOf(instance[i]), min);
					String outval = max + "," + min;
					R_out_val.set(outval);
					R_out.set(String.valueOf(i));
					context.write(R_out, R_out_val);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static class MaxAndMinReducer extends
			Reducer<Object, Text, NullWritable, Text> {
		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
		}

		@Override
		protected void reduce(Object key, Iterable<Text> values,
				Reducer<Object, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			try {
				double max = Double.MIN_VALUE;
				double min = Double.MAX_VALUE;
				
				for (Text val : values) {

					String valstr = val.toString();
					String[] valArry=valstr.split(",");
					max = Math.max(max, Double.valueOf(valArry[0]));
					min = Math.min(min, Double.valueOf(valArry[1]));
				}

				String parameters =key.toString()+","+max +"," + min;
				
				Text R_out_value = new Text();
				R_out_value.set(parameters);
				context.write(NullWritable.get(), R_out_value);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
