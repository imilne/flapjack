// Copyright 2009-2019 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.mabc;

import jhi.flapjack.data.*;
import jhi.flapjack.data.results.*;
import jhi.flapjack.gui.*;
import jhi.flapjack.gui.table.*;

import scri.commons.gui.*;

import java.util.*;

public class MabcTableModel extends LineDataTableModel
{
	private int chrCount, qtlCount;

	// Indices to track what goes where
	private int rppIndex, rppTotalIndex, rppCoverageIndex;
	private int qtlIndex, qtlStatusIndex;
	private int selectedIndex, rankIndex, commentIndex, sortIndex;
	private int dataCountIndex;
	private int percData;
	private int hetCountIndex;
	private int hetPercIndex;
	private int decisionIndex;

	private FilterColumn filterColumn;

	public MabcTableModel(GTViewSet viewSet)
	{
		this.dataSet = viewSet.getDataSet();

		setLines(viewSet.tableHandler().linesForTable());
		initModel();
	}

	void initModel()
	{
		// Use information from the first result to determine the UI
		LineInfo line = lines.get(0);
		MabcResult s = line.getLineResults().getMabcResult();
		chrCount = s.getChrScores().size();
		qtlCount = s.getQtlScores().size();


		// New column indices
		dataCountIndex = 1;
		percData = 2;
		hetCountIndex = 3;
		hetPercIndex = 4;
		rppIndex = 5;
		rppTotalIndex = rppIndex + chrCount;
		rppCoverageIndex = rppTotalIndex + 1;
		qtlIndex = rppCoverageIndex + 1;
		qtlStatusIndex = qtlCount > 0 ? qtlIndex + (qtlCount*2) : -1;
		rankIndex = qtlCount > 0 ? qtlStatusIndex + 1 : qtlIndex;
		commentIndex = rankIndex + 1;
		sortIndex = commentIndex +1;
		selectedIndex = sortIndex +1;
		decisionIndex = selectedIndex +1;

		int colCount = decisionIndex + 1;
		columnNames = new String[colCount];
		ttNames = new String[colCount];

		// LineInfo column
		columnNames[0] = RB.getString("gui.mabc.MabcTableModel.line");

		columnNames[dataCountIndex] =  RB.getString("gui.mabc.MabcTableModel.dataCount");
		columnNames[percData] = RB.getString("gui.mabc.MabcTableModel.percData");
		columnNames[hetCountIndex] = RB.getString("gui.mabc.MabcTableModel.hetCount");
		columnNames[hetPercIndex] = RB.getString("gui.mabc.MabcTableModel.percHet");

		// For each chromosome's RPP result:
		for (int i = 0; i < s.getChrScores().size(); i++)
		{
			MabcChrScore cs = s.getChrScores().get(i);
			columnNames[rppIndex + i] = RB.format("gui.mabc.MabcTableModel.rpp", cs.view.getChromosomeMap().getName());
			ttNames[rppIndex + i] = RB.format("gui.mabc.MabcTableModel.rpp.tooltip", cs.view.getChromosomeMap().getName());
		}

		columnNames[rppTotalIndex] = RB.getString("gui.mabc.MabcTableModel.rppTotal");
		columnNames[rppCoverageIndex] = RB.getString("gui.mabc.MabcTableModel.rppCoverage");

		// QTL section of the table
		int qtl = 0;
		for (MabcQtlScore score: s.getQtlScores())
		{
			columnNames[qtlIndex+(qtl*2)] = RB.format("gui.mabc.MabcTableModel.ld", score.qtl.getQTL().getName());
			ttNames[qtlIndex+(qtl*2)] = RB.format("gui.mabc.MabcTableModel.ld.tooltip", score.qtl.getQTL().getName());
			columnNames[qtlIndex+(qtl*2)+1] = RB.format("gui.mabc.MabcTableModel.qtlStatus", score.qtl.getQTL().getName());
			qtl++;
		}

		if (qtlStatusIndex != -1)
			columnNames[qtlStatusIndex] = RB.getString("gui.mabc.MabcTableModel.qtlAlleleCount");
		columnNames[selectedIndex] = RB.getString("gui.mabc.MabcTableModel.selected");
		columnNames[rankIndex] = RB.getString("gui.mabc.MabcTableModel.rank");
		columnNames[commentIndex] = RB.getString("gui.mabc.MabcTableModel.comments");
		columnNames[sortIndex] = RB.getString("gui.mabc.MabcTableModel.sortFilter");
		columnNames[decisionIndex] = RB.getString("gui.mabc.MabcTableModel.decision");

		for (int i = 0; i < columnNames.length; i++)
			if (ttNames[i] == null)
				ttNames[i] = columnNames[i];

		filterColumn = new MabcFilterColumn(decisionIndex, getObjectColumnClass(decisionIndex), columnNames[decisionIndex], MabcFilterColumn.SELECT);
	}

	@Override
	public boolean skipExport(int col)
	{
		// We don't want to export the don't sort column
		return col == sortIndex;
	}

	@Override
	public int getRowCount()
	{
		return lines.size();
	}

	@Override
	public Object getObjectAt(int row, int col)
	{
		LineInfo line = lines.get(row);
		MabcResult stats = line.getLineResults().getMabcResult();

		// Name, Selected and Sort can work without results
		if (col == 0)
			return line;
		else if (col == selectedIndex)
			return line.getSelected();
		else if (col == sortIndex)
			return line.getLineResults().isSortToTop();

		// For everything else, don't show entries if stats object null
		if (stats == null)
			return null;

		// RPP values
		if (col >= rppIndex && col < rppTotalIndex)
		{
			return stats.getChrScores().get(col-rppIndex).sumRP;
		}

		// RPP Total
		else if (col == rppTotalIndex)
			return stats.getRppTotal();

		// RPP Coverage
		else if (col == rppCoverageIndex)
			return stats.getGenomeCoverage();

		// QTL values
		else if (col >= qtlIndex && col < qtlStatusIndex)
		{
			col = col-qtlIndex;
			int qtl = col / 2;

			MabcQtlScore score = stats.getQtlScores().get(qtl);

			if (col % 2 == 0)
				return score.drag;
			else
				return score.status;
		}

		// Sum of QTL status (where status == 1)
		else if (col == qtlStatusIndex)
			return stats.getQtlStatusCount();

		else if (col == rankIndex)
			return line.getLineResults().getRank();

		else if (col == commentIndex)
		{
			String comment = line.getLineResults().getComments();
			return comment == null ? "" : comment;
		}

		else if (col == dataCountIndex)
			return stats.getDataCount();

		else if (col == percData)
			return stats.getPercentData();

		else if (col == hetCountIndex)
			return stats.getHeterozygousCount();

		else if (col == hetPercIndex)
			return stats.getPercentHeterozygous();

		else if (col == decisionIndex)
			return stats.calculateDecisionString();

		return null;
	}

	@Override
	public Class getObjectColumnClass(int col)
	{
		if (col == 0)
			return LineInfo.class;
		else if (col == commentIndex)
			return String.class;
		else if (col == selectedIndex || col == sortIndex)
			return Boolean.class;
		else if (col == rankIndex || col == dataCountIndex || col == hetCountIndex)
			return Integer.class;
		else
			return Double.class;
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return (col == selectedIndex ||
				col == rankIndex ||
				col == commentIndex ||
				col == sortIndex);
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		LineInfo line = (LineInfo) getObjectAt(row, 0);

		if (col == selectedIndex)
			selectLine(line, (boolean)value);

		else if (col == rankIndex)
			line.getLineResults().setRank((int)value);

		else if (col == commentIndex)
			line.getLineResults().setComments((String)value);

		else if (col == sortIndex)
			line.getLineResults().setSortToTop((boolean)value);

		fireTableRowsUpdated(row, row);

		Actions.projectModified();
	}

	int selectQTL(int number)
	{
		int selectedCount = 0;
		for (int row=0; row < getRowCount(); row++)
		{
			if ((int) getValueAt(row, qtlStatusIndex) >= number)
			{
				setValueAt(true, row, selectedIndex);
				selectedCount++;
			}
			else
				setValueAt(false, row, selectedIndex);

			fireTableRowsUpdated(row, row);
		}

		return selectedCount;
	}

	int getRankIndex()
	{
		return rankIndex;
	}

	// Returns only those columns that make sense for filtering (by numbers)
	public FilterColumn[] getFilterableColumns()
	{
		ArrayList<FilterColumn> cols = new ArrayList<>();

		for (int i = 0; i < getColumnCount(); i++)
		{
			Class c = getObjectColumnClass(i);

			if (i == decisionIndex)
				cols.add(new FilterColumn(i, c, columnNames[i], FilterColumn.NONE));

			else if (c == Double.class || c == Float.class || c == Integer.class || c == Boolean.class)
				cols.add(new FilterColumn(i, c, columnNames[i], FilterColumn.NONE));
		}

		return cols.toArray(new FilterColumn[] {});
	}


	// Returns only those columns that make sense for filtering (by numbers)
	public FilterColumn[] getFilterColsMabcSelected()
	{
		ArrayList<FilterColumn> cols = new ArrayList<>();

		for (int i = 0; i < getColumnCount(); i++)
		{
			Class c = getObjectColumnClass(i);

			if (i == decisionIndex)
				cols.add(new MabcFilterColumn(i, c, columnNames[i], MabcFilterColumn.SELECT));

			else if (c == Double.class || c == Float.class || c == Integer.class || c == Boolean.class)
				cols.add(new FilterColumn(i, c, columnNames[i], FilterColumn.NONE));
		}

		return cols.toArray(new FilterColumn[] {});
	}

	public FilterColumn getFilterColumn()
	{
		return filterColumn;
	}
}