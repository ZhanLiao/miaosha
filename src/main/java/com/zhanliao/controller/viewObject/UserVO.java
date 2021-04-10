package com.zhanliao.controller.viewObject;

/**
 * @Author: ZhanLiao
 * @Description: 有些用户信息是不能返回给前端的，或者针对前端框架要求返回的数据格式而做设定
 * @Date: 2021/3/9 16:01
 * @Version: 1.0
 */
public class UserVO {
    private Integer id;
    private String name;
    private Integer gender;
    private Integer age;
    private String telephone;

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
