// Copyright 2009-2016 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.dialog;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import jhi.flapjack.data.*;
import jhi.flapjack.data.pedigree.*;
import jhi.flapjack.gui.*;

import scri.commons.gui.*;

public class SelectParentsDialog extends JDialog implements ActionListener
{
	private GTView view;
	private PedManager pm;

	private DefaultComboBoxModel<IndexWrapper> p1Model = new DefaultComboBoxModel<>();
	private DefaultComboBoxModel<IndexWrapper> p2Model = new DefaultComboBoxModel<>();
	private ArrayList<IndexWrapper> allLines = new ArrayList<>();

	private boolean isOK;

	public SelectParentsDialog(GTView view)
	{
		super(
			Flapjack.winMain,
			RB.getString("gui.dialog.SelectParentsDialog.title"),
			true
		);

		this.view = view;
		isOK = false;

		initComponents();
		initComponents2();

		FlapjackUtils.initDialog(this, bOK, bCancel, true, getContentPane());
	}

	private void initComponents2()
	{
		RB.setText(bOK, "gui.text.ok");
		bOK.addActionListener(this);

		RB.setText(bCancel, "gui.text.cancel");
		bCancel.addActionListener(this);

		// Enable or disable the parentsOnly option (based on ped manager)
		pm = view.getViewSet().getDataSet().getPedManager();
			if (pm.getPedigrees().size() == 0)
				parentsOnly.setEnabled(false);

		parentsOnly.addActionListener(this);

		// Create a (wrapped) list of all lines
		for (int i = 0; i < view.lineCount(); i++)
			allLines.add(new IndexWrapper(view.getLineInfo(i), i));

		initModels();

		// First run only; can we use the mouse-over info to set some default
		// selections? This assumes we're showing the full list.
		if (parentsOnly.isSelected() == false)
		{
			int index = view.mouseOverLine;
			if (p1Model.getSize() > index)
				p1Combo.setSelectedIndex(index);
			if (p2Model.getSize() > (index+1))
				p2Combo.setSelectedIndex((index+1));
		}

		p1Combo.addActionListener(e -> checkStates());
		p2Combo.addActionListener(e -> checkStates());
		checkStates();
	}

	private void checkStates()
	{
		// Only allow OK if different lines are selected. This also catches the
		// situation with only 1 active line (as both indices will be equal)
		bOK.setEnabled(p1Combo.getSelectedIndex() != p2Combo.getSelectedIndex());
	}

	private void initModels()
	{
		initModel(p1Combo, p1Model);
		initModel(p2Combo, p2Model);

		checkStates();
	}

	private void initModel(JComboBox<IndexWrapper> combo, DefaultComboBoxModel<IndexWrapper> model)
	{
		boolean parOnly = parentsOnly.isSelected();

		// Remember the current selection
		IndexWrapper sel = model.getElementAt(combo.getSelectedIndex());

		model.removeAllElements();
		for (int i = 0; i < allLines.size(); i++)
			if (!parOnly || (parOnly && pm.isParent(allLines.get(i).line)))
				model.addElement(allLines.get(i));
		combo.setModel(model);

		// Try to reapply the current selection after the list has repopulated
		if (sel != null)
			combo.setSelectedItem(sel);
	}

	static class IndexWrapper
	{
		LineInfo line;
		int index;

		IndexWrapper(LineInfo line, int index)
		{
			this.line = line;
			this.index = index;
		}

		public String toString()
			{ return line.toString(); }
	}

	public int getParent1()
	{
		IndexWrapper wrapper = (IndexWrapper) p1Combo.getSelectedItem();
		return wrapper.index;
	}

	public int getParent2()
	{
		IndexWrapper wrapper = (IndexWrapper) p2Combo.getSelectedItem();
		return wrapper.index;
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

		else if (e.getSource() == parentsOnly)
			initModels();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        dialogPanel1 = new scri.commons.gui.matisse.DialogPanel();
        bOK = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        p2Label = new javax.swing.JLabel();
        p1Label = new javax.swing.JLabel();
        p1Combo = new javax.swing.JComboBox<>();
        p2Combo = new javax.swing.JComboBox<>();
        parentsOnly = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        bOK.setText("OK");
        dialogPanel1.add(bOK);

        bCancel.setText("Cancel");
        dialogPanel1.add(bCancel);

        p2Label.setText("Select 2nd parent line:");
        p2Label.setToolTipText("");

        p1Label.setText("Select 1st parent line:");
        p1Label.setToolTipText("");

        parentsOnly.setText("Only list known parental lines (using any imported pedigree information)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dialogPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(parentsOnly)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(p1Label)
                            .addComponent(p2Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(p2Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(p1Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p1Label)
                    .addComponent(p1Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p2Label)
                    .addComponent(p2Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(parentsOnly)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dialogPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bOK;
    private scri.commons.gui.matisse.DialogPanel dialogPanel1;
    private javax.swing.JComboBox<IndexWrapper> p1Combo;
    private javax.swing.JLabel p1Label;
    private javax.swing.JComboBox<IndexWrapper> p2Combo;
    private javax.swing.JLabel p2Label;
    private javax.swing.JCheckBox parentsOnly;
    // End of variables declaration//GEN-END:variables

}