/**
 * 
 */
package de.haw.schiffeversenken;

import java.net.MalformedURLException;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;

/**
 * @author networker
 *
 */
public class SchiffeVersenken {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		URL remoteURL = null;
		
		try {
			localURL = new URL(protocol + "://141.22.27.33:4244/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		try {
			remoteURL = new URL(protocol + "://141.22.27.30:4242/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		
		Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();

		SVCallback callback = new SVCallback();
		chord.setCallback(callback); 
		
		try {
			chord.join(localURL, remoteURL);
			//chord.create(localURL);
		} catch (ServiceException e) {
			throw new RuntimeException("Could not create DHT!", e);
		}
		System.out.println("Wuhey!");
		while(true) {
			chord.broadcast(chord.getID(), false);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
