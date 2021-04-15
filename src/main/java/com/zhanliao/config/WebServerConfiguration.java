package com.zhanliao.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhanLiao
 * @Description: 当Spring容器内没有TomcatEmbeddedServletContainerFactory这个bean时，就会将此bean加载进去
 * @Date: 2021/4/15 16:00
 * @Version: 1.0
 */
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory configurableWebServerFactory) {
        // 使用对应工厂类提供给我们的接口定制化我们的Tomcat connector
        ((TomcatServletWebServerFactory)configurableWebServerFactory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                // 定制化keepalivetimeout，设置30秒内没有请求则服务端自动断开keepalive连接
                protocol.setKeepAliveTimeout(30000);
                // 当客户端发送超过10000个请求则自动断开keepalive链接
                protocol.setMaxKeepAliveRequests(10000);
            }
        });
    }
}
