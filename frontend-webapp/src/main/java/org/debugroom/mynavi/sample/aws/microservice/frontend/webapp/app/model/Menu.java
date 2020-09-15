package org.debugroom.mynavi.sample.aws.microservice.frontend.webapp.app.model;

public enum Menu {

    LOGOUT("ログアウト", "/frontend/logout"),
    PORTAL("ポータル", "/frontend/portal"),
    USER_MANAGEMENT("ユーザ管理", "/frontend/user-management");

    private String name;
    private String path;

    private Menu(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}
