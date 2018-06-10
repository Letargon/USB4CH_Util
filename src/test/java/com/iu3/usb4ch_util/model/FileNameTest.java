/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andalon
 */
public class FileNameTest {
    
    public FileNameTest() {
    }
    FileName fn;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
        fn = new FileName("MAGD");
    }

    /**
     * Test of getFileName method, of class FileName.
     */
    @Test
    public void testGetFileName() {
        System.out.println(fn.getFileName());
    }

    /**
     * Test of getTime method, of class FileName.
     */
    @Test
    public void testGetTime() {
        
    }
    
}
