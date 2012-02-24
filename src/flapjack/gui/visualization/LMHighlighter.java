// Copyright 2007-2011 Plant Bioinformatics Group, SCRI. All rights reserved.
// Use is subject to the accompanying licence terms.

package flapjack.gui.visualization;

import java.awt.*;

/**
 * Line/Marker (LM) highligher class.
 */
public class LMHighlighter extends Thread implements IOverlayRenderer
{
	private GenotypeCanvas canvas;

	private int alphaEffect = 0;
	private int index = 0;
	private int method = 0;

	/**
	 * Constructs and runs a new highlighter. A previous instance can be passed
	 * to this object so it can ensure it has been killed before beginning the
	 * new highlighting.
	 */
	public LMHighlighter(GenotypePanel gPanel, int index, LMHighlighter previous, int method)
	{
		this.index = index;
		this.method = method;

		canvas = gPanel.canvas;

		if (previous != null)
		{
			previous.interrupt();
			canvas.overlays.remove(previous);
		}

		start();
	}

	public void run()
	{
		canvas.overlays.add(this);

		// Darken the regions around the line/marker
		alphaEffect = 200;
		canvas.repaint();

		// Then wait for a second
		try { Thread.sleep(3000); }
		catch (InterruptedException e) {}

		// Before fading the other lines back to normality
		for (int i = 1; i <= 40 && !isInterrupted(); i++)
		{
			// 40 * 5 = 200 (the starting alpha)
			alphaEffect = (int) (200 - (i * 5));
			canvas.repaint();

			// 25 * 40 = 1000 (1 second)
			try { Thread.sleep(25); }
			catch (InterruptedException e) {}
		}

		canvas.overlays.remove(this);
	}

	public void render(Graphics2D g)
	{
		if (method == 0)
			renderForLines(g);
		else
			renderForMarkers(g);
	}

	private void renderForLines(Graphics2D g)
	{
		g.setPaint(new Color(20, 20, 20, alphaEffect));

		int y1 = index * canvas.boxH;
		int y2 = y1 + canvas.boxH;

		g.fillRect(0, 0, canvas.canvasW, y1);
		g.fillRect(0, y2, canvas.canvasW, canvas.canvasH-y2);
	}

	private void renderForMarkers(Graphics2D g)
	{
		g.setPaint(new Color(20, 20, 20, alphaEffect));

		int x1 = index * canvas.boxW;
		int x2 = x1 + canvas.boxW;

		g.fillRect(0, 0, x1, canvas.canvasH);
		g.fillRect(x2, 0, canvas.canvasW-x2, canvas.canvasH);
	}
}