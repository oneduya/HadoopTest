package com.dddyb.afsfuture;

import com.dddyb.utils.IOUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



/**
 * 
 * Class for input data pretreatment based on general class.
 * 
 * @author yashuangMu
 *
 */

public class InputDataPretreatment {

	protected static final String ROOTPATH = IOUtil.ROOTPATH;
	private static final String ARFFPATH = IOUtil.ARFFPATH;
	protected static final String UCI = IOUtil.UCI;

	public static String[] dataForUCI = IOUtil.uciData;

	//在这个map存放的是数据集对应的在hfds中的文件夹路径
	public HashMap<String, String> mUciDataInputPaths = new HashMap<String, String>();
	//这个map存放的是在上面文件夹中具体的存放位置
	public HashMap<String, String> mUciDataInputDataInfors = new HashMap<String, String>();

	public InputDataPretreatment() {

		for (String ucikey : dataForUCI) {
			mUciDataInputPaths.put(ucikey, ROOTPATH + UCI + ucikey + "/");
			mUciDataInputDataInfors.put(ucikey, ARFFPATH + ucikey + ".arff");
		}

	}

	protected Object[] getAllDataName() {

		List<String> aL = Arrays.asList(dataForUCI);

		List<String> resultList = new ArrayList<String>();
		resultList.addAll(aL);

		return resultList.toArray();

	}

	public String[] getuciDataDataName() {

		return dataForUCI;
	}

	public String getInputDataPathByName(String dataname) {

		if (mUciDataInputPaths.containsKey(dataname)) {
			return mUciDataInputPaths.get(dataname);
		}
		return null;
	}

	public String getInputDataInforByName(String dataname) {
		if (mUciDataInputDataInfors.containsKey(dataname)) {
			return mUciDataInputDataInfors.get(dataname);
		}
		return null;
	}

}
