package top.leaftogo.tanmu.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {


    //页面跳转至user.html
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    //value参数表示的是这个接口的url，method参数表示的是接口访问的方法，这里是get
    public String user(){
        return "user"; //返回的字符串和页面名一致
    }
}
