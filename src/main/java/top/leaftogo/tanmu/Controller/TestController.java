package top.leaftogo.tanmu.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.leaftogo.tanmu.Service.TestService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping(value = "/test")
    public String test(@RequestParam("name") String name){
        return name;
    }



    @RequestMapping(value = "/test1")
    @ResponseBody
    public String test1() {
        return "1111111";
    }

    @RequestMapping(value = "/servlet")
    @ResponseBody
    public String test2(HttpSession session, HttpServletResponse response) throws IOException {
        response.getWriter().write("");
        session.getAttribute("");
        return "sdfsfsfsfsd";
    }

    class Student{
        public int id;
        public String username;
        public String studyCode;
        public Map<String,String> map;
    }

    @RequestMapping(value = "/list")
    public Student test11(){
        List<String> list = new ArrayList<>();
        list.add("1111");
        list.add("222");
        list.add("333");
        Student student = new Student();
        student.id=11;
        student.username="仓仓子";
        student.studyCode="201721xxxx";
        return student;
    }

    public Object testr(){
        return null;
    }

    @RequestMapping(value = "/pic")
    public String pic(MultipartFile file){
        //获得文件参数
         return "";
    }
}
