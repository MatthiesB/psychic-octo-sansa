package de.haw.schiffeversenken;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Created by matthies on 19.01.15.
 */
public class SVShot {
    private boolean hit; // Hitmarker
    private ID target; // ID of shot-target

    // represents a shot in the data model
    public SVShot(ID target, boolean hit) {
        this.target = target;
        this.hit = hit;
    }

    public boolean isHit() {
        return hit;
    }

    public ID getTarget() {
        return target;
    }
}
