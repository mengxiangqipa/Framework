package com.framework2.greendao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

@Entity
public class User {
    @Id(autoincrement = true)
    private Long autoincrementId;
    private long id;
    private String name;
    private String userTag;//关联件
    private int age;
    @ToOne(joinProperty = "id")
    private User user;
    //    @ToMany(referencedJoinProperty = "teacherTag")
//    @OrderBy(value = "id ASC")
    @ToMany(joinProperties = {@JoinProperty(name = "userTag", referencedName = "teacherTag")})
    @OrderBy(value = "id ASC")
    private List<Teacher> list;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;
    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    @Generated(hash = 2072477915)
    public User(Long autoincrementId, long id, String name, String userTag, int age) {
        this.autoincrementId = autoincrementId;
        this.id = id;
        this.name = name;
        this.userTag = userTag;
        this.age = age;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getAutoincrementId() {
        return this.autoincrementId;
    }

    public void setAutoincrementId(Long autoincrementId) {
        this.autoincrementId = autoincrementId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 866595985)
    public User getUser() {
        long __key = this.id;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 549177597)
    public void setUser(@NotNull User user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            id = user.getAutoincrementId();
            user__resolvedKey = id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public String getUserTag() {
        return this.userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 579911406)
    public List<Teacher> getList() {
        if (list == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeacherDao targetDao = daoSession.getTeacherDao();
            List<Teacher> listNew = targetDao._queryUser_List(userTag);
            synchronized (this) {
                if (list == null) {
                    list = listNew;
                }
            }
        }
        return list;
    }

    public void setList(List<Teacher> list) {
        this.list = list;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 589833612)
    public synchronized void resetList() {
        list = null;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
}
