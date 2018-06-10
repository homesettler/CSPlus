package com.njuwebclass.csplus.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.njuwebclass.csplus.Crawler.Crawler;
import com.njuwebclass.csplus.utils.Response;
import com.njuwebclass.csplus.utils.ResponseType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.njuwebclass.csplus.utils.Request;

@RestController
public class AuthController {
    @RequestMapping(value="/loginCS")
    public Response login(Request request){


        Response response = new Response();
        response.data = request.getParams();
        String username = request.getParams().getString("username");
        String password = request.getParams().getString("password");
        try{
            if(Crawler.login(username,password))
                response.status = ResponseType.SUCCESS;
            else {
                response.status = ResponseType.FAILURE;
                response.message = "No Such User";
            }
            return response;
        }catch(Exception e){
            e.printStackTrace();
        }
        response.status = ResponseType.FAILURE;
        response.message = "crawlerFailed";
        return response;
    }
}
