/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.pipeline;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andalon
 */
public class PipelineLauncherTest {
    PipelineLauncher ppl;
    public PipelineLauncherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @Before
    public void setUp() {
    ppl     = new PipelineLauncher("test.ini");
    }

    /**
     * Test of setTimeScope method, of class PipelineLauncher.
     */
    @Test
    public void testSetTimeScope() {
    }

    /**
     * Test of terminate method, of class PipelineLauncher.
     */
    @Test
    public void testTerminate() {
    }

    /**
     * Test of runPipeLine method, of class PipelineLauncher.
     */
    //@Test
    public void testRunPipeLine() {
        ppl.setTestConfig();
        ppl.runPipeLine(true);
    }

    /**
     * Test of runMonitor method, of class PipelineLauncher.
     */
    @Test
    public void testRunMonitor() {
    }
    
}
