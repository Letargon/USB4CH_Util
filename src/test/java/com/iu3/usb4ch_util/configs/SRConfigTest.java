package com.iu3.usb4ch_util.configs;

import org.junit.Test;

/**
 *
 * @author Andalon
 */
public class SRConfigTest {
    
    /**
     * Test of loadDefaults method, of class SRConfig.
     */
    @Test
    public void testLoadDefaults() {
        SRConfig conf = new SRConfig("test.ini");
        conf.loadDefaults();
    }
  
}
