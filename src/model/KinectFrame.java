package model;

public class KinectFrame extends Frame {
    private String timestamp;
    private String kinectId;
    private String recordId;
    private double x;
    private double z;
    private String engaged;
    private KinectRecord record;

    public KinectFrame(String timestamp, String kinectId, String recordId, String x, String z, String engaged, KinectRecord record) {
        this.timestamp = timestamp;
        this.kinectId = kinectId;
        this.recordId = recordId;
        this.x = Double.parseDouble(x);
        this.z = Double.parseDouble(z);
        this.engaged = engaged;
        this.record = record;
    }

    public KinectRecord getRecord() {
        return record;
    }

    public void setRecord(KinectRecord record) {
        this.record = record;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKinectId() {
        return kinectId;
    }

    public void setKinectId(String kinectId) {
        this.kinectId = kinectId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getEngaged() {
        return engaged;
    }

    public void setEngaged(String engaged) {
        this.engaged = engaged;
    }
}
