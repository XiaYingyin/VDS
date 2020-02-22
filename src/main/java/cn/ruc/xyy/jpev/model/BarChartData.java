package cn.ruc.xyy.jpev.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

//@Data
//@Builder
////@Table(name = "barChartData")
//@Entity(name = "barChartData")
//public class BarChartData {
//    @Id
//    String label;
//
//    String data;
//
//    public BarChartData(String label, String data) {
//        this.label = label;
//        this.data = data;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public List<Double> getData() {
//        List<Double> values = new ArrayList<Double>();
//        Double value = 0.0;
//
//        for(String field : this.data.split(",")) {
//            try {
//                value = Double.parseDouble(field);
//            }
//            // If the String contains other thing that digits and commas
//            catch (NumberFormatException e) {
//            }
//            values.add(value);
//        }
//
//        return values;
//    }
//
//    public void setData(List<Double> data) {
//        String values = "";
//        for(Double d : data) {
//            values.concat(String.valueOf(d));
//        }
//        this.data = values;
//    }
//}

@Data
@Builder
//@Table(name = "barChartData")
@Entity(name = "barChartData")
public class BarChartData {
    @Id
    String label;
    @ElementCollection
    List<Double> data;

    public BarChartData() {
    }

    public BarChartData(String label, List<Double> data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
}
