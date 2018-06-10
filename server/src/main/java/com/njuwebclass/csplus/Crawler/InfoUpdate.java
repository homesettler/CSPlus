package com.njuwebclass.csplus.Crawler;

public class InfoUpdate {
    private String className;
    private String homework;
    private String type;

    public InfoUpdate(String className, String homework, String type){
        this.className=className;
        this.homework =homework;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof InfoUpdate){
            InfoUpdate info = (InfoUpdate) obj;
            if(info.getClassName().equals(className)&&info.getHomework().equals(homework)&&info.getType().equals(type))
                return true;
        }
        return false;
    }


    public void show(){
        System.out.println(className+"  "+ homework+"  "+type);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
