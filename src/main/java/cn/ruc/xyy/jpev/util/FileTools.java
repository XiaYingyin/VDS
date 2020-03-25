package cn.ruc.xyy.jpev.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.ruc.xyy.jpev.model.FileNode;
import com.google.common.io.Files;

public class FileTools {
	private String mainPath;
	private List<FileNode> fileTree;

	static public void RecursiveScan(File[] arr, int index, int level, List<FileNode> FNList, int pid) {
		// terminate condition
		if(index == arr.length)
			return;
		int id = pid;

		// tabs for internal levels
//		for (int i = 0; i < level; i++)
//			System.out.print("\t");

		// for files
		if(arr[index].isFile()) {
			String name = arr[index].getName();
			String suffix = getExtensionByGuava(name);
			String path = arr[index].getPath();
			//int id = pid + 1;
			FileNode node = new FileNode(++id, name, path, pid, suffix,"file");
			FNList.add(node);
			System.out.println(arr[index].getName());
		}


			// for sub-directories
		else if(arr[index].isDirectory())
		{
			System.out.println("[" + arr[index].getName() + "]");
			String name = arr[index].getName();
			String path = arr[index].getPath();
			//int id = pid + 1;
			FileNode node = new FileNode(id++, name, path, pid, "","folder");
			List<FileNode> DFNList = new ArrayList<FileNode>();
			node.setChildren(DFNList);
			// recursion for sub-directories
			RecursiveScan(arr[index].listFiles(), 0, level + 1, DFNList, pid);
			FNList.add(node);
		}

		// recursion for main directory
		RecursiveScan(arr,++index, level, FNList, pid);
	}

	public static void RecursivePrint(List<FileNode> fnt) {
		for (FileNode fn: fnt) {
			if (fn.getType().equals("file")) {
				System.out.println(fn.getName());
			} else {
				System.out.println(fn.getName());
				RecursivePrint(fn.getChildren());
			}
		}
	}

	public static String getExtensionByGuava(String filename) {
		return Files.getFileExtension(filename);
	}

	public static String getFileContent(File myObj) {
		StringBuffer fileContent = new StringBuffer();
		try {
			//File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				fileContent.append(data);
				fileContent.append("\n");
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			//System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return fileContent.toString();
	}

	public static boolean createProject(String type, String name, String path, String version, String description) {
		path = path + name;
		System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			String header_file = "#include \"access/amapi.h\"\n" +
					"#include \"access/itup.h\"\n" +
					"#include \"access/sdir.h\"\n" +
					"#include \"access/xlogreader.h\"\n" +
					"#include \"catalog/pg_index.h\"\n" +
					"#include \"catalog/pg_type.h\"\n" +
					"#include \"lib/stringinfo.h\"\n" +
					"#include \"storage/bufmgr.h\"\n" +
					"#include \"storage/shm_toc.h\"\n" +
					"//#include \"nodes/relation.h\"\n" +
					"#include \"utils/selfuncs.h\"\n" +
					"#include \"utils/lsyscache.h\"\n" +
					"#include \"utils/syscache.h\"\n" +
					"#include \"optimizer/cost.h\"\n" +
					"#include \"parser/parsetree.h\"";

			// *.h
			BufferedWriter h_bw = new BufferedWriter(new FileWriter(path + '/' + name + ".h"));
			h_bw.write(header_file);
			String index_build_func = "extern IndexBuildResult *" + name + "_build(Relation heap, Relation index,\n" +
					"\t\tstruct IndexInfo *indexInfo);\n";
			String index_insert_func = "extern bool " + name + "_doinsert(Relation rel, IndexTuple itup,\n" +
					"\t\t\t IndexUniqueCheck checkUnique, Relation heapRel);\n";
			String index_search_func = "extern Stack " + name + "_search(Relation rel,\n" +
					"\t\t   int keysz, ScanKey scankey, bool nextkey,\n" +
					"\t\t   Buffer *bufP, int access, Snapshot snapshot);\n";
			String index_costestimate = "extern void " + name + "_costestimate(struct PlannerInfo *root,\n" +
					"\t\t\t   struct IndexPath *path,\n" +
					"\t\t\t   double loop_count,\n" +
					"\t\t\t   Cost *indexStartupCost,\n" +
					"\t\t\t   Cost *indexTotalCost,\n" +
					"\t\t\t   Selectivity *indexSelectivity,\n" +
					"\t\t\t   double *indexCorrelation,\n" +
					"\t\t\t   double *indexPages);\n";
			h_bw.write(index_build_func + index_insert_func + index_search_func + index_costestimate);
			h_bw.close();

			header_file = "#include <math.h>\n" +
					"#include \"postgres.h\"\n" +
					"#include \"access/relscan.h\"\n" +
					"#include \"access/xlog.h\"\n" +
					"#include \"commands/vacuum.h\"\n" +
					"#include \"miscadmin.h\"\n" +
					"#include \"nodes/execnodes.h\"\n" +
					"#include \"pgstat.h\"\n" +
					"#include \"postmaster/autovacuum.h\"\n" +
					"#include \"xpg_storage.h\"\n" +
					"#include \"xpg_util.h\"\n" +
					"\n" +
					"#include \"" + name + ".h\"\n";

			// *.c
			BufferedWriter cbw = new BufferedWriter(new FileWriter(path + '/' + name + ".c"));
			cbw.write(header_file);
			cbw.write("PG_MODULE_MAGIC;\n");
			cbw.write("PG_FUNCTION_INFO_V1(" + name + "_handler);\n");

			cbw.write("Datum " + name +
					"_handler(PG_FUNCTION_ARGS)\n" +
					"{");
			cbw.write("IndexAmRoutine *amroutine = makeNode(IndexAmRoutine);\n" +
					"\n" +
					"\tamroutine->amstrategies = BTMaxStrategyNumber;\n" +
					"\tamroutine->amsupport = BTNProcs;\n" +
					"\tamroutine->amcanorder = true;\n" +
					"\tamroutine->amcanorderbyop = false;\n" +
					"\tamroutine->amcanbackward = true;\n" +
					"\tamroutine->amcanunique = true;\n" +
					"\tamroutine->amcanmulticol = true;\n" +
					"\tamroutine->amoptionalkey = true;\n" +
					"\tamroutine->amsearcharray = true;\n" +
					"\tamroutine->amsearchnulls = true;\n" +
					"\tamroutine->amstorage = false;\n" +
					"\tamroutine->amclusterable = true;\n" +
					"\tamroutine->ampredlocks = true;\n" +
					"\tamroutine->amcanparallel = false;\n" +
					"\tamroutine->amcaninclude = true;\n" +
					"\tamroutine->amkeytype = InvalidOid;\n");

			cbw.write("amroutine->ambuild = " + name + "_build;\n" +
					"\tamroutine->ambuildempty = " + name + "_buildempty;\n" +
					"\tamroutine->aminsert = " + name + "_insert;\n" +
					"\tamroutine->ambulkdelete = " + name + "_bulkdelete;\n"+
					"\tamroutine->amcanreturn = " + name + "_canreturn;\n" +
					"\tamroutine->amcostestimate = " + name + "_costestimate;\n");
			cbw.write("\tPG_RETURN_POINTER(amroutine);\n" +
					"}\n");
			cbw.close();


			// *.sql
			String sql_info = "\\echo Use \"CREATE EXTENSION " + name + "\" to load this file. \\quit\n" +
					"LOAD '$libdir/" + name + "';\n" +
					"\n" +
					"CREATE OR REPLACE FUNCTION " + name + "_handler(internal) RETURNS index_am_handler AS\n" +
					"'$libdir/" + name + "' LANGUAGE C; \n" +
					"CREATE ACCESS METHOD " + name + " TYPE INDEX HANDLER " + name + "_handler;\n";

			BufferedWriter sql_bw = new BufferedWriter(new FileWriter(path + '/' + name + "--" + version + ".sql"));
			sql_bw.write(sql_info);
			sql_bw.close();

			String control_info = "comment = " + description + "\n" +
					"default_version = " + version + "\n" +
					"module_pathname = '$libdir/" + name + "'\n" +
					"relocatable = true";
			BufferedWriter control_bw = new BufferedWriter(new FileWriter(path + '/' + name + ".control"));
			control_bw.write(control_info);
			control_bw.close();

			BufferedWriter makefile_bw = new BufferedWriter(new FileWriter(path + '/' + "Makefile"));
			makefile_bw.write("EXTENSION = " + name + '\n');
			makefile_bw.write("EXTVERSION = " + version + '\n');
			makefile_bw.write("PG_CONFIG ?= pg_config" + '\n');
			makefile_bw.write("MODULE_big = " + name + '\n');

			makefile_bw.write("OBJS = name.o" + '\n');

			String other_info = "DATA = $(wildcard *--*.sql)\n" +
					"PGXS := $(shell $(PG_CONFIG) --pgxs)\n" +
					"include $(PGXS)\n" +
					"\n" +
					"DEBUILD_ROOT = /tmp/$(EXTENSION)\n" +
					"\n" +
					"deb: release-zip\n" +
					"\tmkdir -p $(DEBUILD_ROOT) && rm -rf $(DEBUILD_ROOT)/*\n" +
					"\tunzip ./${EXTENSION}-$(EXTVERSION).zip -d $(DEBUILD_ROOT)\n" +
					"\tcd $(DEBUILD_ROOT)/${EXTENSION}-$(EXTVERSION) && make -f debian/rules orig\n" +
					"\tcd $(DEBUILD_ROOT)/${EXTENSION}-$(EXTVERSION) && debuild -us -uc -sa";
			makefile_bw.write(other_info);
			makefile_bw.close();

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}