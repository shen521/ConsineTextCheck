package com.shen.Control;

import com.shen.Service.fileLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping(value = "/data")
public class uploadController {
    @Autowired
    fileLoadService fileLoadService;
    String a[] =new String[2];
    int c=0;



    @RequestMapping(value="/uploadSource" , method = RequestMethod.POST)
    @ResponseBody
    public String uploadSource(@RequestParam("file") MultipartFile file) throws Exception {
        String pathString = null;
        if(file!=null) {
            //获取上传的文件名称
            String filename = file.getOriginalFilename();
            //文件上传时，chrome与IE/Edge对于originalFilename处理方式不同
            //chrome会获取到该文件的直接文件名称，IE/Edge会获取到文件上传时完整路径/文件名
            //Check for Unix-style path
            int unixSep = filename.lastIndexOf('/');
            //Check for Windows-style path
            int winSep = filename.lastIndexOf('\\');
            //cut off at latest possible point
            int pos = (winSep > unixSep ? winSep:unixSep);
            if (pos != -1)
                filename = filename.substring(pos + 1);
            pathString = "E:/upload/"  +filename;//上传到本地
        }

        try {
            File files=new File(pathString);//在内存中创建File文件映射对象
            //打印查看上传路径
//            System.out.println(pathString);
            if(!files.getParentFile().exists()){//判断映射文件的父文件是否真实存在
                files.getParentFile().mkdirs();//创建所有父文件夹
            }
            file.transferTo(files);//采用file.transferTo 来保存上传的文件
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        a[1]=pathString;
        if (a[0]==null) {
            a[0]=pathString;
        }

        if(c>0&&a[0]!=a[1]) {

                fileLoadService.Consine(a[0],a[1]);

        }
        c++;
        return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + pathString + "\"}";
    }
}
