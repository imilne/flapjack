// Copyright 2009-2018 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.dialog.importer;

import java.awt.*;
import javax.swing.*;

import jhi.flapjack.gui.*;
import jhi.flapjack.io.brapi.*;

import scri.commons.gui.*;

class BrapiPassPanelNB extends JPanel implements IBrapiWizard
{
	private BrapiClient client;
	private BrapiImportDialog dialog;

	private boolean isAuthenticated = false;

	public BrapiPassPanelNB(BrapiClient client, BrapiImportDialog dialog)
	{
		this.client = client;
		this.dialog = dialog;

		initComponents();

		useStudies.setSelected(Prefs.guiBrAPIUseStudies);
		useMaps.setSelected(Prefs.guiBrAPIUseMaps);

		useAuthentication.addActionListener(e -> enableAuthenticationOptions());
	}

	private void enableAuthenticationOptions()
	{
		boolean state = useAuthentication.isSelected();

		userLabel.setEnabled(state);
		passLabel.setEnabled(state);
		username.setEnabled(state);
		password.setEnabled(state);
		saveCredentials.setEnabled(state);
	}

	boolean getCallsData()
	{
		ProgressDialog pd = new ProgressDialog(new CallsDownloader(),
			RB.getString("gui.dialog.importer.BrapiDataPanelNB.title2"),
			RB.getString("gui.dialog.importer.BrapiDataPanelNB.message2"),
			Flapjack.winMain);

		if (pd.failed("gui.error"))
			return false;

		return true;
	}

	private class CallsDownloader extends SimpleJob
	{
		public void runJob(int jobID)
			throws Exception
		{
			client.initService();
			client.getCalls();
		}
	}

	void updateLabels()
	{
		try
		{
			Image img = client.getResource().getImage().getImage();
			img = img.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
			connectionLabel.setIcon(new ImageIcon(img));
		} catch (Exception e) {}

		if (client.getResource().getName() != null)
			connectionLabel.setText("Connecting to " + client.getResource().getName());
		else
			connectionLabel.setText("Connecting to " + client.getResource().getUrl());

		// Grab (any) previously cached auth details and fill them in
		AuthManager.Credentials c = AuthManager.getCredentials(client.getResource().getUrl());
		useAuthentication.setSelected(c.useAuthentication());
		username.setText(c.getUsername());
		password.setText(c.getPassword());
		saveCredentials.setSelected(c.saveCredentials());
		enableAuthenticationOptions();
	}

	@Override
	public void onShow()
	{
		updateLabels();

		getCallsData();

		useMaps.setEnabled(client.hasMaps());
		useStudies.setEnabled(client.hasStudiesSearchGET() || client.hasStudiesSearchPOST());

		dialog.enableBack(true);
		dialog.enableNext(true);
	}

	@Override
	public void onNext()
	{
		Prefs.guiBrAPIUseStudies = useStudies.isEnabled() && useStudies.isSelected();
		Prefs.guiBrAPIUseMaps = useMaps.isEnabled() && useMaps.isSelected();

		AuthManager.setCredentials(client.getResource().getUrl(),
			useAuthentication.isSelected(), saveCredentials.isSelected(),
			username.getText(), new String(password.getPassword()));

		if (client.validateCalls() == false)
			return;

		if (useAuthentication.isSelected() && !refreshData())
			return;

		if (Prefs.guiBrAPIUseStudies)
		{
			if (dialog.getStudiesPanel().refreshData())
				dialog.setScreen(dialog.getStudiesPanel());
		}

		else if (Prefs.guiBrAPIUseMaps)
		{
			if (dialog.getMapsPanel().refreshData())
				dialog.setScreen(dialog.getMapsPanel());
		}

		else if (client.hasAlleleMatrices())
		{
			if (dialog.getMatricesPanel().refreshData())
				dialog.setScreen(dialog.getMatricesPanel());
		}

		else
			dialog.wizardCompleted();

		dialog.getBNext().requestFocusInWindow();
	}

	@Override
	public JPanel getPanel()
		{ return this; }

	@Override
	public String getCardName()
		{ return "pass"; }

	public boolean refreshData()
	{
		ProgressDialog pd = new ProgressDialog(new DataDownloader(),
			RB.getString("gui.dialog.importer.BrapiPassPanelNB.title"),
			RB.getString("gui.dialog.importer.BrapiPassPanelNB.message"),
			Flapjack.winMain);

		if (pd.failed("gui.error"))
			return false;

		if (!isAuthenticated)
		{
			TaskDialog.error(
				RB.getString("gui.dialog.importer.BrapiPassPanelNB.error"),
				RB.getString("gui.text.close"));
			return false;
		}

		return true;
	}

	private class DataDownloader extends SimpleJob
	{
		public void runJob(int jobID)
			throws Exception
		{
			client.setUsername(username.getText());
			client.setPassword(new String(password.getPassword()));

			isAuthenticated = client.doAuthentication();
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        connectionLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        passLabel = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        useAuthentication = new javax.swing.JCheckBox();
        saveCredentials = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        useStudies = new javax.swing.JCheckBox();
        useMaps = new javax.swing.JCheckBox();
        specLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Authentication details (optional):"));

        connectionLabel.setText("Connecting to resource:");

        userLabel.setText("Username to connect as:");
        userLabel.setEnabled(false);

        username.setEnabled(false);

        passLabel.setText("Password:");
        passLabel.setEnabled(false);

        password.setEnabled(false);

        useAuthentication.setText("This resource requires me to authenticate with it:");
        useAuthentication.setOpaque(false);

        saveCredentials.setText("Remember my credentials");
        saveCredentials.setOpaque(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(useAuthentication)
                            .addComponent(connectionLabel))
                        .addGap(0, 221, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passLabel)
                            .addComponent(userLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(saveCredentials)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(username)
                            .addComponent(password))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionLabel)
                .addGap(18, 18, 18)
                .addComponent(useAuthentication)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passLabel)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveCredentials)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional connection options:"));

        useStudies.setText("Filter available data by study (BrAPI /studies-search call)");

        useMaps.setText("Retrieve available maps (and marker positions) data (BrAPI /maps call)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(useStudies)
                    .addComponent(useMaps))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(useStudies)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useMaps)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        specLabel.setText("Note that Flapjack will only work with resources targetting V1.1 of the BrAPI specification.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(specLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(specLabel)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel connectionLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox saveCredentials;
    private javax.swing.JLabel specLabel;
    private javax.swing.JCheckBox useAuthentication;
    private javax.swing.JCheckBox useMaps;
    private javax.swing.JCheckBox useStudies;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
