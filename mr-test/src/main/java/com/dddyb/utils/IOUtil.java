package com.dddyb.utils;

public class IOUtil {

	public static boolean mEvealuationByNum = true;

	/** used in class InputDataPretreatment.java */
	public static final String ROOT = "hdfs://master:9000/";
	public static final String ROOTPATH = ROOT + "user/sourceData/";
	public static final String ARFFPATH = "dataArffInfor/";
	public static final String UCI = "UCI/";

	public static String[] uciData = { "abalone", "adult", "australian", "auto_mpg", "balance", "bankmarket", "banknote",
		"BLOGGER", "blood", "breast-cancer-w-d", "breast-cancer-w-o", "breast-cancer-w-p", "breast-cancer",
		"breast-tissue", "car", "Chess(KingRookvsKing)", "Chess(KingRookvsKingPawn)",
		"chronic_kidney_disease_full", "cmc", "connect-4", "covtype", "creditapproval", "cylinderBands",
		"diabetes", "ecoli", "eegeye", "elecNormNew", "fertility", "forestType", "glass", "haberman",
		"hayes-roth", "heart-statlog", "HTRU_2", "ILPD", "ionosphere", "iris", "kdd_internet_usage", "kddcup",
		"lenses", "locationdataforpersionactivity", "magic", "mfeat-factors", "mfeat-fourier", "mfeat-karhunen",
		"mfeat-zernike", "new-thyroid", "nursery", "parkinsons", "pima-indians-diabetes", "poker-lsn", "skin",
		"sonar", "spectf-heart", "tamilnadu", "vehicle", "vowel", "waveform", "wine", "yeast"};


	/** used in class KFolderDataPretreatment.java */

	public static final int NUMBERFORDER = 10;
	public static final String TRAINFORDERNAME = "TrainFolder";
	public static final String TESTFORDERNAME = "TestFolder";
	public static final String FOLDERDATAPATH = ROOT + "user/dataFolder/";
	public static final String FOLDER = "Folder";
	public static final String FOLDERNAME = "foldername";

	/** used in class KTimesDataPretreatment.java */
	public static final String TIMESDATAPATH = ROOT + "user/dataTimes/";

	/** used in Configuration */
	public static final String DATA_INFOR = "arrfInfor";
	public static final String MAP_NUMBER = "mapnumber";
	public static final String DATA_NAME = "dataname";
	public static final String FOLDER_NUMBER = "/foldernumber";
	public static final String DATA_TIMES = "datatimes";
	public static final String DATA_FOLDERS = "datafolders";
	public static final String MAX_MIX_PATH = "maxmixpath";
	public static final String INPUTPATH = "inputPath";

	public static final String DATAVALUE_PATH = "dataValuesPath";

	public static final String TEXT_TYPE = "*.txt*";
	public static final String END_TEXT_TYPE = ".txt";

	/** used in save result */

	public static final String LOCALPATH = "/home/dddyb/Downloads/workspace/HadoopDataMiningAppResult/";
	public static final String OUTPUT = "output/";
	public static final String MAX_MIN_OUTPUT = "maxminoutput/";
	public static final String LINE = "_";
	public static final String FILE = "_result.txt";

	public static final String RESULT = "/result/";
	public static final String MAERESULT = "/MAEresult/";

	public static final String CONVERDATATONORMALPATH = ROOT
			+ "user/dataFolder/converDataToNormal/";
	public static final String CONVERDATATONORMALWITHTPATH = ROOT
			+ "user/dataFolder/converDataToNormalWithT/";

	public static final String MULTIPLE_OUTPATH = "multipleOutputPath";
	public static final String RESULT_OUTPATH = "resultOutputPath";

	public static final String For_CLASSifiER = "forclassifier";

	public static final String RESULT_OUTPUT = "result/";

	/** used in class DataSplitIntoOverlapSubDatas.java */

	public static final String SPLIT_NO = "split_number";
	public static final String OVERLAP_NO = "overno_number";
	public static final String DATA_NO = "data_number";

	public static final String SPLITOUTPUT = "split" + IOUtil.OUTPUT;
	public static final String SPLIT_DATA_PATH = FOLDERDATAPATH
			+ "splitData/split/";

	public static final String STOP_CONDITION = "stopcondition";

	public static final String MAE = "MAE";

	public static double STOPFORERMT = 0.05;

}
