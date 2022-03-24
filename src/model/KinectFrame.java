package model;

import java.util.HashMap;
import java.util.Map;

public class KinectFrame implements Frame<KinectRecord> {
    private KinectRecord record;
    private Map<String, String> attributes = new HashMap<>();

    public KinectFrame(Map<String, String> attributesMap, KinectRecord record) {
        this.attributes = attributesMap;
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
