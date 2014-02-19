package d3v.cc.server.model;

import java.util.logging.Logger;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Clip {
	
	private static final Logger log = Logger.getLogger(Clip.class.getName());
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String encodedKey;
	
	@Persistent
	private String clip = "";
	
	public Clip() {
	}
	
	public Clip(String clip) {
		this.clip = clip;
	}
	
	public String getClip() {
		return this.clip;
	}
	
	@Override
	public String toString() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("encodedKey", this.encodedKey);
			result.put("clip", this.clip);
		} catch (JSONException e) {
			log.severe(e.toString());
		}

//		return result.toString();
		return this.clip;
	}
	
}
