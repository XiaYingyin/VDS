package cn.ruc.xyy.jpev.service;

import cn.ruc.xyy.jpev.model.BarChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ruc.xyy.jpev.repository.BarChartDataRepository;

// 负责测试数据的读取
@Service("barChartDataService")
public class BarChartDataService {
    private BarChartDataRepository barChartDataRepository;

    @Autowired
    public BarChartDataService(BarChartDataRepository barChartDataRepository) {
        this.barChartDataRepository = barChartDataRepository;
    }

    public BarChartData getBarChartDataByExtension(String name) {
        return barChartDataRepository.findBarChartDataByLabel(name);
    }

    public BarChartData saveBarChartData(BarChartData barChartData) {
        return barChartDataRepository.save(barChartData);
    }
}
