package d3v.cc.server.api;

import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;

import d3v.cc.server.helper.PMF;
import d3v.cc.server.model.Clip;
import d3v.cc.server.model.ClipBoard;


@Api(name="clipboard")
public class ClipBoardEndpoint {
	
	private static final Logger log = Logger.getLogger(ClipBoardEndpoint.class
			.getName());
	
	
	@ApiMethod(name="clipboard.get")
	public ClipBoard get(User user) {
		String methodName = "get";
		log.entering(ClipBoardEndpoint.class.getName(), methodName, user);
		
		String key = user.getUserId();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ClipBoard result = null;
		try {
			result = pm.getObjectById(ClipBoard.class, key);
		} catch(JDOObjectNotFoundException e) {
			log.warning(String.format("No clipboard found for %s. Creating new one...", user));
			result = ClipBoard.createInstance(user);
		} finally {
			result.loadAll();
			pm.close();
		}
		
		log.fine(String.format("Found clipboard: %s", result.toString()));
		
		log.exiting(ClipBoardEndpoint.class.getName(), methodName);
		return result;
	}
	
	@ApiMethod(name="clipboard.add")
	public ClipBoard add(User user, Clip clip) {
		String methodName = "add";
		log.entering(ClipBoardEndpoint.class.getName(), methodName, new Object[]{user, clip});
		
		String key = user.getUserId();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ClipBoard result = null;
		
		try {
			result = pm.getObjectById(ClipBoard.class, key);
		} catch(JDOObjectNotFoundException e) {
			log.warning(String.format("No clipboard found for %s. Creating new one ...", user));
			result = ClipBoard.createInstance(user);
		} finally {
			result.addClip(clip);
			result.loadAll();
			pm.makePersistent(result);
			pm.close();
		}
		
		log.fine(String.format("Added clip: %s to clipboard: %s", clip, result));
		
		log.exiting(ClipBoardEndpoint.class.getName(), methodName);
		return result;
		
	}

}
