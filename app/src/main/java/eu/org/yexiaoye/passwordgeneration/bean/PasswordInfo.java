package eu.org.yexiaoye.passwordgeneration.bean;

import java.io.Serializable;

public class PasswordInfo implements Serializable {
    private int index;
    private String pwdContent;
    private String remark;
    private String dateStored;

    public PasswordInfo() { }

    public PasswordInfo(int index, String pwdContent, String remark, String dateStored) {
        this.index = index;
        this.pwdContent = pwdContent;
        this.remark = remark;
        this.dateStored = dateStored;
    }

    public PasswordInfo(int index,String pwdContent, String remark) {
        this.index = index;
        this.pwdContent = pwdContent;
        this.remark = remark;
    }

    public PasswordInfo(String pwdContent) {
        this.pwdContent = pwdContent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPwdContent() {
        return pwdContent;
    }

    public void setPwdContent(String pwdContent) {
        this.pwdContent = pwdContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDateStored() {
        return dateStored;
    }

    public void setDateStored(String dateStored) {
        this.dateStored = dateStored;
    }

}
