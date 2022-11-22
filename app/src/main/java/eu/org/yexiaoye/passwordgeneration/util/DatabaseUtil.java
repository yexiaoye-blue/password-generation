package eu.org.yexiaoye.passwordgeneration.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseUtil extends SQLiteOpenHelper {

    public static volatile DatabaseUtil databaseUtilInstance;

    private SQLiteDatabase mSQLiteDatabase;

    private DatabaseUtil(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public static DatabaseUtil getInstance(Context context) {
        if (databaseUtilInstance == null) {
            synchronized (DatabaseUtil.class) {
                if (databaseUtilInstance == null) {
                    databaseUtilInstance = new DatabaseUtil(context);
                }
            }
        }
        return databaseUtilInstance;
    }

    public SQLiteDatabase getWriteableSQLiteDatabase() {
        mSQLiteDatabase = this.getWritableDatabase();
        return mSQLiteDatabase;
    }


    /**
     * 每次使用完成之后，需要在onDestroy()方法中销毁此对象，当该对象活跃时，请勿关闭
     */
    public void close() {
        mSQLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO:// 添加账号字段，在主页面添加是否输入账号选项，完善本应用！
        /*CREATE TABLE t_pwd2 (
                _index          INTEGER,
                _password    VARCHAR (120)  PRIMARY KEY,
                _remark      VARCHAR (255),
                _date_stored DATETIME default(strftime('%Y.%m.%d %H:%M','now','localtime'))
        );*/
        String sql = "CREATE TABLE "+Constants.TABLE_NAME+
                "(_index INTEGER, _password VARCHAR(120) PRIMARY KEY,_remark VARCHAR(255) DEFAULT '',_date_stored DATETIME DEFAULT (strftime('%Y.%m.%d %H:%M','now','localtime')))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
