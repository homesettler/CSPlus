package com.njuwebclass.csplus.Crawler;
import java.util.Date;

public class DDLInfo implements Comparable<DDLInfo>{
    private String className;
    private String homework;
    private Date ddl;

    public DDLInfo(String className, String homework, Date ddl){
        this.className =className;
        this.homework = homework;
        this.ddl =ddl;
    }

    public void show(){
        System.out.println(className+"  "+homework+"  "+ddl);
    }

    @Override
    public int compareTo(DDLInfo ddlInfo){
        return this.ddl.compareTo(ddlInfo.ddl);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public Date getDdl() {
        return ddl;
    }

    public void setDdl(Date ddl) {
        this.ddl = ddl;
    }
}
