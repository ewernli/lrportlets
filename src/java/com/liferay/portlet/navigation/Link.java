/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.portlet.navigation;

import com.liferay.portal.model.Layout;
import java.util.Locale;

/**
 *
 * @author ewernli
 */
public class Link {

    public static String HOST = "http://wernli.nine.ch";
    public static String COMMUNITY = "/web/guest";

    public String name;
    public String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Link(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Link( Layout layout )
    {
        this.name = layout.getHTMLTitle( Locale.US );
        this.url =  HOST + COMMUNITY + layout.getFriendlyURL();
    }
}
