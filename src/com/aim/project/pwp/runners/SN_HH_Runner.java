package com.aim.project.pwp.runners;

import AbstractClasses.HyperHeuristic;
import com.aim.project.pwp.hyperheuristics.SN_HH_LSR;
import com.aim.project.pwp.hyperheuristics.SN_HH;

public class SN_HH_Runner extends HH_Runner_Visual {
    @Override
    protected HyperHeuristic getHyperHeuristic(long seed) {

//        return new SN_HH_LSR(seed);
        return new SN_HH(seed);
    }

    public static void main(String [] args) {

        HH_Runner_Visual runner = new SN_HH_Runner();
        runner.run();
    }
}
