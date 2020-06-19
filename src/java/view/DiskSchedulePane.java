package view;

import controller.DiskSchedulePaneController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DiskSchedulePane {
    private static final String tip = "Tips: \n\t1. 鼠标左键双击折线图，退出或进入全屏。" +
            "\n\t2. 悬停折线图的点，获得详细坐标。" +
            "\n\t3. 鼠标左键单击缩小，右键单击放大。";
    private static final Text calText = new Text();

    public DiskSchedulePane(Stage viewer, TabPane tabPane){
        Scene viewerScene = new Scene(initPane(tabPane));
        viewer.setScene(viewerScene);
        viewer.setTitle("disk scheduled viewer");
        viewer.setFullScreen(true);
        viewer.setMinWidth(1300);
        viewer.setMinHeight(950);
        DiskSchedulePaneController.addListener2ChangeXYProperties(viewer,tabPane);
        DiskSchedulePaneController.changeText(tabPane,calText);
        viewer.initModality(Modality.APPLICATION_MODAL);
        viewer.show();
    }

    private BorderPane initPane(TabPane tabPane){
        BorderPane borderPane = new BorderPane();

        Label label = new Label("横向增比:");
        Slider slider = new Slider();
        slider.setMax(200);
        slider.setMin(0);
        slider.setValue(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(10);
        DiskSchedulePaneController.addListener2ChangXYChartWidth(slider,tabPane);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(label,slider);

        Text text = new Text(tip);
        text.setFill(Color.CORAL);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(text,calText);

        borderPane.setCenter(tabPane);
        borderPane.setTop(vBox);
        borderPane.setBottom(anchorPane);

        AnchorPane.setBottomAnchor(text,100.0);
        AnchorPane.setRightAnchor(text,600.0);
        AnchorPane.setTopAnchor(text,10.0);
        AnchorPane.setLeftAnchor(text,50.0);

        AnchorPane.setBottomAnchor(calText,10.0);
        AnchorPane.setRightAnchor(calText,100.0);
        AnchorPane.setTopAnchor(calText,90.0);
        AnchorPane.setLeftAnchor(calText,100.0);

        return borderPane;
    }

    public static Text getCalText(){return calText;}
}
