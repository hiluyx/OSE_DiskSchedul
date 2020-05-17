package utils;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Alg{

    public enum ALG_TYPES {
        FCFS,SSTF,LOOK,C_SCAN
    }

    public static List<XYChart.Data<Number,Number>> alg_sel(ALG_TYPES alg_types,
                                                            List<XYChart.Data<Number,Number>> dataList,
                                                            int startPoint){
        switch (alg_types){
            case FCFS : return FCFS_alg(dataList);
            case LOOK : return LOOK_alg(dataList,startPoint);
            case SSTF : return SSTF_alg(dataList,startPoint);
            case C_SCAN : return C_SCAN_alg(dataList,startPoint);
        }
        return null;
    }

    private static List<Number> xyList2yList(List<XYChart.Data<Number,Number>> dataList){
        List<Number> yList = new ArrayList<>();
        for (XYChart.Data<Number, Number> numberNumberData : dataList) {
            yList.add(numberNumberData.getYValue());
        }
        return yList;
    }

    /**
     * 先到先服务算法FIFO，返回原生随机数数组
     * @param dataList rawData
     * @return data following the original time sequence
     */
    public static List<XYChart.Data<Number,Number>> FCFS_alg(List<XYChart.Data<Number,Number>> dataList){
        return dataList;
    }

    /**
     * 最短服务时间优先算法(SSTF, Shortest ServiceT ime First)
     * @param dataList rawData
     * @param startHead disk head position
     * @return data processed by SSTF
     */
    public static List<XYChart.Data<Number,Number>> SSTF_alg(List<XYChart.Data<Number,Number>> dataList,
                                                             int startHead){
        List<Number> yList = xyList2yList(dataList);
        List<XYChart.Data<Number,Number>> processedXYList = new ArrayList<>();
        int currHead = startHead;
        for(int i = 0; i < yList.size(); i++){
            int currPosition = i;
            int minDistance = Math.abs(yList.get(i).intValue() - currHead);
            for (int j = i + 1; j < yList.size(); j++){
                int currDistance = Math.abs(yList.get(j).intValue() - currHead);
                if (currDistance < minDistance){
                    minDistance = currDistance;
                    currPosition = j;
                }
            }
            currHead = yList.get(currPosition).intValue();
            processedXYList.add(new XYChart.Data<>(i,yList.get(currPosition)));
            if (currPosition != i){
                yList.add(yList.get(i).intValue());
                yList.remove(currPosition);
            }
        }
        return processedXYList;
    }

    /**
     * SCAN 扫描算法：也称电梯算法
     * @param dataList rawData
     * @param startPoint head position
     * @return data processed by LOOK
     */
    public static List<XYChart.Data<Number,Number>> LOOK_alg(List<XYChart.Data<Number,Number>> dataList, int startPoint){
        return SCAN_alg(dataList,startPoint,true);
    }
    /**
     * C-SCAN 循环扫描算法
     * @param dataList rawData
     * @param startPoint head position
     * @return data processed by C_SCAN
     */
    public static List<XYChart.Data<Number,Number>> C_SCAN_alg(List<XYChart.Data<Number,Number>> dataList, int startPoint){
        return SCAN_alg(dataList,startPoint,false);
    }

    /**
     * SCAN通用方法
     */
    private static List<XYChart.Data<Number,Number>> SCAN_alg(List<XYChart.Data<Number,Number>> dataList,
                                                              int startPoint,boolean TYPE){
        List<Number> yList = xyList2yList(dataList);
        List<XYChart.Data<Number,Number>> processedXYList = new ArrayList<>();
        yList.sort(Comparator.comparingInt(Number::intValue));
        int currPosition = 0;
        int headIndex = 0;
        for (Number number : yList) {
            if (number.intValue() < startPoint) {
                headIndex ++;
            } else {
                processedXYList.add(new XYChart.Data<>(currPosition ++, number));
            }
        }
        if (TYPE){
            for (int i = headIndex; i >= 0; i --){
                processedXYList.add(new XYChart.Data<>(processedXYList.size(),yList.get(i)));
            }
        }
        else{
            for (Number number : yList){
                if (yList.indexOf(number) < headIndex){
                    processedXYList.add(new XYChart.Data<>(currPosition ++,number));
                }
            }
        }
        return processedXYList;
    }
}