package com.tzq;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Https配置类
 * @author 马庆龙
 *
 */
@Component
public class HttpsAutoRedirect {
//	@Bean
//	 public TomcatServletWebServerFactory servletContainer() {
//		TomcatServletWebServerFactory  tomcat = new TomcatServletWebServerFactory() {
//	     @Override
//	     protected void postProcessContext(Context context) {
//	       SecurityConstraint securityConstraint = new SecurityConstraint();
//	       securityConstraint.setUserConstraint("CONFIDENTIAL");
//	       SecurityCollection collection = new SecurityCollection();
//	       collection.addPattern("/*");
//	       securityConstraint.addCollection(collection);
//	       context.addConstraint(securityConstraint);
//	     }
//	   };
//	   tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
//	   return tomcat;
//	 }
//	 private Connector initiateHttpConnector() {
//	   Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//	   connector.setScheme("http");
//	   connector.setPort(80);
//	   connector.setSecure(false);
//	   connector.setRedirectPort(443);
//	   return connector;
//	 }
}
