package com.dior.dior.bean;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "sys_log_error")
public class SysLogError  implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 请求URI
     */
    @Column(name = "request_uri")
    private String requestUri;

    /**
     * 请求方式
     */
    @Column(name = "request_method")
    private String requestMethod;

    /**
     * 请求参数
     */
    @Column(name = "request_params")
    private String requestParams;

    /**
     * 用户代理
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 操作IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 异常信息
     */
    @Column(name = "error_info")
    private String errorInfo;

    /**
     * 创建者
     */
    @Column(name = "creator")
    private Long creator;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;
}