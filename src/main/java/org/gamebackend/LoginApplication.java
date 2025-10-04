package org.gamebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LoginApplication
{
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = SpringApplication.run(LoginApplication.class, args);

    }
}
