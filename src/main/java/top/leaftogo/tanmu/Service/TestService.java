package top.leaftogo.tanmu.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.leaftogo.tanmu.Mapper.UserInfoMapper;

@Service
public class TestService {

    @Autowired
    UserInfoMapper userInfoMapper;

    public String test(){
        return "test";
    }
}
