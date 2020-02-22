package cn.ruc.xyy.jpev.controller;

import cn.ruc.xyy.jpev.model.BarChartData;
import cn.ruc.xyy.jpev.model.ExtensionItem;
import cn.ruc.xyy.jpev.service.BarChartDataService;
import cn.ruc.xyy.jpev.util.TPCHTestProcess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ExtensionController {

	public ExtensionController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private BarChartDataService barChartDataService;

	@RequestMapping(value ="/extension/list", method=RequestMethod.GET)
	public List<ExtensionItem> getExtList(@RequestParam("type") String etype){
		String ls_exts = "SELECT row_to_json(row) from (SELECT e.extname AS \"name\", t.etype AS \"type\", e.extversion AS \"version\", n.nspname AS \"schema\", c.description AS \"description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        		+ "LEFT JOIN ext_type t ON e.extname = t.extname "
        		+ "WHERE t.etype = " + etype + ") row";
    	if (Integer.parseInt(etype) == 0) {
		  ls_exts = "SELECT row_to_json(row) from (SELECT e.extname AS \"name\", t.etype AS \"type\", e.extversion AS \"version\", n.nspname AS \"schema\", c.description AS \"description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        		+ "LEFT JOIN ext_type t ON e.extname = t.extname) row";
    	}
		List<ExtensionItem> extList = new LinkedList<ExtensionItem>();
		try {
			List<Map<String, Object>> rstList = jdbc.queryForList(ls_exts);
			ObjectMapper objectMapper = new ObjectMapper();
			for (Map<String, Object> m : rstList) {
				ExtensionItem ei = objectMapper.readValue(m.get("row_to_json").toString(), ExtensionItem.class);
				extList.add(ei);
			}
		} catch (DataAccessException de) {
			System.out.println("Exception: " + de);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return extList;
	}

	@RequestMapping(value ="/extension/list/{ext_name}", method=RequestMethod.GET)
	public ExtensionItem getExtInfo(@PathVariable("ext_name") String ext_name) {
		String ext_info = "SELECT row_to_json(row) from (SELECT e.extname AS \"name\", t.etype AS \"type\", e.extversion AS \"version\", n.nspname AS \"schema\", c.description AS \"description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        		+ "LEFT JOIN ext_type t ON e.extname = t.extname "
        		+ "where e.extname = '" + ext_name + "') row";
		ExtensionItem ext = new ExtensionItem();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, Object>> rstList = jdbc.queryForList(ext_info);
			ext = objectMapper.readValue(rstList.get(0).get("row_to_json").toString(), ExtensionItem.class);
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return ext;
	}

	/*@RequestMapping(value ="/extension/list", method=RequestMethod.GET)
	public String getExtListByType(@RequestParam("type") String etype) {
		String ls_exts = "SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        + "LEFT JOIN ext_type t ON e.extname = t.extname"
        + "WHERE t.etype = " + etype;
		try {
			Object obj = jdbc.queryForList(ls_exts);
			return obj.toString();
		} catch (DataAccessException de) {
			return "Exception: " + de;
		}
	}*/
	
	@RequestMapping(value = "/ext/install/{ext_name}", method=RequestMethod.GET)
	public String installExt(@PathVariable("ext_name") String ext_name) {
		String install = "create extension " + ext_name;
		try {
			jdbc.execute(install);
			return "Install Extension " + ext_name + " Success!";
		} catch (DataAccessException de) {
			return "Exception: " + de;
		}
	}
	
	@RequestMapping(value = "/ext/uninstall/{ext_name}", method=RequestMethod.GET)
	public String uninstallExt(@PathVariable("ext_name") String ext_name) {
		String install = "drop extension " + ext_name;
		try {
			jdbc.execute(install);
			return "Uninstall Extension " + ext_name + " Success!";
		} catch (DataAccessException de) {
			return "Exception: " + de;
		}
	}

	@RequestMapping(value = "/extension/test/{name}", method = RequestMethod.GET)
	public BarChartData getBarChartData(@PathVariable("name") String name){
		BarChartData barChartData = new BarChartData();

		try {
			barChartData = barChartDataService.getBarChartDataByExtension(name);
			//System.out.println("test");
		} catch (NullPointerException e) {
//			try {
//				barChartData = TestExtension(name);
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			} catch (InterruptedException ex) {
//				ex.printStackTrace();
//			}
			e.printStackTrace();
		}

//		if (barChartData == null) {
//			//System.out.println("line 141");
//			try {
//				barChartData = TestExtension(name);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

		return barChartData;
	}

	@RequestMapping(value = "/extension/test", method = RequestMethod.GET)
	public BarChartData TestExtension(@RequestParam("name") String name) throws IOException, InterruptedException {
		// todo: execution test script
		List<Double> simData = Arrays.asList(100.0, 16.9, 23.5, 45.7, 100.0, 19.0, 23.5, 45.7, 100.0, 60.0, 23.5, 45.7, 23.5, 45.7, 100.0, 90.0, 23.5, 45.7, 100.0, 78.0, 23.5, 45.7);
		TPCHTestProcess ttp = new TPCHTestProcess();
		simData = ttp.ExecuteTPCHTest(name);
		BarChartData barChartData = new BarChartData(name, simData);
		try {
			barChartData = barChartDataService.saveBarChartData(barChartData);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		System.out.println(barChartData);
		return barChartData;
	}
}


