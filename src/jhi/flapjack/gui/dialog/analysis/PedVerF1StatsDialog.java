// Copyright 2009-2019 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.dialog.analysis;

import java.awt.event.*;
import javax.swing.*;

import jhi.flapjack.analysis.*;
import jhi.flapjack.data.*;
import jhi.flapjack.gui.*;

import scri.commons.gui.*;

public class PedVerF1StatsDialog extends JDialog implements ActionListener
{
	private ChromosomeSelectionDialog csd;

	private boolean isOK;

	private ButtonGroup f1Group;

	private DefaultComboBoxModel<LineInfo> parent1Model;
	private DefaultComboBoxModel<LineInfo> parent2Model;
	private DefaultComboBoxModel<LineInfo> f1Model;

	/**
	 * Creates new form PedVerStatsDialogNew
	 */
	public PedVerF1StatsDialog(GTViewSet viewSet)
	{
		super(
			Flapjack.winMain,
			RB.getString("gui.dialog.analysis.PedVerF1StatsDialog.title"),
			true
		);

		isOK = false;

        initComponents();
		initComponents2();

		AnalysisSet as = new AnalysisSet(viewSet)
			.withViews(null)
			.withSelectedLines()
			.withSelectedMarkers();

		csd = new ChromosomeSelectionDialog(viewSet, true, true);
		csdLabel.addActionListener(e -> csd.setVisible(true));

		setupF1ButtonGroup();
		setupComboBoxes(as);

		rdbSimulateF1.setSelected(true);

		FlapjackUtils.initDialog(this, bOK, bCancel, true,
			getContentPane(), jPanel1, parentsPanel);
	}

	private void setupComboBoxes(AnalysisSet as)
	{
		parent1Model = createComboModelFrom(as);
		parent1Combo.setModel(parent1Model);
		if (as.lineCount() >= 1)
			parent1Combo.setSelectedIndex(0);

		parent2Model = createComboModelFrom(as);
		parent2Combo.setModel(parent2Model);
		if (as.lineCount() >= 2)
			parent2Combo.setSelectedIndex(1);

		f1Model = createComboModelFrom(as);
		f1Combo.setModel(f1Model);
		if (as.lineCount() >= 3)
			f1Combo.setSelectedIndex(2);

		f1Combo.setEnabled(rdbSelectF1.isSelected());
	}

	private DefaultComboBoxModel<LineInfo> createComboModelFrom(AnalysisSet as)
	{
		DefaultComboBoxModel<LineInfo> model = new DefaultComboBoxModel<>();
		for (int i = 0; i < as.lineCount(); i++)
			model.addElement(as.getLine(i));

		return model;
	}

	private void initComponents2()
	{
//		RB.setText(bOK, "gui.text.ok");
		bOK.addActionListener(this);

		RB.setText(bCancel, "gui.text.cancel");
		bCancel.addActionListener(this);

		RB.setText(bHelp, "gui.text.help");
		FlapjackUtils.setHelp(bHelp, "pedver_f1s_known_parents.html");

		RB.setText(chkExcludeParents, "gui.dialog.analysis.MABCStatsDialog.chkExlcudeParents");
		chkExcludeParents.addActionListener(this);
		chkExcludeParents.setSelected(Prefs.guiPedVerF1sExcludeParents);

		rdbSelectF1.addActionListener(e -> f1Combo.setEnabled(rdbSelectF1.isSelected()) );
		rdbSimulateF1.addActionListener(e -> f1Combo.setEnabled(rdbSelectF1.isSelected()) );
	}

	private void setupF1ButtonGroup()
	{
		f1Group = new ButtonGroup();
		f1Group.add(rdbSelectF1);
		f1Group.add(rdbSimulateF1);
	}

	public boolean isOK()
		{ return isOK; }

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bOK)
		{
			isOK = true;
			setVisible(false);
		}

		else if (e.getSource() == bCancel)
			setVisible(false);

		else if (e.getSource() == chkExcludeParents)
			Prefs.guiPedVerF1sExcludeParents = chkExcludeParents.isSelected();
	}

	public int getParent1()
	{
		return parent1Combo.getSelectedIndex();
	}

	public int getParent2()
	{
		return parent2Combo.getSelectedIndex();
	}

	public int getF1()
	{
		return f1Combo.getSelectedIndex();
	}

	public boolean simulateF1()
	{
		return rdbSimulateF1.isSelected();
	}

	// Generates a boolean array with a true/false selected state for each of
	// the possible chromosomes that could be used in the sort
	public boolean[] getSelectedChromosomes()
	{
		return csd.getSelectedChromosomes();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        parentsPanel = new javax.swing.JPanel();
        lblParent1 = new javax.swing.JLabel();
        parent1Combo = new javax.swing.JComboBox<>();
        lblParent2 = new javax.swing.JLabel();
        parent2Combo = new javax.swing.JComboBox<>();
        rdbSimulateF1 = new javax.swing.JRadioButton();
        rdbSelectF1 = new javax.swing.JRadioButton();
        f1Combo = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        chkExcludeParents = new javax.swing.JCheckBox();
        dialogPanel1 = new scri.commons.gui.matisse.DialogPanel();
        bOK = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        csdLabel = new scri.commons.gui.matisse.HyperLinkLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        parentsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select parents:"));

        lblParent1.setText("Select parent line 1:");
        lblParent1.setToolTipText("");

        lblParent2.setText("Select parent line 2:");
        lblParent2.setToolTipText("");

        rdbSimulateF1.setText("Simulate an F1 from the parents (above)");

        rdbSelectF1.setActionCommand("");
        rdbSelectF1.setText("Select an F1 from the existing lines:");

        jLabel1.setText("<html>Pedigree Verification of F1s (Known Parents) will calculate statistics for each line comparing<br>it to the parents and either a supplied or simulated F1.");

        chkExcludeParents.setText("Exclude other parental lines from analysis and view");

        javax.swing.GroupLayout parentsPanelLayout = new javax.swing.GroupLayout(parentsPanel);
        parentsPanel.setLayout(parentsPanelLayout);
        parentsPanelLayout.setHorizontalGroup(
            parentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(parentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(parentsPanelLayout.createSequentialGroup()
                        .addComponent(lblParent1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parent1Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(parentsPanelLayout.createSequentialGroup()
                        .addComponent(lblParent2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parent2Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(parentsPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(f1Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(parentsPanelLayout.createSequentialGroup()
                        .addGroup(parentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkExcludeParents)
                            .addComponent(rdbSelectF1)
                            .addComponent(rdbSimulateF1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        parentsPanelLayout.setVerticalGroup(
            parentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, parentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(parentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblParent1)
                    .addComponent(parent1Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(parentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblParent2)
                    .addComponent(parent2Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chkExcludeParents)
                .addGap(18, 18, 18)
                .addComponent(rdbSimulateF1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdbSelectF1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f1Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bOK.setText("Run");
        dialogPanel1.add(bOK);

        bCancel.setText("Cancel");
        dialogPanel1.add(bCancel);

        bHelp.setText("Help");
        dialogPanel1.add(bHelp);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data selection settings:"));

        csdLabel.setText("Select chromosomes to analyse");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(csdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(csdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dialogPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(parentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(parentsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dialogPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bHelp;
    private javax.swing.JButton bOK;
    private javax.swing.JCheckBox chkExcludeParents;
    private scri.commons.gui.matisse.HyperLinkLabel csdLabel;
    private scri.commons.gui.matisse.DialogPanel dialogPanel1;
    private javax.swing.JComboBox<LineInfo> f1Combo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblParent1;
    private javax.swing.JLabel lblParent2;
    private javax.swing.JComboBox<LineInfo> parent1Combo;
    private javax.swing.JComboBox<LineInfo> parent2Combo;
    private javax.swing.JPanel parentsPanel;
    private javax.swing.JRadioButton rdbSelectF1;
    private javax.swing.JRadioButton rdbSimulateF1;
    // End of variables declaration//GEN-END:variables
}