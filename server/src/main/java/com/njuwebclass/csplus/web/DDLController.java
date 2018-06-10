package com.njuwebclass.csplus.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.njuwebclass.csplus.Crawler.Crawler;
import com.njuwebclass.csplus.Crawler.DDLInfo;
import com.njuwebclass.csplus.utils.Response;
import com.njuwebclass.csplus.utils.ResponseType;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class DDLController {


    @RequestMapping(value="/ddl")
    public Response getAllDDL(){
        System.out.println("start get DDL");
        Response response = new Response();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse("2018-03-01");
            List<DDLInfo> s = Crawler.getDDLInfo(date);

            JSONArray ddllist = new JSONArray();
            for (DDLInfo di : s) {
                JSONObject ddlinfoJson = new JSONObject();
                ddlinfoJson.put("classN", di.getClassName());
                ddlinfoJson.put("homework", di.getClassName());
                ddlinfoJson.put("information", di.getDdl());
                ddllist.add(ddlinfoJson);
                System.out.println(ddlinfoJson.toJSONString());
            }



            response.message = "";
            response.status = ResponseType.SUCCESS;
            response.data = ddllist;
            return response;
        }catch(Exception e){
            e.printStackTrace();

            response.message = "GetDDLInfo Failed";
            response.status = ResponseType.FAILURE;
        }
        return response;

    }
}
