/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iu3.usb4ch_util.configs.model;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Andalon
 */
public class SRIniGroup {

    public String groupName;//group
    public HashMap<String, String> properties;//key = value

    public SRIniGroup() {
        groupName = new String();
        properties = new HashMap<>();
    }

    public SRIniGroup(String groupName, HashMap<String, String> properties) {
        this.groupName = groupName;
        this.properties = properties;
    }
 

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.groupName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SRIniGroup other = (SRIniGroup) obj;
        if (!Objects.equals(this.groupName, other.groupName)) {
            return false;
        }
        return true;
    }
    
}
