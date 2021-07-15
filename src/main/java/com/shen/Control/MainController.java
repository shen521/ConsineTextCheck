package com.shen.Control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/fileLoad")
    public String fileLoad(){

        return "layui_upload";
    }
}
