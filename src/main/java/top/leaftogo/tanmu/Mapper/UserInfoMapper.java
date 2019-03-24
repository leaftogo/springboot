package top.leaftogo.tanmu.Mapper;

import org.apache.ibatis.annotations.*;
import top.leaftogo.tanmu.Entity.UserInfoEntity;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    @Insert("insert into user_info (openid,username,user_pic_url,description,like_amount,money) values (#{openid},#{username},#{user_pic_url},#{description},#{like_amount},#{money})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void add(UserInfoEntity entity);

    @Select("select * from user_info where openid = #{openid}")
    List<UserInfoEntity> findEntityByOpenid(String openid);

}
