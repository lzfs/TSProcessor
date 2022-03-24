package model;

import java.util.HashMap;
import java.util.Map;

public class KinectFrame implements Frame<KinectRecord> {
    private KinectRecord record;
    private Map<String, String> attributes = new HashMap<>();

    public KinectFrame(String timestamp, String kinectId, String recordId, String x, String z, String engaged, KinectRecord record) {
        this.attributes.put("timestamp", timestamp);
        this.attributes.put("kinectId", kinectId);
        this.attributes.put("recordId", recordId);
        this.attributes.put("x", x);
        this.attributes.put("z", z);
        this.attributes.put("engaged", engaged);
        this.record = record;
    }

    @Override
    public KinectRecord getRecord() {
        return record;
    }

    public String getValue(String key) {
        return this.attributes.get(key);
    }
}
