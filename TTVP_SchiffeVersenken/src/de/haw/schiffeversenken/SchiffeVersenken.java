/**
 * 
 */
package de.haw.schiffeversenken;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Collections;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.util.logging.SystemOutPrintlnLogger;

/**
 * @author networker
 *
 */
public class SchiffeVersenken {

	private static SVLogic GameLogic;

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		// check params
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

		// init chord
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

		ChordImpl chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();

		GameLogic = new SVLogic(chord);

		SVCallback callback = new SVCallback(GameLogic);
		chord.setCallback(callback);

		// join or create?
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

		// wait for all players to be ready
		System.out.println("Press enter to continue...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Game running!");

		GameLogic.Init(); // init game logic
		GameLogic.start(); // start game logic thread

		BigInteger MaxID = ((new BigInteger("2").pow(160)).subtract(new BigInteger("1")));


		System.out.println("ID: "+chord.getID().toHexString(4) + " PrdID: "+chord.getPredecessorID().toHexString(4));

		if(ID.valueOf(MaxID).isInInterval(chord.getPredecessorID(), chord.getID())) { // are we the first to shoot?
			System.out.println("First Fire!");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GameLogic.FirstFire(); // shoot!
		}

		System.out.println("Press enter to cancel game...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		chord.leave();
		System.exit(0);
	}

}
