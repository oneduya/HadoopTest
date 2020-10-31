package com.dddyb;

import com.dddyb.utils.Utils;
import weka.core.Instances;

import java.net.URL;

/**
 * @Description: //TODO
 * @ClassName: ReadFile
 * @author: WAHWJ
 * @version: 1.0.0
 * @createTime: 2020-10-22 11:37
 */
public class ReadFile {
    public static void main(String[] args) {
        ClassLoader classLoader = ReadFile.class.getClassLoader();
        URL resource = classLoader.getResource("com/dddyb/data/iris.arff");
        String path = resource.getPath();
        Instances instances = Utils.getInstancesForArffFile(path);
        System.out.println(instances.size());
    }
}
