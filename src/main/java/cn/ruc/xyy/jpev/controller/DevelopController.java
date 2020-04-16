package cn.ruc.xyy.jpev.controller;

import cn.ruc.xyy.jpev.model.BuildInfo;
import cn.ruc.xyy.jpev.model.FileInfo;
import cn.ruc.xyy.jpev.model.ProjectInfo;
import cn.ruc.xyy.jpev.util.BuildProcess;
import cn.ruc.xyy.jpev.util.FileTools;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.ruc.xyy.jpev.model.FileNode;

@RestController
public class DevelopController {
    @RequestMapping(value ="/develop/test", method=RequestMethod.GET)
    public String getFolderTree () {
        System.out.println("test");
        return "Hello, develop!";
    }

    // 用于扫描工程路径
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

    // 获取文件的具体信息
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

    // 创建工程
    @PostMapping("/develop/project")
    public ProjectInfo createProject(@RequestBody ProjectInfo projectInfo) {
        System.out.println("get: " + projectInfo.getName());
        boolean success = FileTools.createProject(projectInfo.getType(), projectInfo.getName(), projectInfo.getPath(), projectInfo.getVersion(), projectInfo.getDescription());
        if (success)
            return projectInfo;
        else return null;
    }

    // 编译工程
    @RequestMapping(value = "/develop/build", method = RequestMethod.GET)
    public BuildInfo buildProject(@RequestParam("path") String path) {
        String rst = BuildProcess.buildProject(path);
        System.out.println(rst);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BuildInfo bi = new BuildInfo(df.format(new Date()), rst);
        return bi;
    }

    // 安装扩展
    @RequestMapping(value = "/develop/install/{name}", method = RequestMethod.GET)
    public String installExtension(@RequestParam("path") String path, @PathVariable("name") String name) {
        String rst = BuildProcess.installExtension(path, name);
        System.out.println(rst);
        return rst;
    }

    // 创建文件
    @RequestMapping(value = "/develop/file/{name}", method = RequestMethod.GET)
    public String createFile(@RequestParam("path") String path, @PathVariable("name") String name) {
        return FileTools.createFile(path, name);
    }
}
