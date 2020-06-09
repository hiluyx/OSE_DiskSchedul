package view;

import controller.MainPaneController;
import model.Disk;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class MainPane extends BorderPane {
    private static final String tipText =
            "Tips：\n\t1. 鼠标左键单击一行选中，按住CTRL键+鼠标左键多选，CTRL+A全选。" +
            "\n\t2. 数据输入时，双击显示输入栏，回车确认，单击勾选或取消算法选择。" +
            "\n\t3. new按键新添一行。" +
            "\n\t4. clear按键清除所有行。" +
            "\n\t5. delete按键删除选中的行。" +
            "\n\t6. start按键模拟选中的行。";
    private static final AnchorPane anchorPane = new AnchorPane();
    private static final GridPane gridPane = new GridPane();
    public static  Stage primaryStage;
    private static final ObservableList<Disk> disks = FXCollections.observableArrayList();
    private static final TableView<Disk> diskTableView = new TableView<>();
    private static List<Disk> delDisks = new ArrayList<>();

    public MainPane(){
        initTableView();

        Disk firstDisk = new Disk(0,100,20,100);
        Disk secondDisk = new Disk(1,200,10,60);
        Disk thirdDisk = new Disk(2,300,20,80);
        disks.addAll(firstDisk,secondDisk,thirdDisk);
        diskTableView.setItems(disks);

        Button addButton = initAddButton();
        Button clearButton = initClearButton();
        Button delButton = initDelButton();
        Button startButton = initStartButton();
        Text text = initTipText();
        anchorPane.getChildren().addAll(gridPane,startButton,addButton,delButton,clearButton);
        super.setBottom(anchorPane);
        super.setCenter(text);
        super.setTop(diskTableView);
    }

    private static Text initTipText(){
        Text text = new Text(tipText);
        text.setFont(Font.font("Verdana", FontWeight.LIGHT, 17));
        text.setLineSpacing(5);
        return text;
    }
    private static Button initClearButton(){
        Button button = new Button("clear");
        button.setMinWidth(200);
        button.setOnAction(event -> {
            delDisks.clear();
            disks.clear();
            diskTableView.getItems().clear();
        });
        AnchorPane.setTopAnchor(button,10.0);
        AnchorPane.setLeftAnchor(button,350.0);
        AnchorPane.setRightAnchor(button,350.0);
        AnchorPane.setBottomAnchor(button,100.0);
        return button;
    }
    private static Button initAddButton(){
        Button button = new Button("new");
        button.setMinWidth(200);
        button.setOnAction(event -> addDisk());
        AnchorPane.setTopAnchor(button,10.0);
        AnchorPane.setLeftAnchor(button,100.0);
//        AnchorPane.setRightAnchor(button,500.0);
        AnchorPane.setBottomAnchor(button,100.0);
        return button;
    }
    private static Button initDelButton(){
        Button button = new Button("delete");
        button.setMinWidth(200);
        button.setOnAction(event -> {
            List<Disk> diskSelectionList = diskTableView.getSelectionModel().getSelectedItems();
            for (Disk disk : diskSelectionList) getDelDisks().add(disk);
            delDisks();
        });
        AnchorPane.setTopAnchor(button,10.0);
//        AnchorPane.setLeftAnchor(button,500.0);
        AnchorPane.setRightAnchor(button,100.0);
        AnchorPane.setBottomAnchor(button,100.0);
        return button;
    }
    private static Button initStartButton(){
        Button button = new Button("start");
        button.setOnAction(event -> {
            MainPaneController.startAndCheck(diskTableView);
        });
        AnchorPane.setTopAnchor(button,70.0);
        AnchorPane.setLeftAnchor(button,100.0);
        AnchorPane.setRightAnchor(button,100.0);
        AnchorPane.setBottomAnchor(button,40.0);
        return button;
    }
    private static void initTableView(){

        diskTableView.setEditable(true);

        TableColumn<Disk,String> groupCol = new TableColumn<>("Group(组别)");
        groupCol.setCellValueFactory(new PropertyValueFactory<>("groupNumber"));
        groupCol.setSortType(TableColumn.SortType.ASCENDING);
        groupCol.setMinWidth(100);

        TableColumn<Disk,String> trackNumberCol = new TableColumn<>("Track number(磁道数)");
        trackNumberCol.setCellValueFactory(new PropertyValueFactory<>("trackNumber"));
        trackNumberCol.setMinWidth(170);
        trackNumberCol.setCellFactory(TextFieldTableCell.forTableColumn());
        trackNumberCol.setOnEditCommit(event ->{
            TablePosition<Disk,String> position = event.getTablePosition();
            String number = event.getNewValue();
            Disk disk = event.getTableView().getItems().get(position.getRow());
            disk.setTrackNumber(number);
        });

        TableColumn<Disk,String> initialHeadPositionCol = new TableColumn<>("Initial head(磁头初始位置)");
        initialHeadPositionCol.setCellValueFactory(new PropertyValueFactory<>("startPoint"));
        initialHeadPositionCol.setMinWidth(200);
        initialHeadPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        initialHeadPositionCol.setOnEditCommit(event ->{
            TablePosition<Disk,String> position = event.getTablePosition();
            String number = event.getNewValue();
            Disk disk = event.getTableView().getItems().get(position.getRow());
            disk.setStartPoint(number);
        });

        TableColumn<Disk,String> numberOfRequestsCol = new TableColumn<>("requests(请求次数)");
        numberOfRequestsCol.setCellValueFactory(new PropertyValueFactory<>("seekTime"));
        numberOfRequestsCol.setMinWidth(150);
        numberOfRequestsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        numberOfRequestsCol.setOnEditCommit(event ->{
            TablePosition<Disk,String> position = event.getTablePosition();
            String number = event.getNewValue();
            Disk disk = event.getTableView().getItems().get(position.getRow());
            disk.setSeekTime(number);
        });

        TableColumn<Disk,Boolean> FCFS_Col = new TableColumn<>("FCFS");
        FCFS_Col.setCellValueFactory(new PropertyValueFactory<>("FCFS_sel"));
        FCFS_Col.setMinWidth(80);
        FCFS_Col.setCellValueFactory(param -> {
            Disk disk = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(disk.isFCFS_sel());
            booleanProperty.addListener((observable, oldValue, newValue) -> disk.setFCFS_sel(newValue));
            return booleanProperty;
        });
        setCells(FCFS_Col);

        TableColumn<Disk,Boolean> SSTF_Col = new TableColumn<>("SSTF");
        SSTF_Col.setCellValueFactory(new PropertyValueFactory<>("SSTF_sel"));
        SSTF_Col.setMinWidth(80);
        SSTF_Col.setCellValueFactory(param -> {
            Disk disk = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(disk.isSSTF_sel());
            booleanProperty.addListener((observable, oldValue, newValue) -> disk.setSSTF_sel(newValue));
            return booleanProperty;
        });
        setCells(SSTF_Col);

        TableColumn<Disk,Boolean> LOOK_Col = new TableColumn<>("LOOK");
        LOOK_Col.setCellValueFactory(new PropertyValueFactory<>("LOOK_sel"));
        LOOK_Col.setMinWidth(80);
        LOOK_Col.setCellValueFactory(param -> {
            Disk disk = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(disk.isLOOK_sel());
            booleanProperty.addListener((observable, oldValue, newValue) -> disk.setLOOK_sel(newValue));
            return booleanProperty;
        });
        setCells(LOOK_Col);

        TableColumn<Disk,Boolean> C_SCAN_Col = new TableColumn<>("C_SCAN");
        C_SCAN_Col.setCellValueFactory(new PropertyValueFactory<>("C_SCAN_sel"));
        C_SCAN_Col.setMinWidth(80);
        C_SCAN_Col.setCellValueFactory(param -> {
            Disk disk = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(disk.isC_SCAN_sel());
            booleanProperty.addListener((observable, oldValue, newValue) -> disk.setC_SCAN_sel(newValue));
            return booleanProperty;
        });
        setCells(C_SCAN_Col);

        TableColumn<Disk,Boolean> algSelectionCol = new TableColumn<>("Algorithm selection(算法选择)");
        algSelectionCol.getColumns().add(FCFS_Col);
        algSelectionCol.getColumns().add(LOOK_Col);
        algSelectionCol.getColumns().add(C_SCAN_Col);
        algSelectionCol.getColumns().add(SSTF_Col);

        diskTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        diskTableView.getColumns().add(groupCol);
        diskTableView.getColumns().add(trackNumberCol);
        diskTableView.getColumns().add(initialHeadPositionCol);
        diskTableView.getColumns().add(numberOfRequestsCol);
        diskTableView.getColumns().add(algSelectionCol);
    }

    private static void addDisk(){
        Disk disk = new Disk(disks.size());
        disks.add(disk);
        diskTableView.setItems(disks);
    }
    private static void delDisks(){
        for (Disk disk : delDisks) System.out.println(disk.getGroupNumber());
        for (Disk disk : delDisks){
            int delPos = Integer.parseInt(disk.getGroupNumber());
            disks.remove(disk);
            for (int i = delPos;i < disks.size();i ++){
                disks.get(i).setGroupNumber(String.valueOf(i));
            }
        }
        delDisks.clear();
        diskTableView.setItems(disks);
    }



    private static void setCells(TableColumn<Disk,Boolean> tableColumn){
        tableColumn.setCellFactory(param -> {
            CheckBoxTableCell<Disk,Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    public static List<Disk> getDelDisks() { return delDisks; }
    public static void setDelDisks(List<Disk> delDisks) { MainPane.delDisks = delDisks; }
}

