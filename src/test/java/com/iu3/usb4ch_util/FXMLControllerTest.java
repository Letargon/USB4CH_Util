/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Andalon
 */
public class FXMLControllerTest {

    FXMLController fc;

    @Before
    public void setUp() {
        fc = new FXMLController();
    }

    /**
     * Test of initialize method, of class FXMLController.
     */
    //@Test
    public void testInitialize() {
        fc.testTriggered();
    }
}
