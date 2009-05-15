package flapjack.gui.dialog.prefs;

import java.awt.*;
import javax.swing.*;

import flapjack.gui.*;

import scri.commons.gui.*;

class NBWarningPanel extends JPanel implements IPrefsTab
{
	public NBWarningPanel()
    {
        initComponents();

        setBackground((Color)UIManager.get("fjDialogBG"));
        panel.setBackground((Color)UIManager.get("fjDialogBG"));

		panel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.prefs.NBWarningPanel.panelTitle")));

		RB.setText(warnDuplicateMarkers, "gui.dialog.prefs.NBWarningPanel.warnDuplicateMarkers");
		RB.setText(warnEditMarkerMode, "gui.dialog.prefs.NBWarningPanel.warnEditMarkerMode");
		RB.setText(warnEditLineMode, "gui.dialog.prefs.NBWarningPanel.warnEditLineMode");

		initSettings();
    }

    private void initSettings()
    {
    	warnDuplicateMarkers.setSelected(Prefs.warnDuplicateMarkers);
		warnEditMarkerMode.setSelected(Prefs.warnEditMarkerMode);
		warnEditLineMode.setSelected(Prefs.warnEditLineMode);
    }

	public void applySettings()
	{
		Prefs.warnDuplicateMarkers = warnDuplicateMarkers.isSelected();
		Prefs.warnEditMarkerMode = warnEditMarkerMode.isSelected();
		Prefs.warnEditLineMode = warnEditLineMode.isSelected();
	}

	public void setDefaults()
	{
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        warnDuplicateMarkers = new javax.swing.JCheckBox();
        warnEditMarkerMode = new javax.swing.JCheckBox();
        warnEditLineMode = new javax.swing.JCheckBox();

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Inform me:"));

        warnDuplicateMarkers.setText("When duplicate markers are found during data import");

        warnEditMarkerMode.setText("When switching to 'marker mode'");

        warnEditLineMode.setText("When switching to 'line mode'");

        org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(warnDuplicateMarkers)
                    .add(warnEditMarkerMode)
                    .add(warnEditLineMode))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(warnDuplicateMarkers)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(warnEditMarkerMode)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(warnEditLineMode)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private javax.swing.JCheckBox warnDuplicateMarkers;
    private javax.swing.JCheckBox warnEditLineMode;
    private javax.swing.JCheckBox warnEditMarkerMode;
    // End of variables declaration//GEN-END:variables
}