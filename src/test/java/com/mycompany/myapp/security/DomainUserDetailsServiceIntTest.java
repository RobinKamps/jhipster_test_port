package com.mycompany.myapp.security;

import com.mycompany.myapp.JhiTestApp;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for DomainUserDetailsService.
 *
 * @see DomainUserDetailsService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhiTestApp.class)
@Transactional
public class DomainUserDetailsServiceIntTest {

    private static final String USER_ONE_EMAIL = "test-user-one@localhost";
    private static final String USER_TWO_EMAIL = "test-user-two@localhost";
    private static final String USER_THREE_EMAIL = "test-user-three@localhost";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService domainUserDetailsService;

    @Before
    public void init() {
        User userOne = new User();
        userOne.setPassword(RandomStringUtils.random(60));
        userOne.setActivated(true);
        userOne.setEmail(USER_ONE_EMAIL);
        userOne.setFirstName("userOne");
        userOne.setLastName("doe");
        userOne.setLangKey("en");
        userRepository.save(userOne);

        User userTwo = new User();
        userTwo.setPassword(RandomStringUtils.random(60));
        userTwo.setActivated(true);
        userTwo.setEmail(USER_TWO_EMAIL);
        userTwo.setFirstName("userTwo");
        userTwo.setLastName("doe");
        userTwo.setLangKey("en");
        userRepository.save(userTwo);

        User userThree = new User();
        userThree.setPassword(RandomStringUtils.random(60));
        userThree.setActivated(false);
        userThree.setEmail(USER_THREE_EMAIL);
        userThree.setFirstName("userThree");
        userThree.setLastName("doe");
        userThree.setLangKey("en");
        userRepository.save(userThree);
    }

    @Test
    @Transactional
    public void assertThatUserCanBeFoundByEmail() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL);
        assertThat(userDetails).isNotNull();
    }

    @Test
    @Transactional
    public void assertThatUserCanBeFoundByEmailIgnoreCase() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL.toUpperCase(Locale.ENGLISH));
        assertThat(userDetails).isNotNull();
    }


    @Test(expected = UserNotActivatedException.class)
    @Transactional
    public void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_THREE_EMAIL);
        assertThat(userDetails).isNotNull();
    }

}
