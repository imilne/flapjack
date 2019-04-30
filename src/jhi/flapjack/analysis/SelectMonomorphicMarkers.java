// Copyright 2009-2019 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.analysis;

import java.util.*;

import jhi.flapjack.data.*;

import scri.commons.gui.*;

public class SelectMonomorphicMarkers extends SimpleJob
{
	private GTViewSet viewSet;
	private boolean[] selectedChromosomes;
	private int count;

	public SelectMonomorphicMarkers(GTViewSet viewSet, boolean[] selectedChromosomes)
	{
		this.viewSet = viewSet;
		this.selectedChromosomes = selectedChromosomes;
	}

	public int getCount()
		{ return count; }

	public void runJob(int index)
		throws Exception
	{
		AnalysisSet as = new AnalysisSet(viewSet)
			.withViews(selectedChromosomes)
			.withSelectedLines()
			.withSelectedMarkers();

		for (int i = 0; i < as.viewCount(); i++)
			maximum += as.markerCount(i);


		for (int view = 0; view < as.viewCount(); view++)
		{
			boolean isSpecialChromosome = as.getGTView(view).getChromosomeMap().isSpecialChromosome();

			// For each marker...
			for (int marker = as.markerCount(view)-1; marker >= 0 && okToRun; marker--)
			{
				HashSet<Integer> foundStates = new HashSet<>();

				for (int line = 0; line < as.lineCount(); line++)
				{
					int state = as.getState(view, line, marker);

					// We don't want to add the unknown state
					if (state != 0)
						foundStates.add(state);

					// If we've found more than one state this marker isn't monomorphic
					if (foundStates.size() > 1)
					{
						as.getMarker(view, marker).selectMarkerAndLinkedMarker(false);
						break;
					}
				}

				// Select the marker if it's monomorphic
				if (foundStates.isEmpty() || foundStates.size() == 1)
				{
					as.getMarker(view, marker).selectMarkerAndLinkedMarker(true);

					if (isSpecialChromosome == false)
						count++;
				}
				progress++;
			}
		}
	}
}