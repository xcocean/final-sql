package top.lingkang.finalsql.g;

/**
 * @author lingkang
 * Created by 2022/4/19
 */
public class GColumn {
    private String tableName;
    private String name;
    private String type;
    private String length;
    private boolean isPri;
    private String comment;

    @Override
    public String toString() {
        return "GColumn{" +
                "tableName='" + tableName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", length='" + length + '\'' +
                ", isPri=" + isPri +
                ", comment='" + comment + '\'' +
                '}';
    }

    public GColumn(String tableName, String name, String type, String length, boolean isPri, String comment) {
        this.tableName = tableName;
        this.name = name;
        this.type = type;
        this.length = length;
        this.isPri = isPri;
        this.comment = comment;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPri() {
        return isPri;
    }

    public void setPri(boolean pri) {
        isPri = pri;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
