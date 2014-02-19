package d3v.cc.server;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import d3v.cc.server.helper.PMF;
import d3v.cc.server.model.ClipBoard;

@SuppressWarnings("serial")
public class CloudClipServerServlet extends HttpServlet {
	
	private static final Logger log = Logger
			.getLogger(CloudClipServerServlet.class.getName());
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		log.entering(CloudClipServerServlet.class.getName(), "doGet");
		
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if(user == null) {
        	resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
		
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		
//		ClipBoard cb = new ClipBoard(user);
//		cb.addClip(new Date().toString());
//		
//		log.finer(cb.toString());
//		
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		pm.makePersistent(cb);
		
		log.exiting(CloudClipServerServlet.class.getName(), "doGet");
	}
}
