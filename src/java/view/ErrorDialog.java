package view;

import model.Disk;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

import java.util.Comparator;
import java.util.List;

public class ErrorDialog {

    public static void getBlankDialog(){
        Alert _alert = new Alert(Alert.AlertType.ERROR);
        _alert.setTitle("Start Failed!");
        _alert.setHeaderText("Blank");
        _alert.setContentText("There was not any lines picked!");
        _alert.showAndWait();
    }

    public static void getErrorDialog(List<Disk> wrongDisks,List<String> wrongMsg){
        wrongDisks.sort(Comparator.comparingInt(c -> Integer.parseInt(c.getGroupNumber())));

        StringBuilder wrongString = new StringBuilder();
        StringBuilder wrongContent = new StringBuilder();
        wrongString.append("Groups: ");
        wrongContent.append("Groups: ");
        for (Disk wDisk : wrongDisks){
            wrongContent.append(wDisk.getGroupNumber()).append(",");
            wrongString.append(wDisk.getGroupNumber()).append(",");
        }
        wrongContent.append(" was wrong!\n");
        wrongString.append(" was wrong!");

        for (String msg : wrongMsg){
            wrongString.append("\n\t").append(msg);
        }
        Label label = new Label("The exception msg was:");

        TextArea textArea = new TextArea(wrongString.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        Alert _alert = new Alert(Alert.AlertType.ERROR);
        _alert.setTitle("Start Failed!");
        _alert.setHeaderText("请检查你的数据是否规范");
        _alert.initModality(Modality.APPLICATION_MODAL);
        _alert.setContentText(wrongContent.toString());
        _alert.getDialogPane().setExpandableContent(expContent);
        _alert.showAndWait();
    }
}
