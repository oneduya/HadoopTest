package com.dddyb.utils;


import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.AbstractLoader;
import weka.core.converters.ConverterUtils;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author ：Yabing Du
 * @date ：2019/11/11 17:08
 * @description： 基础类
 * @modified By：
 * @version:
 */
public class Utils {
    /**
     * @param filePath 文件路径名
     * @return instances 数据对象
     * @author Yuangang Wang
     * @description 读取文件并返回Instances数据对象
     */
    public static Instances getInstancesForArffFile(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            Object currentConverter = ConverterUtils.getLoaderForFile(filePath);
            try {
                AbstractLoader loader = (AbstractLoader) currentConverter;
                loader.setSource(file);
                Instances instances = loader.getDataSet();
                System.out.println("load data successfully!");
                return instances;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("file not exist!");
            return (null);
        }

    }

    /**
     * @param
     * @return
     * @author Yabing Du
     * @description
     */
    public static List<List<Double>> instances2List(Instances instances) {
        int numericAttNum = 0;
        for (int attNum = 0; attNum < instances.numAttributes(); attNum++) {
            Attribute attribute = instances.attribute(attNum);
            if (attribute.isNumeric()) {
                numericAttNum += 1;
            } else {
                instances.setClassIndex(attNum);
            }
        }
        int i, j, flag;
        List<List<Double>> data = new ArrayList<>();
        for (Instance ins : instances) {
            List<Double> dataTmp = new ArrayList<>();
            flag = 0;
            for (i = 0; i < numericAttNum; i++) {
                if (i != instances.classIndex()) {
                    dataTmp.add(ins.value(i));
                    flag++;
                }
            }
            data.add(dataTmp);
        }
        return data;
    }




    /**
     * @param instances 数据对象
     * @return result 样本的类别
     * @author Yabing Du
     * @description 获取数据中所有样本的类别
     */
    public static List getClassValues(Instances instances) {
        List<Integer> result = new ArrayList<>();
        if (instances.classIndex() < 0) {
            for (int attNum = 0; attNum < instances.numAttributes(); attNum++) {
                Attribute attribute = instances.attribute(attNum);
                if (attribute.isNominal() && attribute.name().equals("class")) {
                    instances.setClassIndex(attNum);
                }
            }
            if (instances.classIndex() < 0){
                return null;
            }
            for (int i = 0; i < instances.numInstances(); i++) {
                double classValueD = instances.instance(i).classValue();
                int classValueI = new Double(classValueD).intValue();
                result.add(classValueI);
            }
        } else {
            for (int i = 0; i < instances.numInstances(); i++) {
                double classValueD = instances.instance(i).classValue();
                int classValueI = new Double(classValueD).intValue();
                result.add(classValueI);
            }
        }
        return result;
    }

    /**
     * @param instances 数据
     * @return 双重list
     * @author Yabing Du
     * @description 根据instances形成target数据，形如[[1.0 0.0 0.0],[0.0 1.0 0.0]]
     */
    public static List<List<Double>> getTarget(Instances instances) {
        List<List<Double>> result = new ArrayList<>();
        List classValues = getClassValues(instances);
        if(classValues == null){
            return null;
        }
        int classNum = Collections.max((List<Integer>) classValues) + 1;
        for (int i = 0; i < classValues.size(); i++) {
            List<Double> classTmp = new ArrayList<>();
            for (int j = 0; j < classNum; j++) {
                if ((Integer) classValues.get(i) == j) {
                    classTmp.add(1.0);
                } else {
                    classTmp.add(0.0);
                }
            }
            result.add(classTmp);
        }
        return result;
    }

    /**
     * @Description: 把类标号提取出来，变成[[1.0 0.0 0.0],[0.0 1.0 0.0]]的形式
     * @Author: WAHWJ
     * @param instances
     * @return double[][]
     * @Date: 2020/10/9 14:59
     */
    public static double[][] getTargetDMatrix(Instances instances) {
        List classValues = getClassValues(instances);
        if(classValues == null){
            return null;
        }
        int classNum = Collections.max((List<Integer>) classValues) + 1;
        double[][] target = new double[classValues.size()][classNum];
        for (int i = 0; i < classValues.size(); i++) {
            for (int j = 0; j < classNum; j++) {
                if ((Integer) classValues.get(i) == j) {
                    target[i][j] = 1.0;
                } else {
                    target[i][j] = 0.0;
                }
            }
        }
        return target;
    }

    /**
     * @Description: 获取double[][]类型的数据，不包含类标号
     * @Author: WAHWJ
     * @param instances
     * @return double[][]
     * @Date: 2020/10/9 15:13
     */
    public static double[][] getDataDMatrix(Instances instances) {
        int numericAttNum = 0;
        for (int attNum = 0; attNum < instances.numAttributes(); attNum++) {
            Attribute attribute = instances.attribute(attNum);
            if (attribute.isNumeric()) {
                numericAttNum++;
            } else {
                instances.setClassIndex(attNum);
            }
        }
        int sampleNum = instances.size();
        double[][] data = new double[instances.size()][numericAttNum];
        for (int i = 0; i < sampleNum; i++) {
            int cur = 0;
            for (int j = 0; j < numericAttNum; ) {
                if (cur != instances.classIndex()) {
                    data[i][j++] = instances.instance(i).value(cur);
                }
                cur++;
            }
        }
        return data;
    }

    /**
     * @param list 提取组合
     * @param k 想要选取组合的个数
     * @return list 包含有所有可能的集合
     * @author Yabing Du
     * @description 在集合中随机选取k个的所有组合
     */
    public static <T> List<List<T>> combinations(List<T> list, int k) {
        if (k == 0 || list.isEmpty()) {//去除K大于list.size的情况。即取出长度不足K时清除此list
            return Collections.emptyList();
        }
        if (k == 1) {//递归调用最后分成的都是1个1个的，从这里面取出元素
            return list.stream().map(e -> Stream.of(e).collect(toList())).collect(toList());
        }
        Map<Boolean, List<T>> headAndTail = split(list, 1);
        List<T> head = headAndTail.get(true);
        List<T> tail = headAndTail.get(false);
        List<List<T>> c1 = combinations(tail, (k - 1)).stream().map(e -> {
            List<T> l = new ArrayList<>();
            l.addAll(head);
            l.addAll(e);
            return l;
        }).collect(toList());
        List<List<T>> c2 = combinations(tail, k);
        c1.addAll(c2);
        return c1;
    }


    /**
     * @param list 需要划分的集合
     * @param n    前n个为一组，后面的为一组
     * @return 一个map对象
     * @author Yabing Du
     * @description 根据n将集合分成两组, partitioningBy方法可以将集合按条件分为两组，符合的true为键，不符合的false为键
     */

    public static <T> Map<Boolean, List<T>> split(List<T> list, int n) {
        return IntStream
                .range(0, list.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, list.get(i)))
                .collect(partitioningBy(entry -> entry.getKey() < n, mapping(AbstractMap.SimpleEntry::getValue, toList())));
    }

    public static void main(String[] args) throws Exception {

    }
}
