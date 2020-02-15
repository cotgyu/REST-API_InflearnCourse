package com.example.demoinfleanrestapi.configs;

import com.example.demoinfleanrestapi.accounts.Account;
import com.example.demoinfleanrestapi.accounts.AccountRoles;
import com.example.demoinfleanrestapi.accounts.AccountService;
import com.example.demoinfleanrestapi.common.AppProperties;
import com.example.demoinfleanrestapi.common.BaseControllerTest;
import com.example.demoinfleanrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{
        //Given
        //String username = "keesun@email.com2";
        //String password = "keesun2";
// 애플리케이션 기동 시 해당 정보 자동 입력됨
//        Account keesun = Account.builder()
//                .email(appProperties.getUserUsername())
//                .password(appProperties.getUserPassword())
//                .roles(Set.of(AccountRoles.ADMIN, AccountRoles.USER))
//                .build();
//
//        this.accountService.saveAccount(keesun);


        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                    .param("username", appProperties.getUserUsername())
                    .param("password", appProperties.getUserPassword())
                    .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

    }

}