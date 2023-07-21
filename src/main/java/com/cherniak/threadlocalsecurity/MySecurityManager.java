package com.cherniak.threadlocalsecurity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySecurityManager{

  private static final ThreadLocal<MyUserDetails> userDetails = new ThreadLocal<>();

  public void login(String userName, String password){
    log.info(" logging username '{}'", userName);
    userDetails.set(new MyUserDetails(userName, password));
  }

  public MyUserDetails getUserDetails(){
    return userDetails.get();
  }

  public void logout(){
    userDetails.remove();
  }

}
