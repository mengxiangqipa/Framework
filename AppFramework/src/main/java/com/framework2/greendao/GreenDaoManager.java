package com.framework2.greendao;

import java.util.List;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.framework.utils.Y;

/**
 * 数据库管理类
 *
 * @author Yangjie
 *         className GreenDaoManager
 *         created at  2016/11/28  11:08
 */
public class GreenDaoManager<T> {
    private final static String dbName = "greenDao_51office";
    private static volatile GreenDaoManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    /**
     * 获取单例引用
     */
    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(@NonNull Context context) {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return openHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    @SuppressWarnings("unchecked")
    private SQLiteDatabase getWritableDatabase(@NonNull Context context) {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null) {
                //重写更新数据库的配置
                @Override
                public void onUpgrade(Database db, int oldVersion, int newVersion) {
                    Y.y("数据库更新:" + oldVersion + "    " + newVersion);
//                    MigrationHelper.getInstance().migrate(db, MessageInfoDao.class);
//                    backUpOldData(db, oldVersion, newVersion);//备份
//                    DaoMaster.dropAllTables(db, true);
//                    onCreate(db);
//                    restoreData(db, oldVersion, newVersion);//还原备份
                }
            };
        }
        return openHelper.getWritableDatabase();
    }

    /**
     * 初始化DaoMaster  DaoSession
     *
     * @param context 上下文
     */
    private void initDaoMasterAndDaoSession(@NonNull Context context) {
        if (daoMaster == null)
            daoMaster = new DaoMaster(getWritableDatabase(context));
        if (daoSession == null)
            daoSession = daoMaster.newSession();
    }

    /**
     * 插入一条记录
     * 大量操作请调用 {@link #insertEntityList(Context, Class, List)}
     *
     * @param entity 实体类
     * @return boolean 是否成功
     */
    public boolean insertEntity(@NonNull Context context, Object entity) {
        initDaoMasterAndDaoSession(context);
        try {
            daoSession.insert(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入多条记录
     *
     * @param list 用户对象列表
     * @return boolean 是否成功
     */
    @SuppressWarnings("unchecked")
    public boolean insertEntityList(@NonNull Context context, @NonNull Class<T> entityClass, @NonNull List<T> list) {
        if (list.isEmpty()) {
            return false;
        }
        initDaoMasterAndDaoSession(context);
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) daoSession.getDao(entityClass);
        try {
            dao.insertInTx(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一条记录
     * 大量操作请调用 {@link #deleteEntityList(Context, Class, List)}
     *
     * @param entity 用户对象
     * @return boolean 是否成功
     */
    public boolean deleteEntity(@NonNull Context context, @NonNull Object entity) {
        initDaoMasterAndDaoSession(context);
        try {
            daoSession.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一组记录
     *
     * @param list 用户对象列表
     * @return boolean 是否成功
     */
    @SuppressWarnings("unchecked")
    public boolean deleteEntityList(@NonNull Context context, @NonNull Class<T> entityClass, @NonNull List<T> list) {
        if (list.isEmpty())
            return false;
        initDaoMasterAndDaoSession(context);
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) daoSession.getDao(entityClass);
        try {
            dao.deleteInTx(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除所有记录
     *
     * @param entityClass 实体类名
     * @return boolean 是否成功
     */
    @SuppressWarnings("unchecked")
    public boolean deleteAll(@NonNull Context context, @NonNull Class<T> entityClass) {
        initDaoMasterAndDaoSession(context);
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) daoSession.getDao(entityClass);
        try {
            dao.deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新一条记录
     * 大量操作请调用 {@link #updateEntityList(Context, Class, List)}
     *
     * @param entity 用户对象
     * @return boolean 是否成功
     */
    public boolean updateEntity(@NonNull Context context, @NonNull Object entity) {
        initDaoMasterAndDaoSession(context);
        try {
            daoSession.update(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新一组记录
     *
     * @param list 用户对象列表
     * @return boolean 是否成功
     */
    @SuppressWarnings("unchecked")
    public boolean updateEntityList(@NonNull Context context, @NonNull Class<T> entityClass, @NonNull List<T> list) {
        initDaoMasterAndDaoSession(context);
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) daoSession.getDao(entityClass);
        try {
            dao.updateInTx(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询实体列表
     */
    @SuppressWarnings("unchecked")
    public List<T> queryEntityList(@NonNull Context context, @NonNull Class<T> entityClass) {
        initDaoMasterAndDaoSession(context);
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) daoSession.getDao(entityClass);
        QueryBuilder<T> queryBuilder = dao.queryBuilder();
        try {
            return queryBuilder.list();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询实体列表 --根据某一属性
     *
     * @param whereCondition UserDao.Properties.Age.gt(age)
     * @param property       UserDao.Properties.Age
     */
    @SuppressWarnings("unchecked")
    public List<T> queryEntityListWithCondition(@NonNull Context context, @NonNull Class<T> entityClass, @NonNull WhereCondition whereCondition,
                                                @Nullable Property property) {
        initDaoMasterAndDaoSession(context);
        AbstractDao<T, ?> dao = (AbstractDao<T, ?>) daoSession.getDao(entityClass);
        QueryBuilder<T> qb = dao.queryBuilder();
        qb.where(whereCondition);
        if (null != property)
            qb.orderDesc(property);
        try {
            return qb.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> queryEntityListWithCondition(@NonNull Context context, @NonNull Class<T> entityClass, @NonNull WhereCondition whereCondition) {
        return queryEntityListWithCondition(context, entityClass, whereCondition, null);
    }

    /**
     * <pre>
     * 备份旧数据--这个应该是每次在更新数据库版本的时候需要重新写的
     * 每个版本应该是不一样的
     * <pre/>
     */
    private void backUpOldData(Database db, int oldVersion, int newVersion) {

    }

    private List<T> listTemp;

    /**
     * <pre>
     * 恢复旧数据--这个应该是每次在更新数据库版本的时候需要重新写的
     * 每个版本应该是不一样的
     * <pre/>
     */
    private void restoreData(Database db, int oldVersion, int newVersion) {

    }
}
