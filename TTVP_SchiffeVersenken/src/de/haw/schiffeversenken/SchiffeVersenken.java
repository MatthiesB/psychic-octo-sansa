/**
 * 
 */
package de.haw.schiffeversenken;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.util.logging.SystemOutPrintlnLogger;

/**
 * @author networker
 *
 */
public class SchiffeVersenken {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if(args.length < 3) {
			System.out.println("Wrong Args!");
			System.out.println("Usage: SchiffeVersenken {create|join} ip port [remoteIP remotePort]");
			System.exit(1);
		}

		if(args[0].equals("join") && args.length < 5) {
			System.out.println("Wrong Args!");
			System.out.println("Usage: SchiffeVersenken {create|join} ip port [remoteIP remotePort]");
			System.exit(1);
		}

		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		URL remoteURL = null;
		
		try {
			localURL = new URL(protocol + "://" + args[1] + ":" + args[2] + "/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		if(args[0].equals("join")) {
			try {
				remoteURL = new URL(protocol + "://" + args[3] + ":" + args[4] + "/");
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

		Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();

		SVCallback callback = new SVCallback();
		chord.setCallback(callback);

		if(args[0].equals("join")) {
			try {
				chord.join(localURL, remoteURL);
			} catch (ServiceException e) {
				throw new RuntimeException("Could not join DHT!", e);
			}
		} else if(args[0].equals("create")) {
			try {
				chord.create(localURL);
			} catch (ServiceException e) {
				throw new RuntimeException("Could not create DHT!", e);
			}
		} else {
			System.out.println("Wrong Args!");
			System.out.println("Usage: SchiffeVersenken {create|join} ip port [remoteIP remotePort]");
			System.exit(1);
		}

		System.out.println("Press enter to continue...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(chord.retrieve(chord.getID()).)

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
