package d3v.cc.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import d3v.cc.server.helper.PMF;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ClipBoard {
	
	private static final Logger log = Logger.getLogger(ClipBoard.class
			.getName());
	
	
	public static final int MAX_CLIPS = 8;
	public static final int MIN_CLIPS = 0;
	
	@PrimaryKey	
	private String key;
	
	@Persistent
	private Date created;
	
	@Persistent
	private Date updated;
	
	@Persistent
	private List<Clip> clips;
	
	protected ClipBoard(User user) {
		this.key = user.getUserId();
		this.created = new Date();
		this.clips = new ArrayList<Clip>(MAX_CLIPS);
		
//		for(int i = MIN_CLIPS; i < MAX_CLIPS; i++) {
//			this.clips.add(new Clip("" + i));
//		}
	}
	
	public String getKey() {
		return this.key;
	}
	
	public Date getCreated() {
		return this.created;
	}
	
	public List<Clip> getClips() {
		return this.clips;
	}
	
	public boolean addClip(Clip clip) {
		this.updated = new Date();
		
		this.clips.add(MIN_CLIPS, clip);
		
		if(this.clips.size() > MAX_CLIPS) {
			this.clips.remove(MAX_CLIPS);
		}
		
		assert(this.clips.size() <= MAX_CLIPS);
		
		return true;
	}
	
	public ClipBoard loadAll() {
		this.getKey();
		this.getCreated();
		
		for(Clip clip : this.getClips());
		
		return this;
	}
	
	@Override
	public String toString() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("clips", this.clips);
			result.put("key", this.key);
		} catch (JSONException e) {
			log.severe(e.toString());
		}

		return result.toString();
	}



	public static ClipBoard createInstance(User user) {
		ClipBoard result = new ClipBoard(user);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(result);
		} finally {
			pm.close();
		}
		
		return result;
	}
	
}
