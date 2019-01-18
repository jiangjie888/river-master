package com.river.leader.config.web;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * 统一请求转化器默认配置
 */
public class MvcAdapter {

    public static RequestMappingHandlerAdapter requestMappingHandlerAdapter(
            RequestMappingHandlerAdapter original,
            FastJsonHttpMessageConverter fastJsonHttpMessageConverter) {
            //RequestDataMessageConvert requestDataMessageConvert) {

        /*List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(requestDataMessageConvert);

        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
        argumentResolvers.add(new RequestDataTypeMethodProcessor(converters));
        original.setCustomArgumentResolvers(argumentResolvers);*/

        List<HttpMessageConverter<?>> list = new LinkedList<>();
        //
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);  // see SPR-7316
        list.add(stringHttpMessageConverter);
        //使用阿里 FastJson 作为JSON MessageConverter
        /*FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//保留空的字段
        SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
        SerializerFeature.WriteNullNumberAsZero);//Number null -> 0
        fastJsonHttpMessageConverter.setFastJsonConfig(config);
        fastJsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));*/
        list.add(fastJsonHttpMessageConverter);

        original.setMessageConverters(list);
        return original;
    }
}