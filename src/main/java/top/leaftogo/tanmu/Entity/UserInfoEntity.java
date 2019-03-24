package top.leaftogo.tanmu.Entity;

import lombok.Data;

@Data
public class UserInfoEntity {
    public int id;
    public String openid;
    public String username;
    public String user_pic_url;
    public String description;
    public int like_amount;
    public float money;


    public UserInfoEntity(int id, String openid, String username, String user_pic_url, String description, int like_amount, float money) {
        this.id = id;
        this.openid = openid;
        this.username = username;
        this.user_pic_url = user_pic_url;
        this.description = description;
        this.like_amount = like_amount;
        this.money = money;
    }

    public UserInfoEntity(String openid, String username, String user_pic_url, String description, int like_amount, float money) {
        this.openid = openid;
        this.username = username;
        this.user_pic_url = user_pic_url;
        this.description = description;
        this.like_amount = like_amount;
        this.money = money;
    }

    public UserInfoEntity() {
    }
}
