package top.leaftogo.tanmu.Component.TimeTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuestionTimeTask {

//服务启动时运行，设置10天的间隔日期
    @Scheduled(fixedDelay = 1000*60*60*24*10)
    public void questionMateListRefresh() {

    }

    @Scheduled(fixedDelay = 1000*60*60*24)
    public void setYml(){

    }

}
