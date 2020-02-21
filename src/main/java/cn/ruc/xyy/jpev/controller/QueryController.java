package cn.ruc.xyy.jpev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
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
		String queryJSON = obj.toString().substring(2, obj.toString().length() - 2).replace("QUERY PLAN=", "");
		return queryJSON;
	}

	@RequestMapping(value = "/query", method=RequestMethod.GET)
	public String ngetQueryPlan(@RequestParam("query") String query) {
		query = "EXPLAIN (ANALYZE, COSTS, VERBOSE, BUFFERS, FORMAT JSON) " + query;
		Object obj = jdbc.queryForList(query);
		String queryJSON = obj.toString().substring(2, obj.toString().length() - 2).replace("QUERY PLAN=", "");
		return queryJSON;
	}
}
