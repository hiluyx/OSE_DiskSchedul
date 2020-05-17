package view;

import controller.MainPane;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import utils.Alg;

import java.util.*;

public class DiskScheduledViewer {

    /**
     *
     *
     * offset & viewerCount 窗口定位偏移
     * toEqString 排除重复窗口
     */
    private static final int offset = 10;
    private final int viewerCount;
    private LineChart<Number,Number> XYLineChart;
    private final List<XYChart.Data<Number,Number>> rawData;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    public DiskScheduledViewer(int startPoint,
                               int xUpperBound,
                               int yUpperBound,
                               int seekTime,
                               int group,
                               Map<Alg.ALG_TYPES,XYChart.Series<Number,Number>> series){
        viewerCount = group;
        //生成表格
        this.xAxis = new NumberAxis(0,xUpperBound,xUpperBound/10.0);
        this.yAxis = new NumberAxis(0,yUpperBound,yUpperBound/10.0);
        this.XYLineChart = new LineChart<>(xAxis,yAxis);
        //生成随机数
        this.rawData = this.generate_RRData(startPoint,seekTime,yUpperBound);
        //处理数据
        Set<Alg.ALG_TYPES> keySet = series.keySet();
        for (Alg.ALG_TYPES alg_types : keySet) {
            XYChart.Series<Number, Number> replaceSeries = new XYChart.Series<>();
            replaceSeries.getData().addAll(Objects.requireNonNull(
                    Alg.alg_sel(alg_types, this.rawData, startPoint)));
            replaceSeries.setName(alg_types.toString());
            series.replace(alg_types, replaceSeries);
        }
        for (Alg.ALG_TYPES alg_types : keySet){
            this.XYLineChart.getData().add(series.get(alg_types));
        }
        //创建窗口
        Platform.runLater(this::initTheViewerStage);
    }

    private void initTheViewerStage(){
        Scene viewerScene = new Scene(new Group(XYLineChart),530,400);
        Stage viewer = new Stage();
        viewer.setScene(viewerScene);
        viewer.setTitle("disk scheduled viewer(Group-" + viewerCount + ")");
        viewer.setX(MainPane.primaryStage.getX() - 200 + offset*viewerCount*2);
        viewer.setY(MainPane.primaryStage.getY() + 200 + offset*viewerCount*3);
        viewer.show();
    }

    /**
     *
     * @param startPoint head
     * @param seekTime simulation times
     * @param yUpperBound Simulation boundary value
     * @return raw data
     */
    private List<XYChart.Data<Number,Number>> generate_RRData(int startPoint, int seekTime, int yUpperBound){
        List<XYChart.Data<Number,Number>> dataList = new ArrayList<>();
        XYChart.Data<Number,Number> dataStart = new XYChart.Data<>(0,startPoint);
        dataList.add(dataStart);
        for(int i = 1; i <= seekTime;i++){
            XYChart.Data<Number,Number> data = new XYChart.Data<>(i, (int) (Math.random() * (yUpperBound + 1)));
            dataList.add(data);
        }
        return dataList;
    }

    /**
     * setter & getter
     */
    public LineChart<Number,Number> getXYLineChart() { return XYLineChart; }
    public void setXYLineChart(LineChart<Number,Number> XYLineChart) { this.XYLineChart = XYLineChart; }
    public NumberAxis getXAxis() { return xAxis; }
    public void setXAxis(NumberAxis xAxis) { this.xAxis = xAxis; }
    public NumberAxis getYAxis() { return yAxis; }
    public void setYAxis(NumberAxis yAxis) { this.yAxis = yAxis; }
}
