/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.module.components.util;

/**
 *
 * @author stcu
 */
public enum ProjectFileType {
    VISIBILITY("visibility"),
    TRANSACTION("transaction"),
    EXTENDED("extended")
    ;

    private final String text;

    /**
     * @param text
     */
    private ProjectFileType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
    
    public static ProjectFileType fromString(String from) {
        for (ProjectFileType s: values()) {
            if (s.toString().startsWith(from)) {
                return s;
            }
        }

        throw new IllegalArgumentException( from );
    }
}
