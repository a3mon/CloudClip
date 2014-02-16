package d3v.cc.client.java;

import java.util.logging.Logger;

import d3v.cc.client.java.model.monitors.ISystemClipBoardListener;
import d3v.cc.client.java.model.monitors.ISystemClipBoardMonitor;
import d3v.cc.client.java.model.monitors.SystemClipBoardMonitor;

public class Controller implements ISystemClipBoardListener {
	
	private static final Logger log = Logger.getLogger(Controller.class
			.getName());
	
	
	public Controller() {
		ISystemClipBoardMonitor scbMonitor = new SystemClipBoardMonitor();
		scbMonitor.addSystemClipBoardListener( this );
	}

	@Override
	public void systemClipBoardChanged(String clip) {
		log.info(clip);
		
	}

}
