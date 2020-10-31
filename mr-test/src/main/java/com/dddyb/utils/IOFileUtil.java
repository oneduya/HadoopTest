package com.dddyb.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;

public class IOFileUtil {

	public static Instance stringsToInstance(Instances m_data, String str) {
		String attributes[] = null;
		Instance inst = new DenseInstance(m_data.numAttributes());

		if (str.contains(",")) {
			attributes = str.split(",");
		} else if (str.contains(" ")) {
			attributes = str.split(" ");
		} else {
			attributes = str.split("\\s+");
		}
		if (attributes.length == m_data.numAttributes()) {
			for (int i = 0; i < m_data.numAttributes(); i++) {
				Attribute attribute = m_data.attribute(i);
				String value = attributes[i];
				if (value.equals("?")) {
					return null;
				}

				if (attribute.isNumeric()) {
					inst.setValue(m_data.attribute(i), Double.parseDouble(value));
				} else {
					if (value.startsWith("'")) {
						value = value.substring(1, value.length() - 1);
					}

					try {
						inst.setValue(m_data.attribute(i), value);
					} catch (Exception e) {
						m_data.attribute(i);
						inst.setValue(m_data.attribute(i), value);
						e.printStackTrace();

					}
				}
			}
		} else {
			return null;
		}
		return inst;

	}
	
	public static double[] convStrToDoubArr(String str) {
		String strArr[] = str.split(",");
		double[] douArr = new double[strArr.length];
		for (int i = 0; i < douArr.length; i++) {
			douArr[i] = Double.valueOf(strArr[i]);
		}
		return douArr;
	}

	public static long getLengthOfFile(Configuration conf, Path Filepath) {
		long filesize = 0;
		try {
			FileSystem hdfs = FileSystem.get(conf);
			FileStatus status[] = hdfs.listStatus(Filepath);
			for (FileStatus file : status) {
				FileStatus files = hdfs.getFileStatus(file.getPath());
				if (files.isFile()&& files.getPath().toString().contains(IOUtil.END_TEXT_TYPE)) {
					filesize = files.getLen();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filesize;
	}

	public static ArrayList<String> getArrayFromPath(Path path)
			throws Exception {

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(path.toString()), conf);

		if (fs.exists(path)) {
			FileStatus status[] = fs.listStatus(path);
			for (FileStatus file : status) {
				if (file.isFile() && file.getLen() > 0) {
					InputStream in = null;
					try {
						in = fs.open(file.getPath());
						return convertStreamToArrayString(in);

					} finally {
						IOUtils.closeStream(in);
					}
				}
			}
		}
		return new ArrayList<String>();
	}

	public static ArrayList<String> convertStreamToArrayString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		ArrayList<String> arrstr = new ArrayList<String>();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				arrstr.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return arrstr;
	}
	
	public static String convertArrayToString(ArrayList<Double> array) {
		
		String temp = "";
		for (int i = 0; i < array.size(); i++) {
			if (i == array.size()-1) {
				temp = temp + array.get(i);
			} else {
				temp = temp + array.get(i) + ",";
			}

		}
		return temp;
	}
	
	public static String getStringFromPath(Path path) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(path.toString()), conf);
		FileStatus status[] = fs.listStatus(path);
		for (FileStatus file : status) {

			if (file.isFile()&&file.getLen()>0) {
				InputStream in = null;
				try {
					in = fs.open(file.getPath());
					return convertStreamToString(in);

				} finally {
					IOUtils.closeStream(in);
				}
			}
		}
		return null;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return line;
	}
	

	public static void saveStringToLocal(String path, String values) throws IOException {
		String folder=path.substring(0, path.lastIndexOf("/")+1);
		String filename = path.substring(path.lastIndexOf("/")+1);
		File dir = new File(folder);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(folder,filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream outputStream = new FileOutputStream(path, true);
		outputStream.write(values.toString().getBytes());
		outputStream.close();

	}
	
	public static String getTargetPathFromFile(Configuration conf, Path path) throws IOException{

		FileSystem fs = FileSystem.get(URI.create(path.toString()), conf);
		if (fs.exists(path)) {
			FileStatus status[] = fs.listStatus(path);
			for (FileStatus file : status) {

				if (file.isFile() && file.getLen() > 0) {
					InputStream in = null;
					try {
						in = fs.open(file.getPath());
						return IOFileUtil.convertStreamToString(in);

					} finally {
						IOUtils.closeStream(in);
					}
				}
			}
		}
		
		return null;
	}
	
	public static Instances genInstancesFromDFS(Configuration conf,Path path) throws Exception{
		
		String arrfInfor = conf.get(IOUtil.DATA_INFOR);
		DataSource source = new DataSource(arrfInfor);
		Instances mdata = source.getDataSet();
		mdata.setClassIndex(mdata.numAttributes() - 1);
		mdata.clear();

		FileSystem fs = FileSystem.get(URI.create(path.toString()), conf);
		if (fs.exists(path)) {
			FileStatus status[] = fs.listStatus(path);
			for (FileStatus file : status) {
				if (file.isFile() && file.getLen() > 0) {
					InputStream in = null;
					try {
						in = fs.open(file.getPath());
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						String line = null;
						while ((line = reader.readLine()) != null) {
							Instance inst = stringsToInstance(mdata, line);
							if (inst != null) {
								mdata.add(inst);
							}
						}
					} finally {
						IOUtils.closeStream(in);
					}
				}
			}
		}

		return mdata;
	}
	
}
