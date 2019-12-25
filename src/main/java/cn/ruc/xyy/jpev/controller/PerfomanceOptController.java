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
public class PerfomanceOptController {

	public PerfomanceOptController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private JdbcTemplate jdbc;

	@RequestMapping(value ="/popt/{table}/{value}", method=RequestMethod.GET)
	public String getQueryPlan(@PathVariable("table") String table, @PathVariable("value") int value){
		String update_card = "update pg_class set reltuples = " + value + " where relname = " + "'" + table + "'";
		jdbc.execute(update_card);
		return "Update " + table + "'s" + " cardinality to " + value;
	}
}
