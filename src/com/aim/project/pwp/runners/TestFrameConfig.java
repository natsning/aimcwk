package com.aim.project.pwp.runners;

//import com.aim.hyflex.HyFlexTestFrame;

public class TestFrameConfig {


	private final long RUN_TIME_IN_SECONDS = 60;
	private final int[] INSTANCE_IDs = {2,3,4};
	private final String[] PROBLEM_DOMAINS = { "AIM_PWP" };
	private final long[] SEEDS = {13032020L, 13032021L, 13032022L, 13032023L, 13032024L, 13032025L,
									13032026L, 13032027L, 13032028L, 13032029L, 13032030L};
	private final int TOTAL_RUNS = 11;

//	@Override
	public String[] getDomains() {

		return this.PROBLEM_DOMAINS;
	}

//	@Override
	public int[] getInstanceIDs() {

		return this.INSTANCE_IDs;
	}

//	@Override
	public long getRunTime() {

		return (463 * RUN_TIME_IN_SECONDS)*1_000L / 600;
	}

	public int getTotalRuns(){
		return TOTAL_RUNS;
	}

	public long[] getSeeds(){
		return SEEDS;
	}
}