 package org.activiti.explorer.filter;
 
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.RequestDispatcher;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 
 public class ExplorerFilter
   implements Filter
 {
   private List<String> ignoreList = new ArrayList();
   
   public void init(FilterConfig filterConfig) throws ServletException
   {
     this.ignoreList.add("/ui");
     this.ignoreList.add("/VAADIN");
     this.ignoreList.add("/modeler.html");
     this.ignoreList.add("/editor-app");
     this.ignoreList.add("/service");
     this.ignoreList.add("/diagram-viewer");
   }
   
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
     HttpServletRequest req = (HttpServletRequest)request;
     String path = req.getRequestURI().substring(req.getContextPath().length());
     int indexSlash = path.indexOf("/", 1);
     String firstPart = null;
     if (indexSlash > 0) {
       firstPart = path.substring(0, indexSlash);
     } else {
       firstPart = path;
     }
     
     if (this.ignoreList.contains(firstPart))
     {
 
       if (("/service".equals(firstPart)) && (req.getRemoteUser() == null) && (
         (req.getSession(false) == null) || (req.getSession().getAttribute("_currentUser") == null))) {
         ((HttpServletResponse)response).sendError(403);
         return;
       }
       
       chain.doFilter(request, response);
     } else {
       request.getRequestDispatcher("/ui" + path).forward(request, response);
     }
   }
   
   public void destroy() {}
 }


