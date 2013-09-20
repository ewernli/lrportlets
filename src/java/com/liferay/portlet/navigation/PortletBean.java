package com.liferay.portlet.navigation;

import com.liferay.portal.SystemException;
import com.liferay.portal.model.Layout;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;

public class PortletBean {

    String namespace;

    public String getBaseUrl()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getRequestContextPath();
    }


    public String getRssUrl() {
          return "RssServlet?pid=" + getCurrentLayout().getPlid();
    }

    public Layout getCurrentLayout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PortletContext pctx = (PortletContext) facesContext.getExternalContext().getContext();
        PortletRequest request = (PortletRequest) facesContext.getExternalContext().getRequest();
        Layout layout = (Layout) request.getAttribute("LAYOUT");

        /* Theme themeDisplay = (Theme) request.getAttribute("THEME"); */

        return layout;
    }

    public String getPortletId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (namespace == null) {
            namespace = facesContext.getExternalContext().encodeNamespace("");
        }
        return namespace;
    }

    public String getLayoutId() {

        return String.valueOf(getCurrentLayout().getPlid());
    }

    public List<Link> getLayouts() throws SystemException {
        List<Link> layouts = new ArrayList<Link>();

        Layout layout = getCurrentLayout();

        for (Layout l : layout.getAllChildren()) {
            if (l.isHidden() == false) {
                layouts.add(new Link(l));
            }
        }

        return layouts;
    }

    public PortletBean() {
        
    }

    
}
