package com.njuwebclass.csplus.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njuwebclass.csplus.Crawler.Crawler;
import com.njuwebclass.csplus.Crawler.InfoUpdate;
import com.njuwebclass.csplus.utils.Response;
import com.njuwebclass.csplus.utils.ResponseType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InfoUpdateController {

    @RequestMapping(value="/infoupdate")
    public Response getInfoUpdate(){
        Response response = new Response();
        try{
            List<InfoUpdate> infoUpdateList = Crawler.getUpdateInfo("infoupdate");
            JSONArray infoUpdateArray = new JSONArray();
            for(InfoUpdate infoUpdate:infoUpdateList){
                JSONObject infoupdateObject = new JSONObject();
                infoupdateObject.put("classN",infoUpdate.getClassName());
                infoupdateObject.put("homework",infoUpdate.getHomework());
                infoupdateObject.put("information",infoUpdate.getType());
                infoUpdateArray.add(infoupdateObject);
                System.out.println(infoupdateObject.toJSONString());
            }
            System.out.println("Info Update done");
            response.message = "";
            response.status = ResponseType.SUCCESS;
            response.data=infoUpdateArray;
            return response;

        }catch(Exception e){
            response.message = "Update Failed";
            response.status = ResponseType.FAILURE;
            response.data = null;
            return response;
        }

    }
}
