package cn.ruc.xyy.jpev.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cn.ruc.xyy.jpev.model.FileNode;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

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
			// sql file
			String sql_info = "\\echo Use \"CREATE EXTENSION " + name + "\" to load this file. \\quit\n" +
					"LOAD '$libdir/" + name + "';\n" +
					"\n" +
					"CREATE OR REPLACE FUNCTION " + name + "_handler(internal) RETURNS index_am_handler AS\n" +
					"'$libdir/" + name + "' LANGUAGE C; \n" +
					"CREATE ACCESS METHOD " + name + " TYPE INDEX HANDLER " + name + "_handler;\n";
			sql_info = sql_info + "-- Add new operators --\n";

			BufferedWriter sql_bw = new BufferedWriter(new FileWriter(path + '/' + name + "--" + version + ".sql"));
			sql_bw.write(sql_info);
			sql_bw.close();

			// control file
			String control_info = "comment = " + description + "\n" +
					              "default_version = " + version + "\n" +
					              "module_pathname = '$libdir/" + name + "'\n" +
					              "relocatable = true";
			BufferedWriter control_bw = new BufferedWriter(new FileWriter(path + '/' + name + ".control"));
			control_bw.write(control_info);
			control_bw.close();

			// makefile
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
					"DEBUILD_ROOT = /tmp/$(EXTENSION)\n";
			makefile_bw.write(other_info);
			makefile_bw.close();

			ClassPathResource classPathResource = new ClassPathResource("ExtTemplate/index/");
			FileUtils.copyDirectory(classPathResource.getFile(), new File(path));

		}catch(IOException e){
			e.printStackTrace();
			return false;
		}

		return true;
	}

	static public void copyFile(String source, String desc) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(desc).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
}