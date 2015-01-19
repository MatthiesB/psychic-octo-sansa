package de.haw.schiffeversenken;

import de.uniba.wiai.lspi.chord.data.ID;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by matthies on 17.01.15.
 */
public class SVPlayer implements Comparable<SVPlayer> {

    private static final int EMPTY = 0;
    private static final int SHIP = 1;
    private static final int HIT = 2;
    private static final int MISS = 3;

    private ID id = null; // own ID
    private int[] fields; // marking hits and calc non-hit areas
    private LinkedList<SVShot> shots; // cache of fired shots
    private ArrayList<ID> intervalls; // intervall boundaries
    private int hitcount = 0; // count hits for strategy purposes and win detection
    private int I; //
    private BigInteger range; // range between intervall boundaries


    // for the own player representation
    public SVPlayer(ID id, int I, int S) {
        this(id, I);

        int tmpVal;
        for(int i = 0; i < S; i++) {
            do {
                tmpVal = (int)(Math.random() * I);
            } while (fields[tmpVal] != EMPTY);
            fields[tmpVal] = SHIP;
        }
    }

    // for enemy players
    public SVPlayer(ID id, int I) {
        this.id = id;
        shots = new LinkedList<SVShot>();
        fields = new int[I];
        this.I = I;
        intervalls = new ArrayList<ID>();
        java.util.Arrays.fill(fields, EMPTY);
    }

    // for strategy and win detection
    public int getHitcount() {
        return hitcount;
    }

    // for strategy
    public int getShotcount() {
        return this.shots.size();
    }

    public ID getId() {
        return id;
    }

    // calculate target in this players battlefield, starting from node id towards predecessor
    public ID GetTargetDownward() {
        for(int i = fields.length-1; i >= 0; i--) {
            if(fields[i] == EMPTY) {
                return ID.valueOf(this.intervalls.get(i).toBigInteger().add((range.divide(new BigInteger("2")))));
            }
        }
        return null;
    }

    // calculate target in this players battlefield, starting from predecessor towards node id
    public ID GetTargetUpward() {
        for(int i = 0; i < fields.length; i++) {
            if(fields[i] == EMPTY) {
                return ID.valueOf(this.intervalls.get(i).toBigInteger().add((range.divide(new BigInteger("2")))));
            }
        }
        return null;
    }

    // handling the broadcast
    public void Shot(SVShot shot) {
        this.shots.add(shot);
        if(shot.isHit()) {
            this.hitcount++;
        }
        this.CalcHit(shot);
    }

    // writing external shotinfo in data model
    private void CalcHit(SVShot shot) {
        for(int i = 1; i < this.I; i++) {
            if(shot.getTarget().isInInterval(this.intervalls.get(i - 1), this.intervalls.get(i))) {
                if(shot.isHit()) {
                    fields[i-1] = HIT;
                } else {
                    fields[i-1] = MISS;
                }
            }
        }
        if(shot.getTarget().isInInterval(this.intervalls.get(this.intervalls.size() - 1), this.id)) {
            if(shot.isHit()) {
                fields[this.intervalls.size()-1] = HIT;
            } else {
                fields[this.intervalls.size()-1] = MISS;
            }
        }
    }

    // handling the retrieve
    public boolean Inbound(ID target) {
        for(int i = 1; i < this.I; i++) {
            if(target.isInInterval(this.intervalls.get(i - 1), this.intervalls.get(i))) {
                if(fields[i-1] == SHIP) {
                    this.shots.add(new SVShot(target, true));
                    this.hitcount++;
                    return true;
                } else {
                    this.shots.add(new SVShot(target, false));
                    return false;
                }
            }
        }
        if(target.isInInterval(this.intervalls.get(this.intervalls.size() - 1), this.id)) {
            if(fields[this.intervalls.size()-1] == SHIP) {
                this.shots.add(new SVShot(target, true));
                this.hitcount++;
                return true;
            } else {
                this.shots.add(new SVShot(target, false));
                return false;
            }
        }
        return false; // should never happen...
    }

    // (re-)calculate the intervals of the node
    public void CalculateIntervalls(ID start, ID end) {
        range = end.toBigInteger().subtract(start.toBigInteger());
        BigInteger intervall = range.divide(new BigInteger(""+this.I));
        this.intervalls = new ArrayList<ID>(I);
        BigInteger counter = start.toBigInteger();
        for(int i = 0; i < this.I; i++) {
            this.intervalls.add(i, ID.valueOf(counter));
            counter = counter.add(intervall);
        }

        fields = new int[this.I];
        for(SVShot x : shots) {
            this.CalcHit(x);
        }
    }

    @Override
    public int compareTo(SVPlayer o) {
        return id.compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SVPlayer svPlayer = (SVPlayer) o;

        if (id != null ? !id.equals(svPlayer.id) : svPlayer.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
