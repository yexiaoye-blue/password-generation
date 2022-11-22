package eu.org.yexiaoye.passwordgeneration.dao;

import java.util.List;

import eu.org.yexiaoye.passwordgeneration.bean.PasswordInfo;
import eu.org.yexiaoye.passwordgeneration.exception.NoMatchingDataException;

public interface PasswordDao {

    long insert(PasswordInfo passwordInfo);

    int deleteByPassword(String password);

    /*int updateByIndex(int index);

    int updateByPassword(String password);*/
    int update(PasswordInfo passwordInfo);

    PasswordInfo selectByPassword(String password);

    PasswordInfo selectByPasswordIndex(int index);

    List<PasswordInfo> selectAll();

    List<PasswordInfo> selectAllIndexAndPassword();

    // 查询所有索引
    // List<Integer> selectAllIndex();

    void close();
}
