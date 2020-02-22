package cn.ruc.xyy.jpev.model;

import java.util.List;

public class BarChartDataSet {

    List<String> barChartLabels;
    List<BarChartData> barChartData;

    public List<String> getBarChartLabels() {
        return barChartLabels;
    }

    public void setBarChartLabels(List<String> barChartLabels) {
        this.barChartLabels = barChartLabels;
    }

    public List<BarChartData> getBarChartData() {
        return barChartData;
    }

    public void setBarChartData(List<BarChartData> barChartData) {
        this.barChartData = barChartData;
    }
}
