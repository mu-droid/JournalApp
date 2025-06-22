package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTests {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @CsvSource({
            "Mudit",
            "Ram"
    })
    public void testFindUserByName(String name){
//         assertNotNull(userRepository.findByUserName("Ram"));
        assertNotNull(userRepository.findByUserName(name), "failed for:" + name);

        //we can also check if a user have empty entries or not
        // assertTrue(!user.getJournalEntries().isEmpty());

    }
}
