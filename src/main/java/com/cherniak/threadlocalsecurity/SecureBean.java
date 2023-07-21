package com.cherniak.threadlocalsecurity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@Slf4j
public class SecureBean {

    @Autowired
    private MySecurityManager securityManager;

    public void writeSecureMessage() {

        log.info("Access to protected method writeSecureMessage was opened for userName = '{}'",
                securityManager.getUserDetails().getUsername());
                //MySecurityManager.getUserDetails().getUsername());
    }
}
