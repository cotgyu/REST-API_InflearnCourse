package com.example.demoinfleanrestapi.configs;

import com.example.demoinfleanrestapi.accounts.Account;
import com.example.demoinfleanrestapi.accounts.AccountRepository;
import com.example.demoinfleanrestapi.accounts.AccountRoles;
import com.example.demoinfleanrestapi.accounts.AccountService;
import com.example.demoinfleanrestapi.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {

                Account admin = Account.builder()
                        .email(appProperties.getAdminUsername()) //.email("keesun@email.com") // 하드코딩이 아닌 properties 로 관리
                        .password(appProperties.getAdminPassword()) //.password("keesun")
                        .roles(Set.of(AccountRoles.ADMIN, AccountRoles.USER))
                        .build();

                accountService.saveAccount(admin);

                Account user = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(Set.of(AccountRoles.USER))
                        .build();

                accountService.saveAccount(user);


            }
        };
    }
}
