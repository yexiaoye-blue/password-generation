package eu.org.yexiaoye.passwordgeneration.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import eu.org.yexiaoye.passwordgeneration.bean.PasswordInfo;
import eu.org.yexiaoye.passwordgeneration.dao.PasswordDao;
import eu.org.yexiaoye.passwordgeneration.util.Constants;
import eu.org.yexiaoye.passwordgeneration.util.DatabaseUtil;

public class PasswordDaoImpl implements PasswordDao {

    private static final String TAG = "PasswordDao";
    private final DatabaseUtil mHelper;
    private final SQLiteDatabase mWritableDB;

    public PasswordDaoImpl(DatabaseUtil databaseUtil) {
        mHelper = databaseUtil;
        mWritableDB = mHelper.getWriteableSQLiteDatabase();
    }

    /**
     * return : the row ID of the newly inserted row
     * @param passwordInfo PasswordInfo
     * @return  新插入的行id
     */
    @Override
    public long insert(PasswordInfo passwordInfo) {
        ContentValues values = new ContentValues();

        values.put("_index",passwordInfo.getIndex());
        values.put("_password",passwordInfo.getPwdContent());
        if (passwordInfo.getRemark() != null) {
            values.put("_remark",passwordInfo.getRemark());
        }
        return mWritableDB.insert(Constants.TABLE_NAME, null, values);
    }

    /**
     * 根据password删除某行
     * @param password    password
     * @return  the row ID of the newly inserted row, or -1 if an error occurred
     */
    @Override
    public int deleteByPassword(String password) {
        return mWritableDB.delete(Constants.TABLE_NAME, "_password=?", new String[]{password});
    }

    /*@Override
    public int updateByIndex(int index) {
        return 0;
    }

    @Override
    public int updateByPassword(String password) {
        return 0;
    }
*/
    /**
     * 修改记录，存储日期不支持修改
     * @param passwordInfo  PasswordInfo,index不能为空
     * @return              the number of rows affected(受影响的行数)
     */
    @Override
    public int update(PasswordInfo passwordInfo) {
        // 开启事务
        mWritableDB.beginTransaction();
        int count = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("_index",passwordInfo.getIndex());
            values.put("_password",passwordInfo.getPwdContent());
            if (passwordInfo.getRemark() != null) {
                values.put("_remark",passwordInfo.getRemark());
            }
            count = mWritableDB.update(Constants.TABLE_NAME, values, "_index=?", new String[]{String.valueOf(passwordInfo.getIndex())});
            mWritableDB.setTransactionSuccessful();
        } finally {
            mWritableDB.endTransaction();
        }

        return count;
    }

    @Override
    public PasswordInfo selectByPassword(String password){
        String sql = "select _index,_remark,_date_stored from "+Constants.TABLE_NAME+ " where _password=?";
        Cursor cursor = mWritableDB.rawQuery(sql, new String[]{password});
        PasswordInfo passwordInfo = null;
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            int columnIndex = cursor.getColumnIndex("_index");
            int remarkIndex = cursor.getColumnIndex("_remark");
            int dateStoredIndex = cursor.getColumnIndex("_date_stored");

            passwordInfo = new PasswordInfo(
                    cursor.getInt(columnIndex),
                    password,
                    cursor.getString(remarkIndex),
                    cursor.getString(dateStoredIndex));
        }
        cursor.close();
        return passwordInfo;
    }


    @Override
    public PasswordInfo selectByPasswordIndex(int index) {
        String sql = "select _password,_remark,_date_stored from "+Constants.TABLE_NAME+ " where _index=?";
        Cursor cursor = mWritableDB.rawQuery(sql, new String[]{String.valueOf(index)});
        PasswordInfo passwordInfo = null;
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            int passwordIndex = cursor.getColumnIndex("_password");
            int remarkIndex = cursor.getColumnIndex("_remark");
            int dateStoredIndex = cursor.getColumnIndex("_date_stored");

            passwordInfo = new PasswordInfo(index, cursor.getString(passwordIndex), cursor.getString(remarkIndex),cursor.getString(dateStoredIndex));
        }
        cursor.close();
        return passwordInfo;
    }

    /**
     * 查询所有密码信息，
     * @return  如果没有数据则返回0,否则返回List<PasswordInfo>
     */
    @Override
    public List<PasswordInfo> selectAll() {
        String sql = "select _index,_password,_remark,_date_stored from " + Constants.TABLE_NAME;
        Cursor cursor = mWritableDB.rawQuery(sql,null);
        List<PasswordInfo> passwordInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("_index");
            int pwdIndex = cursor.getColumnIndex("_password");
            int remarkIndex = cursor.getColumnIndex("_remark");
            int dateStoredIndex = cursor.getColumnIndex("_date_stored");

            int indexValue = cursor.getInt(columnIndex);
            String pwdValue = cursor.getString(pwdIndex);
            String remarkValue = cursor.getString(remarkIndex);
            String dateStored = cursor.getString(dateStoredIndex);


            PasswordInfo info = new PasswordInfo(indexValue,pwdValue,remarkValue,dateStored);
            passwordInfoList.add(info);
            // Log.d(TAG,idValue + "," + pwdValue + "," + remarkValue);
        }
        cursor.close();
        return passwordInfoList;
    }

    /**
     *
     * @return 若没有数据则list长度返回0
     */
    @Override
    public List<PasswordInfo> selectAllIndexAndPassword() {
        String sql = "select _index,_password from " + Constants.TABLE_NAME;
        Cursor cursor = mWritableDB.rawQuery(sql,null);
        List<PasswordInfo> passwordInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("_index");
            int pwdIndex = cursor.getColumnIndex("_password");

            int indexValue = cursor.getInt(columnIndex);
            String pwdValue = cursor.getString(pwdIndex);

            PasswordInfo info = new PasswordInfo();
            info.setIndex(indexValue);
            info.setPwdContent(pwdValue);
            passwordInfoList.add(info);
            // Log.d(TAG,idValue + "," + pwdValue + "," + remarkValue);
        }
        cursor.close();
        return passwordInfoList;
    }

    /**
     * 关闭
     */
    @Override
    public void close() {
        mHelper.close();
    }
}
