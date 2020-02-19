package cn.ruc.xyy.jpev.model;

public class ExtensionItem {
    //@Id
    //@Column(name = "Name")
    String name;

    //@Column(name = "Type")
    int type;

    //@Column(name = "Version")
    String version;

    //@Column(name = "Schema")
    String schema;

    //@Column(name = "Description")
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExtensionItem() {
    }
}
