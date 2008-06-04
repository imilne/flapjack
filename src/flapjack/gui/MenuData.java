package flapjack.gui;

import flapjack.data.*;
import flapjack.gui.dialog.*;
import flapjack.gui.dialog.analysis.*;
import flapjack.gui.navpanel.*;
import flapjack.gui.visualization.*;

import scri.commons.gui.*;

class MenuData
{
	private WinMain winMain;
	private NavPanel navPanel;
	private GenotypePanel gPanel;

	void setComponents(WinMain winMain, NavPanel navPanel)
	{
		this.winMain = winMain;
		this.navPanel = navPanel;

		gPanel = navPanel.getGenotypePanel();
	}

	void dataSortLines(int sortMethod)
	{
		SortLinesDialog dialog = new SortLinesDialog(gPanel);

		if (dialog.isOK())
		{
			boolean[] chromosomes = dialog.getSelectedChromosomes();

			new SortingLinesProgressDialog(chromosomes).runSort(gPanel, sortMethod);
		}
	}

	void dataFind()
	{
		winMain.getFindDialog().setVisible(true);
		Prefs.guiFindDialogShown = true;
	}

	void dataStatistics()
	{
		GTViewSet viewSet = gPanel.getViewSet();

		StatisticsProgressDialog dialog = new StatisticsProgressDialog(viewSet);

		if (dialog.isOK())
			new AlleleStatisticsDialog(viewSet, dialog.getResults());
	}

	void dataRenameDataSet()
	{
		DataSet dataSet = navPanel.getDataSetForSelection();

		RenameDialog dialog = new RenameDialog(dataSet.getName());

		if (dialog.isOK())
		{
			dataSet.setName(dialog.getNewName());
			navPanel.updateNodeFor(dataSet);

			Actions.projectModified();
		}
	}

	void dataDeleteDataSet()
	{
		String msg = RB.getString("gui.WinMain.deleteDataSet");

		String[] options = new String[] {
			RB.getString("gui.WinMain.deleteDataSetButton"),
			RB.getString("gui.text.cancel") };

		if (TaskDialog.show(msg, MsgBox.QST, 1, options) == 0)
		{
			// Determine the selected data set...
			DataSet dataSet = navPanel.getDataSetForSelection();

			// ...and remove it from both the project and the GUI
			winMain.getProject().removeDataSet(dataSet);
			navPanel.removeDataSetNode(dataSet);

			Actions.projectModified();
		}
	}
}