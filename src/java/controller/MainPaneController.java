package controller;

import model.Disk;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utils.Alg;
import utils.ThreadPool;
import view.DiskSchedulePane;
import view.DiskScheduledTab;
import view.ErrorDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPaneController {
    public static void startAndCheck(TableView<Disk> diskTableView){
        List<Disk> wrongDisks = new ArrayList<>();
        List<String> wrongMsg = new ArrayList<>();
        List<Disk> startDisks = diskTableView.getSelectionModel().getSelectedItems();
        AtomicInteger handledCount = new AtomicInteger(0);
        TabPane tabPane = new TabPane();
        Stage viewer = new Stage();
        List<DiskScheduledTab> tabList = new ArrayList<>();//线程安全的add
        synchronized (wrongDisks){
            ThreadPool.execute(()->{
                for (Disk disk : startDisks){
                    ThreadPool.executeOnCachedThreadPool(() -> {
                        try{
                            String group = disk.getGroupNumber();
                            int startPoint = Integer.parseInt(disk.getStartPoint());
                            int xUpperBound = Integer.parseInt(disk.getSeekTime());
                            int yUpperBound = Integer.parseInt(disk.getTrackNumber());
                            int seekTime = Integer.parseInt(disk.getSeekTime());
                            Map<Alg.ALG_TYPES, XYChart.Series<Number,Number>> seriesMap = new HashMap<>();
                            if (disk.isFCFS_sel()) seriesMap.put(Alg.ALG_TYPES.FCFS,null);
                            if (disk.isC_SCAN_sel()) seriesMap.put(Alg.ALG_TYPES.C_SCAN,null);
                            if (disk.isLOOK_sel()) seriesMap.put(Alg.ALG_TYPES.LOOK,null);
                            if (disk.isSSTF_sel()) seriesMap.put(Alg.ALG_TYPES.SSTF,null);
                            DiskScheduledTab scheduledTab = new DiskScheduledTab(startPoint,xUpperBound,yUpperBound,seekTime,group,seriesMap);
                            scheduledTab.setParentStage(viewer);
                            scheduledTab.setParentTabPane(tabPane);
                            synchronized (tabList){
                                tabList.add(scheduledTab);
                                handledCount.getAndIncrement();
                                tabList.notifyAll();
                            }
                        } catch (Exception exception){
                            wrongMsg.add(exception.getMessage());
                            wrongDisks.add(disk);
                            handledCount.getAndIncrement();
                        }
                        //当已处理的tab数等于模拟的tab数，释放锁
                        synchronized (wrongDisks){
                            if (handledCount.intValue() == startDisks.size()){
                                wrongDisks.notifyAll();
                            }
                        }
                    });
                }
            });
        }
        if (startDisks.size() > 0){
            synchronized (wrongDisks){
                try {
                    wrongDisks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (wrongDisks.size() > 0){
                    ErrorDialog.getErrorDialog(wrongDisks,wrongMsg);
                }else{
                    tabPane.getTabs().addAll(tabList);
                    new DiskSchedulePane(viewer,tabPane);
                }
            }
        }else{
            ErrorDialog.getBlankDialog();
        }
    }

}
