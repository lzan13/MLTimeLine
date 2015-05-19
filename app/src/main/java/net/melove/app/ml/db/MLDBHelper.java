package net.melove.app.ml.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.melove.app.ml.MLApp;
import net.melove.app.ml.activity.MLSignActivity;
import net.melove.app.ml.config.MLConfig;

import java.io.File;

/**
 * Created by Administrator on 2014/12/18.
 */
public class MLDBHelper {

    private String DBName = MLDBConstants.DB_NAME;
    private static MLDBHelper instance;

    private SQLiteDatabase mDB = null;

    private MLDBHelper() {
    }

    /**
     * 单例模式获取 获取数据库操作类实例，同时打开数据库
     *
     * @return
     */
    public static synchronized MLDBHelper getInstance() {
        if (instance == null) {
            instance = new MLDBHelper();
        }
        if (!instance.openDatabase()) {
            instance.closeDatabase();
            return null;
        }
        return instance;
    }

    private boolean openDatabase() {
        File file = new File(MLApp.getDb() + DBName);
        if (!file.exists()) {
            return false;
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(file, null);
        return true;
    }


    public void initTable() {
        mDB.execSQL(MLDBConstants.CREATE_TABLE_USER);
        mDB.execSQL(MLDBConstants.CREATE_TABLE_NOTE);
        mDB.execSQL(MLDBConstants.CREATE_TABLE_REPLY);
        mDB.execSQL(MLDBConstants.CREATE_TABLE_MESSAGE);
        mDB.close();
    }


    /**
     * ------------- 数据库的增删改查操作 ----------------
     */

    /**
     * sql语句方式插入数据
     * sqlStr = "insert into table(col1, col2, ... coln) values('value1', 'value2', ... valuen)";
     * or
     * sqlStr = "insert into table(col1, col2, ... coln) values(?, ?, ... ?)"
     * args = {value1, value2, ... valuen};
     * mDB.execSQL(sqlStr, args);
     *
     * @param sqlStr 进行操作的sql语句
     */
    public void insterData(String sqlStr) {
        mDB.execSQL(sqlStr);
    }

    /**
     * 插入数据 第二种方式
     * 使用ContentValues
     * ContentValues values = new ContentValues();
     * values.put("col1", "value1");
     * values.put("col2", "value2");
     *
     * @param table  要插入的表
     * @param values 需要插入的值
     */
    public long insterData(String table, ContentValues values) {
        long result = mDB.insert(table, null, values);
        return result;
    }

    /**
     * 删除数据 1.通过执行sql语句
     * sqlStr = "delete from table where id=?";
     * args = {"1", "3"};
     *
     * @param sqlStr 进行操作的sql语句
     * @param args   替换sql语句中的值
     */
    public void delete(String sqlStr, String[] args) {
        mDB.execSQL(sqlStr, args);
    }

    /**
     * 删除数据 2.通过封装好的方法
     * whereClause = "id=?";(null 删除所有内容)
     * args = {"1", 3};
     *
     * @param table       要删除的数据所在的表
     * @param whereClause 要删除的过滤条件，如果为null则删除整个表的内容
     * @param args        要删除的数据条件值
     *                    return result      返回影响的行数
     */
    public long delete(String table, String whereClause, String[] args) {
        long result = mDB.delete(table, whereClause, args);
        return result;
    }

    /**
     * 修改数据 1.通过执行sql语句修改
     * sqlStr = "update table set col1='value' where id=1";
     *
     * @param sqlStr 进行操作的sql语句
     */
    public void updateData(String sqlStr) {
        mDB.execSQL(sqlStr);
    }

    /**
     * 修改数据 2.通过使用ContentValues
     * values = new ContentValues();
     * values.put("col1", "value1");
     * whereClause = "id=1";
     * args = "1";
     *
     * @param table       修改数据所在的表
     * @param values      要修改的值
     * @param whereClause 需要修改的数据查询条件
     * @param args        修改数据查询条件的值
     * @return            返回影响的行
     */
    public long updateData(String table, ContentValues values, String whereClause, String[] args) {
        long result = mDB.update(table, values, whereClause, args);
        return result;
    }

    /**
     * 查询数据 1.执行原生的select查询语句
     * sqlStr = "select from table";
     *
     * @param sqlStr 执行查询的sql语句
     * @return cursor 返回查询到的游标，通过游标根据列名获取数据
     */
    public Cursor queryData(String sqlStr) {
        Cursor cursor = mDB.rawQuery(sqlStr, null);
        return cursor;
    }

    /**
     * 查询数据 2.通过goole封装的query方法查询
     *
     * @param table     表名
     * @param columns   指定查询的列，如果不指定则查询所有
     * @param selection 查询过滤条件
     * @param args      查询过滤条件对应的值
     * @param groupBy   分组依据
     * @param having    分组后的过滤条件
     * @param orderBy   排序方式
     * @param limit     分页方式
     * @return 返回查询到的游标，通过游标根据列名获取数据
     */
    public Cursor queryData(String table, String[] columns, String selection, String[] args,
                            String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = mDB.query(table, columns, selection, args, groupBy, having, orderBy, limit);
        return cursor;
    }

    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        if (mDB != null) {
            if (mDB.isOpen()) {
                mDB.close();
            }
        }
    }
}
