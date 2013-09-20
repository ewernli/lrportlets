/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.portlet.navigation;

import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.SyndFeedOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ewernli
 */
public class RssServlet extends HttpServlet {

    private static Map<Long,Date> dates;

    private synchronized Date getDate( long pid ) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        String name = System.getProperty( "rss-file", "/tmp/rss-file" );
        File f = new File( name );

        if( dates == null )
        {
            if( f.exists() )
            {
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                try
                {
                    fis = new FileInputStream( f );
                    ois = new ObjectInputStream( fis );
                    dates = (Map<Long,Date>) ois.readObject();
                }
                finally
                {
                    if( ois != null ) { ois.close(); }
                    if( fis != null ) { fis.close(); }
                }
            }
            else
            {
                dates = new HashMap<Long,Date>();
            }
        }

        if( dates.containsKey( pid ))
        {
            return dates.get( pid );
        }
        else
        {
            Date now = new Date();

            dates.put( pid , now );

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try
            {
                fos = new FileOutputStream( f );
                oos = new ObjectOutputStream( fos );
                oos.writeObject( dates );
            }
            finally
            {
                if( oos != null ) { oos.close(); }
                if( fos != null ) { fos.close(); }
            }

            return now;
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Layout layout;

            String pid = request.getParameter("pid");

            layout = LayoutLocalServiceUtil.getLayout(Long.valueOf(pid));

            String feedType = "rss_2.0";

            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(feedType);

            feed.setTitle("Erwann's blog");
            feed.setLink( Link.HOST + request.getContextPath() + "/RssServlet?pid=" + pid );
            feed.setDescription("A blog on software engineering, computer science and java");

            List entries = new ArrayList();

            for (Layout l : layout.getAllChildren()) {
                if (l.isHidden() == false) {


                    Link link = new Link(l);
                    SyndEntry entry;

                    Date date = getDate( l.getPlid() ) ;

                    entry = new SyndEntryImpl();
                    entry.setTitle(link.getName());
                    entry.setLink(link.getUrl());
                    entry.setPublishedDate( date );

                    /*
                    SyndContent description;
                    description = new SyndContentImpl();
                    description.setType("text/plain");
                    description.setValue(l.getHTMLTitle(Locale.US));
                    entry.setDescription(description);
                     */

                    entries.add(entry);

                }
            }
            feed.setEntries(entries);

            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, out);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
