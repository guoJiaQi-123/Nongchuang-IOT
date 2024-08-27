package com.tyut.shopping_common.component;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class TakeTimeCountListener implements ApplicationListener<ServletRequestHandledEvent> {

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        Throwable failureCause = event.getFailureCause() ;
        if (failureCause != null) {
            System.err.printf("错误原因: %s%n", failureCause.getMessage()) ;
        }
        System.err.printf("请求客户端地址：%s, 请求URL: %s, 请求Method: %s, 请求耗时: %d%n",
                event.getClientAddress(),
                event.getRequestUrl(),
                event.getMethod(),
                event.getProcessingTimeMillis()) ;
    }
}