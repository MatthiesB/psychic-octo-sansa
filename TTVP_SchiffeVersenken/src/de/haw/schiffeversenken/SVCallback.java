/**
 * 
 */
package de.haw.schiffeversenken;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;

/**
 * @author networker
 *
 */
public class SVCallback implements NotifyCallback {

	/* (non-Javadoc)
	 * @see de.uniba.wiai.lspi.chord.service.NotifyCallback#retrieved(de.uniba.wiai.lspi.chord.data.ID)
	 */
	@Override
	public void retrieved(ID target) {
		// TODO Auto-generated method stub
		System.out.println("Retrieved!");
	}

	/* (non-Javadoc)
	 * @see de.uniba.wiai.lspi.chord.service.NotifyCallback#broadcast(de.uniba.wiai.lspi.chord.data.ID, de.uniba.wiai.lspi.chord.data.ID, java.lang.Boolean)
	 */
	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		// TODO Auto-generated method stub
		System.out.println("BROADCAST!!!!");
	}

}
