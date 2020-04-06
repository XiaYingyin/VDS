package cn.ruc.xyy.jpev.model;

public class BuildInfo {
    String timeStamp;
    String result;

    public BuildInfo(String timeStamp, String result) {
        this.timeStamp = timeStamp;
        this.result = result;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
