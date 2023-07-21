package com.cherniak.threadlocalsecurity;

import java.lang.reflect.Method;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@Slf4j
public class MySecurityAdvice implements MethodBeforeAdvice {

  @Autowired
  private MySecurityManager securityManager;

  @Autowired
  private DatabaseUserMap databaseUserMap;

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        MyUserDetails userDetails = securityManager.getUserDetails();
       // MyUserDetails userDetails = MySecurityManager.getUserDetails();
        if (userDetails == null) {
            throw new SecurityException("No auth user. Access denied. You must be logged for using this method " + method.getName());
        }
        if (!databaseUserMap.checkCredentials()) {
       // } else if (!DatabaseUserMap.checkCredentials()) {
            log.warn("Not found credentials to method '{}' access denied. Try again '{}'",
                    method.getName(),
                    userDetails.getUsername()
            );
            throw new SecurityException("You must be used valid credentials " + method.getName());
        } else if ("admin".equals(userDetails.getUsername())) {
            log.info("Logged user as admin from thread was successfully");
        } else {
            log.info("Simple user with name {}. Logged successfully", userDetails.getUsername());
        }
    }
}
