package com.dior.dior.exception;

import com.dior.dior.bean.SysLogError;
import com.dior.dior.mapper.SysLogErrorMapper;
import com.dior.dior.response.ResponseResultVo;
import com.dior.dior.response.ResultVoStatus;
import com.dior.dior.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Component
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    SysLogErrorMapper sysLogErrorMapper;


    @ExceptionHandler({Exception.class})
    public ResponseResultVo handleServiceException(Exception e,HttpServletRequest request) {
        savelog(request,e);
        return ResponseResultVo.faild(ResultVoStatus.ErrorException)
                .addExtra("stackTrace", e.getStackTrace())
                .addExtra("exceptionMessage", e.getClass().getName() + ": " + e.getMessage());

    }


    private void savelog(HttpServletRequest request,
                         Exception e) {
        SysLogError sysLogError = new SysLogError();
        //获取IP
        String IP = IpUtil.getIpAddrByRequest(request);
        //获取请求路径
        String requestURI = request.getRequestURI();
        //异常产生的方法名
        String methodName = e.getStackTrace()[0].getMethodName();
        //获取请求参数
        Map<String, String[]> maps = request.getParameterMap();
        String cs = null;
        for (Map.Entry<String, String[]> entry : maps.entrySet()) {
            cs = entry.getKey() + ":" + Arrays.toString(entry.getValue()) + ";";
        }
        //获取请求类型
        String type = request.getMethod();
        //头信息
        String header = request.getHeader(HttpHeaders.USER_AGENT);
        sysLogError.setCreateDate(new Date());
        sysLogError.setIp(IP);
        sysLogError.setRequestMethod(type);
        sysLogError.setRequestParams(cs);
        sysLogError.setRequestUri(requestURI);
        sysLogError.setUserAgent(header);
        sysLogError.setErrorInfo(e.getMessage());
//        log.error(sysLogError.toString());
        StringBuilder logStr = new StringBuilder();
        logStr.append(sysLogError.getIp()).append("---").append(sysLogError.getRequestUri()).append("---").append(sysLogError.getErrorInfo());
        log.error(logStr.toString());

        sysLogErrorMapper.insertSelective(sysLogError);



    }


}
