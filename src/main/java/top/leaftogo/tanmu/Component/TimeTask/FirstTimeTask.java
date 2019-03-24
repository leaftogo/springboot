package top.leaftogo.tanmu.Component.TimeTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class FirstTimeTask {

    //每天4点（24小时）执行
    @Scheduled(cron = "0 0 4 * * ? ")
    public void authRefresh() {
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
