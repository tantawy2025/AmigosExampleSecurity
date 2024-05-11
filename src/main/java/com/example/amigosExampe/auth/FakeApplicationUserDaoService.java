package com.example.amigosExampe.auth;

import com.example.amigosExampe.Security.ApplicationUserRole;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.amigosExampe.Security.ApplicationUserRole.*;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String userName) {
        return getApplicationUser()
                .stream()
                .filter(applicationUser -> userName.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUser(){
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser("anna"
                        ,passwordEncoder.encode("password")
                        ,STUDENT.getGrantedAuthorites()
                        ,true
                        ,true
                        ,true
                        ,true
                ),

                new ApplicationUser("linda"
                        ,passwordEncoder.encode("password")
                        ,ADMIN.getGrantedAuthorites()
                        ,true
                        ,true
                        ,true
                        ,true
                ),

                new ApplicationUser("tom"
                        ,passwordEncoder.encode("password")
                        ,ADMINTRAINEE.getGrantedAuthorites()
                        ,true
                        ,true
                        ,true
                        ,true
                )
        );

        return applicationUsers;
    }
}
