/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.configs.parser;

import com.iu3.usb4ch_util.configs.model.SRIniGroup;
import java.util.HashSet;
import org.junit.Test;

/**
 *
 * @author Andalon
 */
public class IniFileTest {
    
    public IniFileTest() {
    }

    /**
     * Test of read method, of class IniFile.
     */
    @Test
    public void testRead() {
        
    }

    /**
     * Test of write method, of class IniFile.
     */
//    @Test
    public void testWriteSRIni() {
        IniFile file = new IniFile("test.ini");
        SRIniGroup groupData = new SRIniGroup();
        groupData.groupName = "Test";
        groupData.properties.put("property1","value1");
        file.write(groupData);
        
        HashSet<SRIniGroup> map = file.read();
        assert(map.contains(groupData));
    }
    
}
