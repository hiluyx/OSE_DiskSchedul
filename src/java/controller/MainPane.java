package controller;

import controller.model.Disk;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.Alg;
import utils.ThreadPool;
import view.DiskScheduledViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPane extends BorderPane {
    private static final AnchorPane anchorPane = new AnchorPane();
    private static final GridPane gridPane = new GridPane();
    public static  Stage primaryStage;
    private static final ObservableList<Disk> disks = FXCollections.observableArrayList();
    private static final TableView<Disk> diskTableView = new TableView<>();
    private static List<Disk> delDisks = new ArrayList<>();

    public MainPane(){
        initTableView();
        Button addButton = initAddButton();
        Button clearButton = initClearButton();
        Button delButton = initDelButton();
        Button startButton = initStartButton();
        anchorPane.getChildren().addAll(gridPane,startButton,addButton,delButton,clearButton);
        super.setBottom(anchorPane);
        super.setTop(diskTableView);
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
            startAndCheck();
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

        TableColumn<Disk,String> algSelectionCol = new TableColumn<>("Algorithm selection(算法选择)");
        algSelectionCol.getColumns().addAll(FCFS_Col,SSTF_Col,LOOK_Col,C_SCAN_Col);
        diskTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        diskTableView.setRowFactory(tv->{
//            TableRow<Disk> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 1){
//                    Disk clickedDisk = row.getItem();
//                    getDelDisks().add(clickedDisk);
//                }
//            });
//            return row;
//        });
        diskTableView.getColumns().addAll(groupCol,trackNumberCol,initialHeadPositionCol,numberOfRequestsCol,algSelectionCol);
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

    private static void startAndCheck(){
        List<Disk> wrongDisks = new ArrayList<>();
        List<Disk> startDisks = diskTableView.getSelectionModel().getSelectedItems();
        AtomicInteger number = new AtomicInteger(0);
        synchronized (wrongDisks){
            ThreadPool.execute(()->{
                for (Disk disk : startDisks){
                    ThreadPool.executeOnCachedThreadPool(() -> {
                        number.getAndIncrement();
                        System.out.println("number: " + number.intValue());
                        try{
                            int group = Integer.parseInt(disk.getGroupNumber());
                            int startPoint = Integer.parseInt(disk.getStartPoint());
                            int xUpperBound = Integer.parseInt(disk.getSeekTime());
                            int yUpperBound = Integer.parseInt(disk.getTrackNumber());
                            int seekTime = Integer.parseInt(disk.getSeekTime());
                            Map<Alg.ALG_TYPES, XYChart.Series<Number,Number>> seriesMap = new HashMap<>();
                            if (disk.isFCFS_sel()) seriesMap.put(Alg.ALG_TYPES.FCFS,null);
                            if (disk.isC_SCAN_sel()) seriesMap.put(Alg.ALG_TYPES.C_SCAN,null);
                            if (disk.isLOOK_sel()) seriesMap.put(Alg.ALG_TYPES.LOOK,null);
                            if (disk.isSSTF_sel()) seriesMap.put(Alg.ALG_TYPES.SSTF,null);
                            new DiskScheduledViewer(startPoint,xUpperBound,yUpperBound,seekTime,group,seriesMap);
                        }catch (NumberFormatException exception){
                            System.out.println("add " + disk.getGroupNumber());
                            wrongDisks.add(disk);
                        }
                        synchronized (wrongDisks){
                            if (number.intValue() == startDisks.size()){
                                wrongDisks.notifyAll();
                                System.out.println("FX notified");
                            }
                        }
                    });
                }
            });
        }
        if (startDisks.size() > 0){
            synchronized (wrongDisks){
                try {
                    System.out.println("FX waiting");
                    wrongDisks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (wrongDisks.size() > 0){
                    StringBuilder wrongString = new StringBuilder();
                    wrongString.append("Groups: ");
                    for (Disk wDisk : wrongDisks){
                        wrongString.append(wDisk.getGroupNumber()).append(",");
                    }
                    wrongString.append(" is wrong!");
                    Alert _alert = new Alert(Alert.AlertType.ERROR);
                    _alert.setTitle("Start Failed!");
                    _alert.setHeaderText("Data input wrong:");
                    _alert.setContentText(String.valueOf(wrongString));
                    _alert.showAndWait();
                }
            }
        }else{
            Alert _alert = new Alert(Alert.AlertType.ERROR);
            _alert.setTitle("Start Failed!");
            _alert.setHeaderText("Data input wrong:");
            _alert.setContentText("There is not any data!");
            _alert.showAndWait();
        }
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

