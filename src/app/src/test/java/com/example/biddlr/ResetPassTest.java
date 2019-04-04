package com.example.biddlr;



import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ui.ForgotPassActivity.inputEmail;

public class ResetPassTest {

    String noEmail = null;

    //Tests the email field existence

    @Test
    public void EmailFieldTest() {

        assertEquals(inputEmail,noEmail);
    }


}
