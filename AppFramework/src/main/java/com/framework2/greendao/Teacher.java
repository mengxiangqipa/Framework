package com.framework2.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by xt01 on 2016/9/12.
 */
@Entity
public class Teacher {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String teacherTag;

    @Keep
    public Teacher(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Keep
    public Teacher() {
    }

    @Generated(hash = 1180156097)
    public Teacher(Long id, String name, String teacherTag) {
        this.id = id;
        this.name = name;
        this.teacherTag = teacherTag;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherTag() {
        return this.teacherTag;
    }

    public void setTeacherTag(String teacherTag) {
        this.teacherTag = teacherTag;
    }
}
