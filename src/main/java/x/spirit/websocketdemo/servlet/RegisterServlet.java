/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package x.spirit.websocketdemo.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zhangwei
 */
@WebServlet(name = "register", urlPatterns = {"/ping","/register"})
public class RegisterServlet extends HttpServlet{
    
    private static final long serialVersionUID = 5673352274263245911L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        System.out.println(req.getRequestURI());
        if (req.getRequestURI().equals("/ping")) {
            // query from the index. Get the peer addr
            String actionName = req.getParameter("action");
            long start = System.currentTimeMillis();
            String result = String.format("<html><head><title>%s</title></head><body><h1>Action = %s </h1></body></html>", actionName, actionName);
            System.out.println(String.format("Got Action = %s in %s milliseconds", actionName, System.currentTimeMillis() - start));
            // write the peer addr into the response
            if (result != null ){
                resp.getWriter().print(result);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File Not Found");
            }
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request!");
    }
}
