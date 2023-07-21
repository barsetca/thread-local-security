package com.cherniak.threadlocalsecurity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class ThreadLocalSecurityTest {

  private static final Map<String, String> forLogin = new ConcurrentHashMap<>();

  static {
    forLogin.put("admin1", "pass1");
    forLogin.put("user", "p");
    forLogin.put("default", "unknown");
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    MySecurityManager mySecurityManager = context.getBean("mySecurityManager", MySecurityManager.class);
    SecureBean secureBean = context.getBean("secureBean", SecureBean.class);
    log.info(" /////////////////////////////// AUTHENTICATION /////////////////////////////////////");
    List<CompletableFuture<Void>> futures = getListFuture(secureBean, mySecurityManager);
    mySecurityManager.login("admin", "pass");
   // MySecurityManager.login("admin", "pass");
    secureBean.writeSecureMessage();
    mySecurityManager.logout();
    //MySecurityManager.logout();
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
  }

  public static List<CompletableFuture<Void>> getListFuture(SecureBean secureBean, MySecurityManager mySecurityManager) {
    List<CompletableFuture<Void>> futures = new ArrayList<>();
    for (Map.Entry<String, String> pair : forLogin.entrySet()) {
      CompletableFuture<Void> future = CompletableFuture.runAsync(
          () -> {
            if (!"default".equals(pair.getKey())) {
              //MySecurityManager.login(pair.getKey(), pair.getValue());
              mySecurityManager.login(pair.getKey(), pair.getValue());
            }
            log.info("CompletableFuture user '{}' before call writeSecureMessage", pair.getKey());
            secureBean.writeSecureMessage();
            log.info("CompletableFuture user '{}' AFTER call writeSecureMessage", pair.getKey());
           // MySecurityManager.logout();

            mySecurityManager.logout();
          }
      ).handle((result, ex) -> {
          log.info("//////////// {}", pair.getKey() );
          if (ex != null) {
              log.error("Исключительная ситуация для юзера getMessage '{}': {}", pair.getKey(), ex.getMessage());
          } else {
              log.info("Lля юзера '{}' все закончилось благополучно", pair.getKey());
          }
          return result;
      });
      futures.add(future);
    }
    return futures;
  }
}
