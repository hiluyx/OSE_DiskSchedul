package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.DiskScheduledTab;

public class DiskSchedulePaneController {

    public static void addListener2ChangeXYProperties(Stage viewer, TabPane tabPane){
        viewer.heightProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Tab> tabList = tabPane.getTabs();
            for (Tab tab : tabList){
                DiskScheduledTab scheduledTab = (DiskScheduledTab) tab;
                double offset = viewer.isFullScreen()? 310:360;
                scheduledTab.setXYChartParameters(viewer.getWidth()-20,viewer.getHeight() - offset);
                scheduledTab.getYAxis().setTickUnit((int)(15000/newValue.doubleValue()));
            }
        });
        viewer.widthProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Tab> tabList = tabPane.getTabs();
            for (Tab tab : tabList){
                DiskScheduledTab scheduledTab = (DiskScheduledTab) tab;
                double offset = viewer.isFullScreen()? 310:360;
                scheduledTab.setXYChartParameters(viewer.getWidth()-20,viewer.getHeight() - offset);
                scheduledTab.getXAxis().setTickUnit((int)(7000/newValue.doubleValue()));
            }
        });
    }
    public static void addListener2ChangXYChartWidth(Slider slider, TabPane tabPane){
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Tab> tabList = tabPane.getTabs();
            for (Tab tab : tabList){
                DiskScheduledTab scheduledTab = (DiskScheduledTab) tab;
                scheduledTab.setXYChartWidth(newValue.doubleValue()/100);
            }
        });
    }
    public static void changeText(TabPane tabPane, Text calText){
        DiskScheduledTab tab = (DiskScheduledTab) tabPane.getTabs().get(0);
        calText.setText(tab.getCalResult());
        calText.setLineSpacing(7);
        calText.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
    }

}
