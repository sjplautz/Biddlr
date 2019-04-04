package com.example.biddlr;

import org.junit.Test;

import ui.CreateUserActivity;

import static org.junit.Assert.assertEquals;

public class CreateUserTest extends CreateUserActivity {

    String Email = null;
    String fNmae = null;
    String lName = null;
    String Password = null;
    String rePassword = null;

    //Tests the email field existence
    @Test
    public void EmailFieldTest() {
        assertEquals(txtEnEmail,Email);
    }

    //Tests the Fname field existence
    @Test
    public void FnameFieldTest() {
        assertEquals(txtFname,fNmae);
    }
    //Tests the Lname field existence
    @Test
    public void LnameFieldTest() {
        assertEquals(txtLname,lName);
    }

    //Tests the Password field existence
    @Test
    public void PasswordFieldTest() {
        assertEquals(txtEnPass,Password);
    }

    //Tests the RePassword field existence
    @Test
    public void RePasswordFieldTest() {
        assertEquals(txtReEnPass,rePassword);
    }
}
