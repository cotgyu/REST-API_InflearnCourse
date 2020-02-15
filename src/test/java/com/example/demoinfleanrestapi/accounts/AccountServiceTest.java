package com.example.demoinfleanrestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername(){
        //Given
        String password="keesun";
        String username = "kee.com";

        Account account = Account.builder()
                .email("kee.com")
                .password("keesun")
                .roles(Set.of(AccountRoles.ADMIN, AccountRoles.USER))
                .build();

        this.accountService.saveAccount(account);


        // When
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();

    }

    @Test//(expected = UsernameNotFoundException.class) //1. 방법
    public void findByUserNameFail(){
        String username = "rand@email.com";

        /*
        // 2. 방법
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        }catch (UsernameNotFoundException e){
            assertThat(e.getMessage()).containsSequence(username);
        }
        */

        //3. 방법
        // Expected
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);

    }
}