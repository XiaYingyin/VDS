package cn.ruc.xyy.jpev.model;

public class FileInfo {
    private String name;
    private long size;    // bytes
    private String content;
    private boolean Writeable;
    private boolean Readable;
    private String absolutePath;
    private boolean ifExist;

    public boolean isIfExist() {
        return ifExist;
    }

    public void setIfExist(boolean ifExist) {
        this.ifExist = ifExist;
    }

    public FileInfo() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isWriteable() {
        return Writeable;
    }

    public void setWriteable(boolean writeable) {
        Writeable = writeable;
    }

    public boolean isReadable() {
        return Readable;
    }

    public void setReadable(boolean readable) {
        Readable = readable;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}
