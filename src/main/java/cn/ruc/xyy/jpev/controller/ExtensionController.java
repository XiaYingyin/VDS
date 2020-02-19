package cn.ruc.xyy.jpev.controller;

import cn.ruc.xyy.jpev.model.ExtensionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	@RequestMapping(value ="/extension/list", method=RequestMethod.GET)
	public String getExtList(@RequestParam("type") String etype){
		String ls_exts = "SELECT row_to_json(row) from (SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        		+ "LEFT JOIN ext_type t ON e.extname = t.extname "
        		+ "WHERE t.etype = " + etype + ") row";
    	if (Integer.parseInt(etype) == 0) {
		  ls_exts = "SELECT row_to_json(row) from (SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        		+ "LEFT JOIN ext_type t ON e.extname = t.extname) row";
    	}
		try {
			List<Map<String, Object>> rstList = jdbc.queryForList(ls_exts);
			StringBuffer jsonString = new StringBuffer();
			jsonString.append("[");
			for (Map<String, Object> m : rstList) {
				//System.out.println(m.get("row_to_json"));
				ExtensionItem e = ((ExtensionItem) m.get("row_to_json"));
				System.out.println(e.toString());
				jsonString.append(m.get("row_to_json").toString() + ",");
			}
			if (jsonString.length() > 1)
				jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			System.out.println(jsonString.toString());
			return jsonString.toString();
		} catch (DataAccessException de) {
			return "Exception: " + de;
		}
	}

	@RequestMapping(value ="/extension/list/{ext_name}", method=RequestMethod.GET)
	public String getExtInfo(@PathVariable("ext_name") String ext_name) {
		String ext_info = "SELECT row_to_json(row) from (SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        + "LEFT JOIN ext_type t ON e.extname = t.extname "
        + "where e.extname = '" + ext_name + "') row";
		try {
			//Object obj = jdbc.queryForList(ext_info);
			List<Map<String, Object>> rstList = jdbc.queryForList(ext_info);
			StringBuffer jsonString = new StringBuffer();
			jsonString.append("[");
			for (Map<String, Object> m : rstList) {
				//System.out.println(m.get("row_to_json"));
				jsonString.append(m.get("row_to_json").toString() + ",");
			}
			if (jsonString.length() > 1)
				jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			System.out.println(jsonString.toString());
			return jsonString.toString();
			//return rst;
		} catch (DataAccessException de) {
			return "Exception: " + de;
		}
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
}


