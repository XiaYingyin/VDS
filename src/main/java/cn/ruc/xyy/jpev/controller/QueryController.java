package cn.ruc.xyy.jpev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5555")
public class QueryController {
	@Autowired
	private JdbcTemplate jdbc;

	public QueryController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value ="/query/{sql}", method=RequestMethod.GET)
	public String getQueryPlan(@PathVariable("sql") String sql){
		sql = "EXPLAIN (ANALYZE, COSTS, VERBOSE, BUFFERS, FORMAT JSON)" + sql;
		Object obj = jdbc.queryForList(sql);
		return obj.toString();
	}
}
