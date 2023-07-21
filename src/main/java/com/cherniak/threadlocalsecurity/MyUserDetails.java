package com.cherniak.threadlocalsecurity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUserDetails {

  private String username;
  private String password;

}
