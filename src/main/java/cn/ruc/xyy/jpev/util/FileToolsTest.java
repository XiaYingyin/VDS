package cn.ruc.xyy.jpev.util;

import cn.ruc.xyy.jpev.model.FileNode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileToolsTest {
    @Test
    void executeTPCHTest() {
        List<FileNode> fnt = new ArrayList<>();
        String maindirpath = "/Users/liuchaoyang/Documents/gitRepo/test";

        // File object
        File maindir = new File(maindirpath);

        if(maindir.exists() && maindir.isDirectory())
        {
            // array for files and sub-directories
            // of directory pointed by maindir
            File arr[] = maindir.listFiles();

            System.out.println("**********************************************");
            System.out.println("Files from main directory : " + maindir);
            System.out.println("**********************************************");

            // Calling recursive method
            FileTools.RecursiveScan(arr, 0, 0, fnt, 0);
        }


        System.out.println("=================print tree=================");
        FileTools.RecursivePrint(fnt);
        FileTools.createProject("function", "ftree2", "/Users/liuchaoyang/Documents/gitRepo/", "1.1", "This is demo.");
        //System.out.println(FileTools.getFileContent("/Users/liuchaoyang/Documents/gitRepo/angular8-demo/src/app/app.component.ts"));
    }
}
