// Copyright 2009-2019 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.dialog.analysis;

import java.util.*;
import javax.swing.*;

import jhi.flapjack.data.*;
import jhi.flapjack.data.results.*;
import jhi.flapjack.gui.*;

public class PedVerLinesStatsBatchPanelNB extends JPanel
{
	private ArrayList<GTViewSet> viewSets;

	private PedVerLinesThresholdDialog pedVerLinesThresholdDialog;

	public PedVerLinesStatsBatchPanelNB(ArrayList<GTViewSet> viewSets)
	{
		this.viewSets = viewSets;

		System.out.println("Batch analysis could run on " + viewSets.size() + " datasets");

		initComponents();
		initComponents2();
	}

	private void initComponents2()
	{
		FlapjackUtils.initPanel(dataPanel, thresholdPanel);

		pedVerLinesThresholdDialog = new PedVerLinesThresholdDialog();
		lblThreshold.addActionListener(e -> pedVerLinesThresholdDialog.setVisible(true));
	}

	public boolean isOK()
	{
		return true;
	}

	public PedVerLinesThresholds getThresholds()
	{
		return pedVerLinesThresholdDialog.getThresholds();
	}

	public boolean isAutoSelectVerifiedLines()
	{
		return pedVerLinesThresholdDialog.isAutoSelectVerifiedLines();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        thresholdPanel = new javax.swing.JPanel();
        csdLabel = new scri.commons.gui.matisse.HyperLinkLabel();
        lblThreshold = new scri.commons.gui.matisse.HyperLinkLabel();

        dataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("General settings:"));

        jLabel1.setText("There are no general settings applicable to a batch run of this analysis type.");

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        thresholdPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data selection settings:"));

        csdLabel.setText("Select chromosomes to analyse");

        lblThreshold.setText("Select threshold settings");

        javax.swing.GroupLayout thresholdPanelLayout = new javax.swing.GroupLayout(thresholdPanel);
        thresholdPanel.setLayout(thresholdPanelLayout);
        thresholdPanelLayout.setHorizontalGroup(
            thresholdPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thresholdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(thresholdPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(csdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        thresholdPanelLayout.setVerticalGroup(
            thresholdPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thresholdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(csdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(thresholdPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thresholdPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private scri.commons.gui.matisse.HyperLinkLabel csdLabel;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JLabel jLabel1;
    private scri.commons.gui.matisse.HyperLinkLabel lblThreshold;
    private javax.swing.JPanel thresholdPanel;
    // End of variables declaration//GEN-END:variables
}