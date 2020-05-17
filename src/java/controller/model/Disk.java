package controller.model;

public class Disk {

    private String groupNumber;
    private String trackNumber;
    private String startPoint;
    private String seekTime;
    private boolean FCFS_sel,SSTF_sel,LOOK_sel,C_SCAN_sel;

    public Disk(int groupNumber){
        this.groupNumber = String.valueOf(groupNumber);
    }

    public String getSeekTime() { return seekTime; }
    public String getTrackNumber() { return trackNumber; }
    public String getStartPoint() { return startPoint; }
    public String getGroupNumber() { return groupNumber; }
    public void setGroupNumber(String groupNumber) { this.groupNumber = groupNumber; }
    public void setTrackNumber(String trackNumber) { this.trackNumber = trackNumber; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }
    public void setSeekTime(String seekTime) { this.seekTime = seekTime; }
    public boolean isFCFS_sel() { return FCFS_sel; }
    public void setFCFS_sel(boolean FCFS_sel) { this.FCFS_sel = FCFS_sel; }
    public boolean isSSTF_sel() { return SSTF_sel; }
    public void setSSTF_sel(boolean SSTF_sel) { this.SSTF_sel = SSTF_sel; }
    public boolean isLOOK_sel() { return LOOK_sel; }
    public void setLOOK_sel(boolean LOOK_sel) { this.LOOK_sel = LOOK_sel; }
    public boolean isC_SCAN_sel() { return C_SCAN_sel; }
    public void setC_SCAN_sel(boolean c_SCAN_sel) { C_SCAN_sel = c_SCAN_sel; }
}
