package view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Alg;

import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class DiskScheduledTab extends Tab {

    private double currXYChartW;
    private double currXYChartH;
    private ScrollPane scrollPane;
    private Stage parentStage;
    private TabPane parentTabPane;
    private final StringBuilder calTotalResult = new StringBuilder();
    private final StringBuilder calAverResult = new StringBuilder();
    private final String group;
    private LineChart<Number,Number> XYLineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    public DiskScheduledTab(int startPoint, int xUpperBound, int yUpperBound, int seekTime, String group,
                            Map<Alg.ALG_TYPES,XYChart.Series<Number,Number>> series) throws Exception {

        this.group = group;
        //生成表格
        this.xAxis = new NumberAxis(0,xUpperBound + 5,xUpperBound/10.0);
        this.yAxis = new NumberAxis(0,yUpperBound + 5,yUpperBound/10.0);
        this.XYLineChart = new LineChart<>(xAxis,yAxis);

        dealSeries(startPoint, yUpperBound,seekTime,series);

        initScrollPane();
        super.closableProperty().set(false);
        super.selectedProperty().addListener((observable, oldValue, newValue) ->
                DiskSchedulePane.getCalText().setText(this.getCalResult()));
        this.setText("Group-" + group);
        this.setContent(scrollPane);
    }

    private void dealSeries(int startPoint, int yUpperBound, int seekTime,
                            Map<Alg.ALG_TYPES, XYChart.Series<Number, Number>> series) throws Exception {
        //生成随机数
        List<XYChart.Data<Number, Number>> rawData = Alg.generate_RRData(startPoint, seekTime, yUpperBound);
        //处理数据
        if (series.size() == 0) throw new Exception("Group-" + group + ":  There was not any selection for alg");
        Set<Alg.ALG_TYPES> keySet = series.keySet();
        for (Alg.ALG_TYPES alg_types : keySet) {
            XYChart.Series<Number, Number> replaceSeries = new XYChart.Series<>();
            List<XYChart.Data<Number,Number>> dataList = Alg.alg_sel(alg_types, rawData, startPoint);

            int totalMovingLength = Alg.calTotalMovingTrackLength(dataList);
            double averMovingLength = totalMovingLength/ (double) seekTime;
            this.calTotalResult.append(alg_types.toString()).append(" - ")
                    .append(totalMovingLength).append("  ");
            this.calAverResult.append(alg_types.toString()).append(" - ")
                    .append(averMovingLength).append(" ");

            assert dataList != null;
            replaceSeries.getData().addAll(dataList);
            replaceSeries.setName(alg_types.toString());
            series.replace(alg_types, replaceSeries);
        }
        this.calTotalResult.insert(0,"磁头移动总数（条）：");
        this.calAverResult.insert(0,"平均移动（条）：");
        for (Alg.ALG_TYPES alg_types : keySet){
            this.XYLineChart.getData().add(series.get(alg_types));
        }
    }

    private void initScrollPane(){
        scrollPane = new ScrollPane(XYLineChart);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == MouseEvent.BUTTON2 && event.getButton() == MouseButton.PRIMARY){
                parentStage.setFullScreen(!parentStage.isFullScreen());
            }else if (event.getButton() == MouseButton.PRIMARY){
                zoom(false);
                scrollPane.setVvalue(event.getY()/(XYLineChart.getHeight() + 80));
                scrollPane.setHvalue(event.getX()/parentStage.getWidth());
            }else if (event.getButton() == MouseButton.SECONDARY){
                zoom(true);
                scrollPane.setVvalue(event.getY()/(XYLineChart.getHeight() + 80));
                scrollPane.setHvalue(event.getX()/parentStage.getWidth());
            }
        });
        AnchorPane.setTopAnchor(scrollPane,50.0);
        AnchorPane.setBottomAnchor(scrollPane,50.0);
        AnchorPane.setLeftAnchor(scrollPane,50.0);
        AnchorPane.setRightAnchor(scrollPane,100.0);
    }

    public void setXYChartWidth(double percentage){
        XYLineChart.setMinWidth(currXYChartW + currXYChartW*percentage);
        xAxis.setTickUnit((int)(7000/(currXYChartW + currXYChartW*percentage)));
    }

    public void setXYChartParameters(double width,double height){
        XYLineChart.setMinWidth(width);
        XYLineChart.setMinHeight(height);
        setCurrXYChartW(width);
        setCurrXYChartH(height);
    }

    private void zoom(boolean zoomable){
        if (zoomable){
            XYLineChart.setMinWidth(XYLineChart.getWidth() + 500);
            XYLineChart.setMinHeight(XYLineChart.getHeight() + 500);
        }else{
            if (getCurrXYChartH() != XYLineChart.getHeight()) {
                XYLineChart.setMinWidth(XYLineChart.getWidth() - 500);
                XYLineChart.setMinHeight(XYLineChart.getHeight() - 500);
            }
        }
    }

    /**
     * setter & getter
     */
    public double getCurrXYChartW() { return currXYChartW; }
    public void setCurrXYChartW(double currXYChartW) { this.currXYChartW = currXYChartW; }
    public double getCurrXYChartH() { return currXYChartH; }
    public void setCurrXYChartH(double currXYChartH) { this.currXYChartH = currXYChartH; }
    public TabPane getParentTabPane() { return parentTabPane; }
    public void setParentTabPane(TabPane parentTabPane) { this.parentTabPane = parentTabPane; }
    public Stage getParentStage() { return parentStage; }
    public void setParentStage(Stage parentStage) { this.parentStage = parentStage; }
    public String getCalResult(){return this.calTotalResult.toString() + "\n" + this.calAverResult.toString();}
    public String getGroup(){return this.group;}
    public LineChart<Number,Number> getXYLineChart() { return XYLineChart; }
    public void setXYLineChart(LineChart<Number,Number> XYLineChart) { this.XYLineChart = XYLineChart; }
    public NumberAxis getXAxis() { return xAxis; }
    public void setXAxis(NumberAxis xAxis) { this.xAxis = xAxis; }
    public NumberAxis getYAxis() { return yAxis; }
    public void setYAxis(NumberAxis yAxis) { this.yAxis = yAxis; }
}
