package cn.ruc.xyy.jpev.repository;

import cn.ruc.xyy.jpev.model.BarChartData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("barChartDataRepository")
public interface BarChartDataRepository extends JpaRepository<BarChartData, String> {
    BarChartData findBarChartDataByLabel(String label);
}
