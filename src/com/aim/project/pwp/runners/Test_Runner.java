package com.aim.project.pwp.runners;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.Utilities;
import com.aim.project.pwp.hyperheuristics.SN_HH;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;
import com.aim.project.pwp.instance.Location;
import jdk.jshell.execution.Util;

import java.util.Arrays;

public class Test_Runner {

	final TestFrameConfig config;

	final int TOTAL_RUNS;
	final int[] INSTANCE_IDs;
	final long RUN_TIME;
	final long[] SEEDS;

	public Test_Runner(TestFrameConfig config) {

		this.config = config;

		this.TOTAL_RUNS = config.getTotalRuns();
		this.INSTANCE_IDs = config.getInstanceIDs();
		this.SEEDS = config.getSeeds();
		this.RUN_TIME = config.getRunTime();

	}

	public void runTests() {

		double[] bestSolutionFitness_sn = new double[TOTAL_RUNS];
		double bestFitness_sn;
		double[] bestSolutionFitness_sr = new double[TOTAL_RUNS];
		double bestFitness_sr;
		Location[] bestPath = null;
		StringBuilder sb_sn = new StringBuilder();
		StringBuilder sb_sr = new StringBuilder();

		for(int instance = 1; instance < 2; instance++) {

			bestFitness_sn = Double.MAX_VALUE;
			bestFitness_sr = Double.MAX_VALUE;
			for(int run = 0; run < TOTAL_RUNS; run++) {

				long seed = SEEDS[run];

				HyperHeuristic sn_hh = new SN_HH(seed);
				AIM_PWP sn_problem = new AIM_PWP(seed);
				sn_problem.loadInstance(INSTANCE_IDs[instance]);
				sn_hh.setTimeLimit(RUN_TIME);
				sn_hh.loadProblemDomain(sn_problem);
				sn_hh.run();

				bestSolutionFitness_sn[run] = sn_hh.getBestSolutionValue();
				if(bestSolutionFitness_sn[run] < bestFitness_sn){
					bestFitness_sn = bestSolutionFitness_sn[run];
					bestPath = sn_problem.getRouteOrderedByLocations();
				}
				System.out.println(sn_hh.toString()+": Instance ID: " + INSTANCE_IDs[instance] + "\tTrial: " + run + "\tf(s_{best}) = " + bestSolutionFitness_sn[run]+"\n");

			}
				new SolutionPrinter("randomOutput.txt").printSolution(Arrays.asList(bestPath));

//			for(int run = 0; run < TOTAL_RUNS; run++) {
//
//				long seed = SEEDS[run];
//
//				HyperHeuristic sr_hh = new SR_IE_HH(seed);
//				ProblemDomain sr_problem = new AIM_PWP(seed);
//				sr_problem.loadInstance(INSTANCE_IDs[instance]);
//				sr_hh.setTimeLimit(RUN_TIME);
//				sr_hh.loadProblemDomain(sr_problem);
//				sr_hh.run();
//
//				bestSolutionFitness_sr[run] = sr_hh.getBestSolutionValue();
//				if(bestSolutionFitness_sr[run] < bestFitness_sr){
//					bestFitness_sr = bestSolutionFitness_sr[run];
//				}
//				System.out.println(sr_hh.toString() + ": Instance ID: " + INSTANCE_IDs[instance] + "\tTrial: " + run + "\tf(s_{best}) = " + bestSolutionFitness_sr[run]+"\n");
//
//			}


			sb_sn.append(" SN_HH-" + INSTANCE_IDs[instance] +": "+ bestFitness_sn);
//			sb_sr.append(" SR_HH-" + INSTANCE_IDs[instance] +": "+ bestFitness_sr);
		}

		System.out.println(sb_sn);
		System.out.println(sb_sr);
	}


	public static void main(String [] args) {

		new Test_Runner(new TestFrameConfig()).runTests();
	}
}
