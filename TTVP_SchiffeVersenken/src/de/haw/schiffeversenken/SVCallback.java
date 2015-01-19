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

	private SVLogic GameLogic;

	public SVCallback (SVLogic GameLogic) {
		this.GameLogic = GameLogic;
	}

	/* (non-Javadoc)
	 * @see de.uniba.wiai.lspi.chord.service.NotifyCallback#retrieved(de.uniba.wiai.lspi.chord.data.ID)
	 */
	@Override
	public void retrieved(ID target) {
		System.out.println("Retrieved! Target: "+target.toHexString(4));
		// inform game logic
		GameLogic.InboundShot(target);
	}

	/* (non-Javadoc)
	 * @see de.uniba.wiai.lspi.chord.service.NotifyCallback#broadcast(de.uniba.wiai.lspi.chord.data.ID, de.uniba.wiai.lspi.chord.data.ID, java.lang.Boolean)
	 */
	@Override
	public void broadcast(ID source, ID target, Boolean hit) {
		// inform game logic
		GameLogic.Shot(source, target, hit);
	}

}
