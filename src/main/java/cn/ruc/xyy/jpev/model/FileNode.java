package cn.ruc.xyy.jpev.model;

import java.util.List;

public class FileNode {
    private Integer id;
    private String name;
    private String path;
    private Integer parentId;
    private List<FileNode> children;
    private String suffix;
    private String type;

    public FileNode(Integer id, String name, String path, Integer parentId, String suffix, String type) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.parentId = parentId;
        this.suffix = suffix;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<FileNode> getChildren() {
        return children;
    }

    public void setChildren(List<FileNode> children) {
        this.children = children;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
