package com.pktworld.railway.model;

/**
 * Created by Prabhat on 16/04/16.
 */
public class Trains {

    FromStation dest;
    FromStation source;
    Train train;

    public FromStation getDest() {
        return dest;
    }

    public void setDest(FromStation dest) {
        this.dest = dest;
    }

    public FromStation getSource() {
        return source;
    }

    public void setSource(FromStation source) {
        this.source = source;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}
