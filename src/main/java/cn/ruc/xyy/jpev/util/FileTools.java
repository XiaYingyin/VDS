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
		path = path + '/' + name;

		File file = new File(path);
		if (!file.exists()) {
			System.out.println("path not found: " + path);
			file.mkdir();
		}
		try {
			ClassPathResource classPathResource = new ClassPathResource("ExtTemplate/index/");
//			FileUtils.copyDirectory(classPathResource.getFile(), new File(path));
			copy(classPathResource.getFile(), new File(path));
			String handler_name = name + "_handler";
			// sql file
			String sql_info = "\\echo Use \"CREATE EXTENSION " + name + "\" to load this file. \\quit\n" +
					"LOAD '$libdir/" + name + "';\n" +
					"\n" +
					"CREATE OR REPLACE FUNCTION " + name + "_handler(internal) RETURNS index_am_handler AS\n" +
					"'$libdir/" + name + "' LANGUAGE C; \n" +
					"CREATE ACCESS METHOD " + name + " TYPE INDEX HANDLER " + handler_name + "\n";
			sql_info = sql_info + "-- Add new operators --\n";

			BufferedWriter sql_bw = new BufferedWriter(new FileWriter(path + '/' + name + "--" + version + ".sql"));
			sql_bw.write(sql_info);
			sql_bw.close();

			// control file
			String control_info = "comment = '" + description + "'\n" +
					              "default_version = '" + version + "'\n" +
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

			makefile_bw.write("OBJS = src/" + name + ".o " + "src/insert.o src/scan.o src/delete.o src/cost.o src/compare.o src/build.o" + '\n');

			String other_info = "DATA = $(wildcard *--*.sql)\n" +
					"PGXS := $(shell $(PG_CONFIG) --pgxs)\n" +
					"include $(PGXS)\n" +
					"\n" +
					"DEBUILD_ROOT = /tmp/$(EXTENSION)\n";
			makefile_bw.write(other_info);
			makefile_bw.close();

			// README
			BufferedWriter readme_bw = new BufferedWriter(new FileWriter(path + '/' + "README.md"));


			readme_bw.write("# " + name + '\n');
			readme_bw.write(description + '\n');
			readme_bw.close();

			renameFile(path + "/src/ext_name.c", name + ".c", true);
			replaceFileStr(path + "/src/" + name + ".c", "__handler__", name + "_handler");
//			renameFile(path + "/src/ext_name.c", path + "/src/" + name + ".c", true);
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

	public static boolean replaceFileStr(String filepath, String sourceStr, String targetStr) {
		try {
			FileReader fis = new FileReader(filepath); // 创建文件输入流
//			BufferedReader br = new BufferedReader(fis);
			char[] data = new char[1024]; // 创建缓冲字符数组
			int rn = 0;
			StringBuilder sb = new StringBuilder(); // 创建字符串构建器
			// fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O
			// 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
			while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
				String str = String.valueOf(data, 0, rn);// 把数组转换成字符串
//				System.out.println(str);
				sb.append(str);
			}
			fis.close();// 关闭输入流
			// 从构建器中生成字符串，并替换搜索文本
			String str = sb.toString().replace(sourceStr, targetStr);
			FileWriter fout = new FileWriter(filepath);// 创建文件输出流
			fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
			fout.close();// 关闭输出流

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean renameFile(String oldFilePath, String newFileName, boolean overriding){
		File oldfile = new File(oldFilePath);
		if(!oldfile.exists()){
			return false;
		}
		String newFilepath = oldfile.getParent()+File.separator+newFileName;
		File newFile = new File(newFilepath);
		if(!newFile.exists()){
			return oldfile.renameTo(newFile);
		}else{
			if(overriding){
				newFile.delete();
				return oldfile.renameTo(newFile);
			}else{
				return false;
			}
		}
	}

	public static void copy(File f1, File f2) throws IOException{
		long totalSize = 0;
		//如果目标不是目录，直接退出
		if(!f2.isDirectory()){
			return ;
		}
		//源文件是目录，循环所有子文件
		if(f1.isDirectory()){
			File[] subFiles = f1.listFiles();
			for(int i=0;i<subFiles.length;i++){
				String newFileName = f2.getPath()+"/"+subFiles[i].getName();
				File newFile = new File(newFileName);
				//子文件是目录则递归拷贝
				if(subFiles[i].isDirectory()){
					if(!newFile.exists()){
						newFile.mkdir();
					}else{
					}
					copy(subFiles[i], newFile);
				}else{//子文件是文件，则直接拷贝
					copy(subFiles[i],f2);
				}
			}
		}else{//源文件是文件直接拷贝
			String newFileName = f2.getPath()+"/"+f1.getName();
			File newFile = new File(newFileName);
			//是否覆盖拷贝
            /*if(newFile.exists()){
                return ;
            }*/
			newFile.createNewFile();
			FileInputStream fis = new FileInputStream(f1);
			BufferedInputStream bis = new BufferedInputStream(fis, 8192);
			FileOutputStream fos = new FileOutputStream(newFileName);
			BufferedOutputStream bos = new BufferedOutputStream(fos,8192);
			byte[] b = new byte[8192];
			int count = -1;
			while((count=bis.read(b))!=-1){
				totalSize += count;
				bos.write(b,0,count);
			}
			bis.close();
			bos.close();
			fis.close();
			fos.close();
		}
	}
}