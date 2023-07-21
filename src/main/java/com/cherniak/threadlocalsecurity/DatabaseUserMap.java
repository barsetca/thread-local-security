package com.cherniak.threadlocalsecurity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

public class DatabaseUserMap {

    @Autowired
    private MySecurityManager securityManager;

  private static Map<String, String> userCredentials = new ConcurrentHashMap<>();
  static {
    userCredentials.put("admin", "pass");
    userCredentials.put("user", "p");
    userCredentials.put("default", "unknown");
  }

  public boolean checkCredentials(){
   //MyUserDetails userDetails =  MySecurityManager.getUserDetails();
   MyUserDetails userDetails =  securityManager.getUserDetails();
   String pass = userCredentials.get(userDetails.getUsername());
    return pass != null && pass.equals(userDetails.getPassword());
  }

}
