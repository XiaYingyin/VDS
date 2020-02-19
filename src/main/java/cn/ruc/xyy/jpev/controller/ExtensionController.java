package cn.ruc.xyy.jpev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://10.77.110.134:4200")
public class ExtensionController {

	public ExtensionController() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private JdbcTemplate jdbc;

	@RequestMapping(value ="/extension/list", method=RequestMethod.GET)
	public String getExtList(@RequestParam("type") String etype){
		String ls_exts = "SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        + "LEFT JOIN ext_type t ON e.extname = t.extname "
        + "WHERE t.etype = " + etype;
    if (Integer.parseInt(etype) == -1) {
		  ls_exts = "SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        + "LEFT JOIN ext_type t ON e.extname = t.extname";
    }
		try {
			Object obj = jdbc.queryForList(ls_exts);
			return obj.toString();
		} catch (DataAccessException de) {
			return "Exception: " + de;
		}
	}

	@RequestMapping(value ="/extension/list/{ext_name}", method=RequestMethod.GET)
	public String getExtInfo(@PathVariable("ext_name") String ext_name) {
		String ext_info = "SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" "
				+ "FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND " 
				+ "c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass "
        + "LEFT JOIN ext_type t ON e.extname = t.extname"
        + "where e.extname = '" + ext_name + "'";
		try {
			Object obj = jdbc.queryForList(ext_info);
			return obj.toString();
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


