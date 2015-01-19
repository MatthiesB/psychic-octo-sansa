package de.haw.schiffeversenken;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by matthies on 17.01.15.
 */
public class SVLogic extends Thread {

    private List<SVPlayer> Ocean;
    private ChordImpl chord;
    private SVPlayer ownPlayer;
    private SVPlayer predPlayer;
    private boolean ShotFired = false;
    private ID Target;

    // this class handles the game logic
    public SVLogic(ChordImpl chord) {
        this.chord = chord;
        Ocean = new LinkedList<SVPlayer>();
    }


    // chord is up now, gather all accessible data
    public void Init() {
        ownPlayer = new SVPlayer(chord.getID(), 100, 10);
        predPlayer = new SVPlayer(chord.getPredecessorID(), 100);
        Ocean.add(ownPlayer);
        Ocean.add(predPlayer);
        this.RecalcOcean();
    }

    // calculate a target and do the retrieve (called by tun()-method)
    public void Fire() {
        for(SVPlayer player : Ocean) {
            if(!player.equals(ownPlayer)) {
                if(player.getShotcount() == 99) {
                    ShotFired = true;
                    chord.retrieve(player.GetTargetUpward());
                    break;
                }
                if(player.getHitcount() == 9) {
                    ShotFired = true;
                    chord.retrieve(player.GetTargetUpward());
                    break;
                }
            }
        }
        ShotFired = true;
        chord.retrieve(predPlayer.GetTargetUpward());
    }

    // we are the first du shot!
    public void FirstFire() {
        synchronized (this) {
            this.notify();
        }
    }

    // handle retrieve
    public void InboundShot(ID target) {
        chord.broadcast(target, ownPlayer.Inbound(target)); // handle broadcasting and update of the data model
        synchronized (this) {
            this.notify(); // activate out shooting
        }
    }

    // handle broadcast
    public void Shot(ID source, ID target, boolean hit) {
        SVPlayer tmpPlayer = new SVPlayer(source, 100);
        int indx = Ocean.indexOf(tmpPlayer);
        if(indx != -1) { // player already exists?
            Ocean.get(indx).Shot(new SVShot(target, hit)); // update player
            if(ShotFired && Ocean.get(indx).getHitcount() == 10) { // check for win
                System.out.println("Winner!");
                System.exit(0);
            }
        } else {
            tmpPlayer.Shot(new SVShot(target, hit));
            Ocean.add(tmpPlayer); // add new player
            RecalcOcean(); // update data model
        }
        ShotFired = false;
    }

    // recalculate data model
    private void RecalcOcean() {
        Collections.sort(Ocean);
        Ocean.get(0).CalculateIntervalls(ID.valueOf(new BigInteger("0")), Ocean.get(0).getId());
        for(int i = 1; i < Ocean.size(); i++) {
            Ocean.get(i).CalculateIntervalls(Ocean.get(i-1).getId(), Ocean.get(i).getId());
        }
    }

    // threads run() method. Handles the attacks.
    public void run() {
        while(true) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Wake Up!");
            this.Fire();
        }
    }
}
