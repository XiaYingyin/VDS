package cn.ruc.xyy.jpev.controller;

import cn.ruc.xyy.jpev.model.FileInfo;
import cn.ruc.xyy.jpev.model.ProjectInfo;
import cn.ruc.xyy.jpev.util.FileTools;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

import cn.ruc.xyy.jpev.model.FileNode;

@RestController
public class DevelopController {
    @RequestMapping(value ="/develop/test", method=RequestMethod.GET)
    public String getFolderTree () {
        System.out.println("test");
        return "Hello, develop!";
    }

    @RequestMapping(value ="/develop", method=RequestMethod.GET)
    public List<FileNode> scanFolder(@RequestParam("folder") String folder) {
        List<FileNode> fnt = new ArrayList<>();

        // File object
        File maindir = new File(folder);

        if (maindir.exists() && maindir.isDirectory())
        {
            // array for files and sub-directories
            // of directory pointed by maindir
            File arr[] = maindir.listFiles();

            // Calling recursive method
            FileTools.RecursiveScan(arr, 0, 0, fnt, 0);
        }
        return fnt;
    }

    @RequestMapping(value = "/develop/file", method = RequestMethod.GET)
    public FileInfo getFileInfo(@RequestParam("path") String path) {
        File myObj = new File(path);
        FileInfo fileInfo = new FileInfo();

        if (myObj.exists()) {
            fileInfo.setIfExist(true);
            fileInfo.setName(myObj.getName());
            fileInfo.setAbsolutePath(myObj.getAbsolutePath());
            fileInfo.setWriteable(myObj.canWrite());
            fileInfo.setReadable(myObj.canRead());
            fileInfo.setSize(myObj.length());
            fileInfo.setContent(FileTools.getFileContent(myObj));
        } else {
            //System.out.println("The file does not exist.");
            fileInfo.setIfExist(false);
        }

        return fileInfo;
    }

    @PostMapping("/develop/project")
    public ProjectInfo createProject(@RequestBody ProjectInfo projectInfo) {
        System.out.println("get: " + projectInfo.getName());
        boolean success = FileTools.createProject(projectInfo.getType(), projectInfo.getName(), projectInfo.getPath(), projectInfo.getVersion(), projectInfo.getDescription());
        if (success)
            return projectInfo;
        else return null;
    }


}
