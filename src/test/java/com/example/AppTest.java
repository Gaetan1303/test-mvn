package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class)  // Specify your main application class here
public class AppTest {

    @Test
    public void testApp() {
        assertTrue(true, "This should always pass");
    
    }
}