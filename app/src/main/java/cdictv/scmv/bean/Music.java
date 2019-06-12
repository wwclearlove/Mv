package cdictv.scmv.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Music extends LitePalSupport implements Serializable {
    public  String biaoshi;

    public String getBiaoshi() {
        return biaoshi;
    }

    public void setBiaoshi(String biaoshi) {
        this.biaoshi = biaoshi;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public  String uri;
}
