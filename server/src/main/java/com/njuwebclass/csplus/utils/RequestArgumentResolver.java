package com.njuwebclass.csplus.utils;

import com.alibaba.fastjson.JSONObject;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Date;

public class RequestArgumentResolver implements HandlerMethodArgumentResolver
{
	
    @Override
    public boolean supportsParameter(MethodParameter methodParameter)
    {
    	System.out.println("请求是否为Request类型："+methodParameter.getParameterType().equals(Request.class));
    	//如果controller的形参为Request对象，则返回true，表示需要处理该参数分解，调用下面的resolveArgument处理
        return methodParameter.getParameterType().equals(Request.class);
    }
 
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
    {
    	Request request = new Request();

    	//通过请求中的参数username设置request的user
    	String username = webRequest.getParameter("username");
    	
    	String[] descriptions = webRequest.getDescription(true).split(";");
    	String uri = descriptions[0].split("=")[1];
    	String client = descriptions[1].split("=")[1];
    	request.setUri(uri);
    	request.setClient(client);

    	
        String paramsString = webRequest.getParameter("params");
        JSONObject params = JSONObject.parseObject(paramsString);
        
        request.setParams(params);

		System.out.println("Request content");
		System.out.println("URL: " + uri);
		System.out.println("params: " + params);

        return request;
    }
}