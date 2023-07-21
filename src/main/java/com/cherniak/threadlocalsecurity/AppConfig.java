package com.cherniak.threadlocalsecurity;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public MySecurityManager mySecurityManager() {
    return new MySecurityManager();
  }

  @Bean
  public DatabaseUserMap databaseUserMap() {
    return new DatabaseUserMap();
  }

  @Bean
  public MySecurityAdvice mySecurityAdvice() {
    return new MySecurityAdvice();
  }

  @Bean
  public SecureBean secureBean() {
    SecureBean secureBean = new SecureBean();
    secureBean.setSecurityManager(mySecurityManager());
    MySecurityAdvice securityAdvice = mySecurityAdvice();
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setTarget(secureBean);
    proxyFactory.addAdvice(securityAdvice);
    return (SecureBean) proxyFactory.getProxy();
  }
}
