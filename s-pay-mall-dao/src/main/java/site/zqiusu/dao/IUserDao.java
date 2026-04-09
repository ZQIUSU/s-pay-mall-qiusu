package site.zqiusu.dao;

import org.apache.ibatis.annotations.Mapper;
import site.zqiusu.domain.po.UserAccount;

@Mapper
public interface IUserDao {

    void insert(UserAccount userAccount);

    UserAccount queryByPhone(String phone);

    UserAccount queryByUserId(Long id);
}
