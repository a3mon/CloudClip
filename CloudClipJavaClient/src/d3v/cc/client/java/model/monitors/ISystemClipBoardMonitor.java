package d3v.cc.client.java.model.monitors;

public interface ISystemClipBoardMonitor {

    public abstract boolean addSystemClipBoardListener( ISystemClipBoardListener listener );

    public abstract boolean removeSystemClipBoardListener( ISystemClipBoardListener listener );

}