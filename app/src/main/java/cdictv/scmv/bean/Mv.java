package cdictv.scmv.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Mv extends LitePalSupport implements Serializable {
    public  String username;
    public  String mimeType;
    public  String path;
    public  String biaoshi;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBiaoshi() {
        return biaoshi;
    }

    public void setBiaoshi(String biaoshi) {
        this.biaoshi = biaoshi;
    }
}
