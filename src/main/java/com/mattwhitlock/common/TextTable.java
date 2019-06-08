/*
 * Created on Apr 25, 2019
 */
package com.mattwhitlock.common;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Matt Whitlock
 */
public class TextTable {

	public static enum Alignment {
		LEADING, CENTERED, TRAILING
	}

	public static final class Column {

		short minWidth = 0, maxWidth = Short.MAX_VALUE;
		byte leftPadding = 0, rightPadding = 1;
		int leftBorder = '\0', rightBorder = '\0';
		Alignment horizontalAlignment = Alignment.LEADING;
		int alignTo = '\0';

		Column() {
		}

		public short getMinimumWidth() {
			return minWidth;
		}

		public void setMinimumWidth(short minWidth) {
			ArgUtil.checkRangeInclusive(ArgUtil.checkNonNegative(minWidth, "minWidth"), maxWidth, "minWidth", "maxWidth");
			this.minWidth = minWidth;
		}

		public short getMaximumWidth() {
			return maxWidth;
		}

		public void setMaximumWidth(short maxWidth) {
			ArgUtil.checkRangeInclusive(minWidth, ArgUtil.checkNonNegative(maxWidth, "maxWidth"), "minWidth", "maxWidth");
			this.maxWidth = maxWidth;
		}

		public byte getLeftPadding() {
			return leftPadding;
		}

		public void setLeftPadding(byte leftPadding) {
			this.leftPadding = ArgUtil.checkNonNegative(leftPadding, "leftPadding");
		}

		public byte getRightPadding() {
			return rightPadding;
		}

		public void setRightPadding(byte rightPadding) {
			this.rightPadding = ArgUtil.checkNonNegative(rightPadding, "rightPadding");
		}

		public int getLeftBorder() {
			return leftBorder;
		}

		public void setLeftBorder(int leftBorder) {
			if (!Character.isValidCodePoint(leftBorder)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.leftBorder = leftBorder;
		}

		public int getRightBorder() {
			return rightBorder;
		}

		public void setRightBorder(int rightBorder) {
			if (!Character.isValidCodePoint(rightBorder)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.rightBorder = rightBorder;
		}

		public Alignment getHorizontalAlignment() {
			return horizontalAlignment;
		}

		public void setHorizontalAlignment(Alignment horizontalAlignment) {
			this.horizontalAlignment = Objects.requireNonNull(horizontalAlignment, "horizontalAlignment");
		}

		public int getAlignTo() {
			return alignTo;
		}

		public void setAlignTo(int alignTo) {
			if (!Character.isValidCodePoint(alignTo)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.alignTo = alignTo;
		}

	}

	public static final class Row {

		int topBorder = '\0', topBorderVertex = '\0', bottomBorder = '\0', bottomBorderVertex = '\0';
		Alignment horizontalAlignmentOverride = null;

		CharSequence[] cells;

		Row(CharSequence[] cells) {
			this.cells = cells;
		}

		public int getTopBorder() {
			return topBorder;
		}

		public void setTopBorder(int topBorder) {
			if (!Character.isValidCodePoint(topBorder)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.topBorder = topBorder;
		}

		public int getTopBorderVertex() {
			return topBorderVertex;
		}

		public void setTopBorderVertex(int topBorderVertex) {
			if (!Character.isValidCodePoint(topBorderVertex)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.topBorderVertex = topBorderVertex;
		}

		public void setTopBorder(int topBorder, int topBorderVertex) {
			if (!Character.isValidCodePoint(topBorder) || !Character.isValidCodePoint(topBorderVertex)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.topBorder = topBorder;
			this.topBorderVertex = topBorderVertex;
		}

		public int getBottomBorder() {
			return bottomBorder;
		}

		public void setBottomBorder(int bottomBorder) {
			if (!Character.isValidCodePoint(bottomBorder)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.bottomBorder = bottomBorder;
		}

		public int getBottomBorderVertex() {
			return bottomBorderVertex;
		}

		public void setBottomBorderVertex(int bottomBorderVertex) {
			if (!Character.isValidCodePoint(bottomBorderVertex)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.bottomBorderVertex = bottomBorderVertex;
		}

		public void setBottomBorder(int bottomBorder, int bottomBorderVertex) {
			if (!Character.isValidCodePoint(bottomBorder) || !Character.isValidCodePoint(bottomBorderVertex)) {
				throw new IllegalArgumentException("not a valid code point");
			}
			this.bottomBorder = bottomBorder;
			this.bottomBorderVertex = bottomBorderVertex;
		}

		public Alignment getHorizontalAlignmentOverride() {
			return horizontalAlignmentOverride;
		}

		public void setHorizontalAlignmentOverride(Alignment horizontalAlignmentOverride) {
			this.horizontalAlignmentOverride = horizontalAlignmentOverride;
		}

		public CharSequence[] getCells() {
			return cells;
		}

		public void setCells(CharSequence... cells) {
			if (cells.length != this.cells.length) {
				throw new IllegalArgumentException("wrong number of cells");
			}
			this.cells = cells;
		}

		public CharSequence getCell(int columnIdx) {
			return cells[columnIdx];
		}

		public void setCell(int columnIdx, CharSequence cell) {
			cells[columnIdx] = cell;
		}

	}

	short minWidth = 0, maxWidth = 132;

	private final ArrayList<Row> rows;

	private Column[] columns;

	public TextTable(int initialColumnCount, int initialRowCount) {
		ArgUtil.checkNonNegative(initialColumnCount, "initialColumnCount");
		ArgUtil.checkNonNegative(initialRowCount, "initialRowCount");
		ArrayList<Row> rows = this.rows = new ArrayList<>(initialRowCount);
		for (; initialRowCount > 0; --initialRowCount) {
			rows.add(new Row(new CharSequence[initialColumnCount]));
		}
		Column[] columns = this.columns = new Column[initialColumnCount];
		while (initialColumnCount > 0) {
			columns[--initialColumnCount] = new Column();
		}
	}

	public short getMinimumWidth() {
		return minWidth;
	}

	public void setMinimumWidth(short minWidth) {
		ArgUtil.checkRangeInclusive(ArgUtil.checkNonNegative(minWidth, "minWidth"), maxWidth, "minWidth", "maxWidth");
		this.minWidth = minWidth;
	}

	public short getMaximumWidth() {
		return maxWidth;
	}

	public void setMaximumWidth(short maxWidth) {
		ArgUtil.checkRangeInclusive(minWidth, ArgUtil.checkNonNegative(maxWidth, "maxWidth"), "minWidth", "maxWidth");
		this.maxWidth = maxWidth;
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Column getColumn(int columnIdx) {
		return columns[columnIdx];
	}

	public Column appendColumn() {
		Column[] oldColumns = columns, newColumns = columns = new Column[oldColumns.length + 1];
		System.arraycopy(oldColumns, 0, newColumns, 0, oldColumns.length);
		for (Row row : rows) {
			CharSequence[] oldCells = row.cells, newCells = row.cells = new CharSequence[oldCells.length + 1];
			System.arraycopy(oldCells, 0, newCells, 0, oldCells.length);
		}
		return newColumns[oldColumns.length] = new Column();
	}

	public Column[] appendColumns(int appendCount) {
		if (appendCount == 0) {
			return new Column[0];
		}
		ArgUtil.checkNonNegative(appendCount, "appendCount");
		Column[] oldColumns = columns, newColumns = columns = new Column[oldColumns.length + appendCount];
		System.arraycopy(oldColumns, 0, newColumns, 0, oldColumns.length);
		for (Row row : rows) {
			CharSequence[] oldCells = row.cells, newCells = row.cells = new CharSequence[oldCells.length + appendCount];
			System.arraycopy(oldCells, 0, newCells, 0, oldCells.length);
		}
		Column[] appendedColumns = new Column[appendCount];
		for (int columnIdx = newColumns.length; appendCount > 0;) {
			newColumns[--columnIdx] = appendedColumns[--appendCount] = new Column();
		}
		return appendedColumns;
	}

	public Column insertColumn(int columnIdx) {
		Column[] oldColumns = columns;
		if (columnIdx == oldColumns.length) {
			return appendColumn();
		}
		ArgUtil.checkInRange(columnIdx, 0, oldColumns.length, "columnIdx");
		Column[] newColumns = columns = new Column[oldColumns.length + 1];
		System.arraycopy(oldColumns, 0, newColumns, 0, columnIdx);
		System.arraycopy(oldColumns, columnIdx, newColumns, columnIdx + 1, oldColumns.length - columnIdx);
		for (Row row : rows) {
			CharSequence[] oldCells = row.cells, newCells = row.cells = new CharSequence[oldCells.length + 1];
			System.arraycopy(oldCells, 0, newCells, 0, columnIdx);
			System.arraycopy(oldCells, columnIdx, newCells, columnIdx + 1, oldCells.length - columnIdx);
		}
		return newColumns[columnIdx] = new Column();
	}

	public Column[] insertColumns(int columnIdx, int insertCount) {
		ArgUtil.checkNonNegative(insertCount, "insertCount");
		Column[] oldColumns = columns;
		if (columnIdx == oldColumns.length) {
			return appendColumns(insertCount);
		}
		ArgUtil.checkInRange(columnIdx, 0, oldColumns.length, "columnIdx");
		if (insertCount == 0) {
			return new Column[0];
		}
		Column[] newColumns = columns = new Column[oldColumns.length + insertCount];
		System.arraycopy(oldColumns, 0, newColumns, 0, columnIdx);
		System.arraycopy(oldColumns, columnIdx, newColumns, columnIdx + insertCount, oldColumns.length - columnIdx);
		for (Row row : rows) {
			CharSequence[] oldCells = row.cells, newCells = row.cells = new CharSequence[oldCells.length + insertCount];
			System.arraycopy(oldCells, 0, newCells, 0, columnIdx);
			System.arraycopy(oldCells, columnIdx, newCells, columnIdx + insertCount, oldCells.length - columnIdx);
		}
		Column[] insertedColumns = new Column[insertCount];
		for (columnIdx += insertCount; insertCount > 0;) {
			newColumns[--columnIdx] = insertedColumns[--insertCount] = new Column();
		}
		return insertedColumns;
	}

	public void deleteColumn(int columnIdx) {
		Column[] oldColumns = columns;
		ArgUtil.checkInRange(columnIdx, 0, oldColumns.length - 1, "columnIdx");
		Column[] newColumns = columns = new Column[oldColumns.length - 1];
		System.arraycopy(oldColumns, 0, newColumns, 0, columnIdx);
		System.arraycopy(oldColumns, columnIdx + 1, newColumns, columnIdx, newColumns.length - columnIdx);
		for (Row row : rows) {
			CharSequence[] oldCells = row.cells, newCells = row.cells = new CharSequence[oldCells.length - 1];
			System.arraycopy(oldCells, 0, newCells, 0, columnIdx);
			System.arraycopy(oldCells, columnIdx + 1, newCells, columnIdx, newCells.length - columnIdx);
		}
	}

	public void deleteColumns(int columnIdx, int deleteCount) {
		Column[] oldColumns = columns;
		ArgUtil.checkInRange(columnIdx, 0, oldColumns.length - 1, "columnIdx");
		if (deleteCount == 0) {
			return;
		}
		ArgUtil.checkInRange(deleteCount, 0, oldColumns.length - columnIdx, "deleteCount");
		Column[] newColumns = columns = new Column[oldColumns.length - deleteCount];
		System.arraycopy(oldColumns, 0, newColumns, 0, columnIdx);
		System.arraycopy(oldColumns, columnIdx + deleteCount, newColumns, columnIdx, newColumns.length - columnIdx);
		for (Row row : rows) {
			CharSequence[] oldCells = row.cells, newCells = row.cells = new CharSequence[oldCells.length - deleteCount];
			System.arraycopy(oldCells, 0, newCells, 0, columnIdx);
			System.arraycopy(oldCells, columnIdx + deleteCount, newCells, columnIdx, newCells.length - columnIdx);
		}
	}

	public int getRowCount() {
		return rows.size();
	}

	public Row getRow(int rowIdx) {
		return rows.get(rowIdx);
	}

	public Row appendRow() {
		Row row = new Row(new CharSequence[columns.length]);
		rows.add(row);
		return row;
	}

	public Row appendRow(CharSequence... cells) {
		if (cells == null) {
			return appendRow();
		}
		if (cells.length != columns.length) {
			throw new IllegalArgumentException("wrong number of cells");
		}
		Row row = new Row(cells);
		rows.add(row);
		return row;
	}

	public Row[] appendRows(int appendCount) {
		if (appendCount == 0) {
			return new Row[0];
		}
		ArgUtil.checkNonNegative(appendCount, "appendCount");
		Row[] appendedRows = new Row[appendCount];
		for (int columnCount = columns.length; appendCount > 0;) {
			appendedRows[--appendCount] = new Row(new CharSequence[columnCount]);
		}
		rows.addAll(Arrays.asList(appendedRows));
		return appendedRows;
	}

	public Row insertRow(int rowIdx) {
		ArgUtil.checkInRange(rowIdx, 0, rows.size(), "rowIdx");
		Row row = new Row(new CharSequence[columns.length]);
		rows.add(rowIdx, row);
		return row;
	}

	public Row insertRow(int rowIdx, CharSequence... cells) {
		if (cells == null) {
			return insertRow(rowIdx);
		}
		if (cells.length != columns.length) {
			throw new IllegalArgumentException("wrong number of cells");
		}
		ArgUtil.checkInRange(rowIdx, 0, rows.size(), "rowIdx");
		Row row = new Row(new CharSequence[columns.length]);
		rows.add(rowIdx, row);
		return row;
	}

	public Row[] insertRows(int rowIdx, int insertCount) {
		ArgUtil.checkInRange(rowIdx, 0, rows.size(), "rowIdx");
		if (insertCount == 0) {
			return new Row[0];
		}
		ArgUtil.checkNonNegative(insertCount, "insertCount");
		Row[] insertedRows = new Row[insertCount];
		for (int columnCount = columns.length; insertCount > 0;) {
			insertedRows[--insertCount] = new Row(new CharSequence[columnCount]);
		}
		rows.addAll(rowIdx, Arrays.asList(insertedRows));
		return insertedRows;
	}

	public void deleteRow(int rowIdx) {
		ArgUtil.checkInRange(rowIdx, 0, rows.size() - 1, "rowIdx");
		rows.remove(rowIdx);
	}

	public void deleteRows(int rowIdx, int deleteCount) {
		if (deleteCount == 1) {
			deleteRow(rowIdx);
			return;
		}
		ArgUtil.checkInRange(rowIdx, 0, rows.size() - 1, "rowIdx");
		if (deleteCount == 0) {
			return;
		}
		ArgUtil.checkInRange(deleteCount, 0, rows.size() - rowIdx, "deleteCount");
		rows.subList(rowIdx, rowIdx + deleteCount).clear();
	}

	public final void setAsciiBorders() {
		setBorders('-', '-', '-', '|', '|', '|', '+');
	}

	public void setBorders(int topBorder, int horizBorder, int bottomBorder, int leftBorder, int vertBorder, int rightBorder, int vertex) {
		byte leftPadding, innerPadding, rightPadding;
		if (topBorder != '\0' || horizBorder != '\0' || bottomBorder != '\0') {
			leftPadding = innerPadding = rightPadding = 1;
		}
		else {
			leftPadding = leftBorder == '\0' ? 0 : (byte) 1;
			innerPadding = vertBorder == '\0' ? 0 : (byte) 1;
			rightPadding = rightBorder == '\0' ? 0 : (byte) 1;
		}
		{
			Column[] columns = this.columns;
			int columnIdx = columns.length;
			if (columnIdx > 0) {
				Column column = columns[--columnIdx];
				column.rightBorder = rightBorder;
				column.rightPadding = rightPadding;
				while (columnIdx > 0) {
					column.leftBorder = vertBorder;
					column.leftPadding = innerPadding;
					column = columns[--columnIdx];
					column.rightPadding = innerPadding;
				}
				column.leftBorder = leftBorder;
				column.leftPadding = leftPadding;
			}
		}
		{
			ArrayList<Row> rows = this.rows;
			int rowIdx = rows.size();
			if (rowIdx > 0) {
				Row row = rows.get(--rowIdx);
				row.bottomBorder = bottomBorder;
				row.bottomBorderVertex = vertex;
				while (rowIdx > 0) {
					row.topBorder = horizBorder;
					row.topBorderVertex = vertex;
					row = rows.get(--rowIdx);
				}
				row.topBorder = topBorder;
				row.topBorderVertex = vertex;
			}
		}
	}

	private static class ColumnRenderState {

		final CharSequenceIterator csi = new CharSequenceIterator();

		int contentWidth, leadingWidth, trailingWidth;
		int width, slack;

		void resetMetrics() {
			contentWidth = leadingWidth = trailingWidth = 0;
		}

		void augmentMinimumMetrics(BreakIterator itr, CharSequence cs, int alignTo) {
			CharSequenceIterator csi = this.csi;
			itr.setText(csi.reset(cs));
			for (int beginIdx = itr.current(), nextIdx; (nextIdx = itr.next()) != BreakIterator.DONE; beginIdx = nextIdx) {
				int endIdx = rtrim(cs, beginIdx = ltrim(cs, beginIdx, nextIdx), nextIdx);
				int contentWidth = Character.codePointCount(cs, beginIdx, endIdx);
				if (contentWidth > this.contentWidth) {
					this.contentWidth = contentWidth;
				}
				augmentAlignment(cs, beginIdx, endIdx, alignTo);
			}
		}

		void augmentActualMetrics(BreakIterator itr, CharSequence cs, int alignTo) {
			CharSequenceIterator csi = this.csi;
			itr.setText(csi.reset(cs));
			for (int endIdx = csi.endIdx; csi.idx < endIdx;) {
				int contentWidth = findBreak(itr);
				if (contentWidth > this.contentWidth) {
					this.contentWidth = contentWidth;
				}
				augmentAlignment(cs, csi.beginIdx, csi.endIdx, alignTo);
				csi.beginIdx = ltrim(cs, csi.idx, csi.endIdx = endIdx);
			}
		}

		private void augmentAlignment(CharSequence cs, int beginIdx, int endIdx, int alignTo) {
			int alignIdx;
			if (alignTo != '\0' && (alignIdx = lastIndexOf(cs, beginIdx, endIdx, alignTo)) >= 0) {
				int leadingWidth = Character.codePointCount(cs, beginIdx, alignIdx);
				if (leadingWidth > this.leadingWidth) {
					this.leadingWidth = leadingWidth;
				}
				int trailingWidth = contentWidth - leadingWidth;
				assert trailingWidth == Character.codePointCount(cs, alignIdx, endIdx);
				if (trailingWidth > this.trailingWidth) {
					this.trailingWidth = trailingWidth;
				}
			}
		}

		boolean wrap(char[] dst, int dstOff, BreakIterator itr, Alignment alignment, int alignTo) {
			CharSequenceIterator csi = this.csi;
			CharSequence cs = csi.cs;
			int origEndIdx = csi.endIdx, width = this.width, contentWidth = findBreak(itr), beginIdx = csi.beginIdx, endIdx = csi.endIdx;
			csi.beginIdx = ltrim(cs, csi.idx, csi.endIdx = origEndIdx);
			int leftPadding = 0, rightPadding = 0, alignIdx;
			if (trailingWidth <= 0 || (alignIdx = lastIndexOf(cs, beginIdx, endIdx, alignTo)) < 0) {
				switch (alignment) {
					case LEADING:
						rightPadding = width - contentWidth;
						break;
					case CENTERED:
						leftPadding = (width -= contentWidth) / 2;
						rightPadding = width - leftPadding;
						break;
					case TRAILING:
						leftPadding = width - contentWidth;
						break;
				}
			}
			else {
				int trailingWidth = Character.codePointCount(cs, alignIdx, endIdx), leadingWidth = contentWidth - trailingWidth;
				assert leadingWidth == Character.codePointCount(cs, beginIdx, alignIdx);
				switch (alignment) {
					case LEADING:
						leftPadding = this.leadingWidth - leadingWidth;
						rightPadding = width - leftPadding - contentWidth;
						break;
					case CENTERED:
						leftPadding = (width + this.leadingWidth - this.trailingWidth) / 2 - leadingWidth;
						rightPadding = width - leftPadding - contentWidth;
						break;
					case TRAILING:
						rightPadding = this.trailingWidth - trailingWidth;
						leftPadding = width - rightPadding - contentWidth;
						break;
				}
			}
			Arrays.fill(dst, dstOff, dstOff += leftPadding, ' ');
			for (int idx = beginIdx; idx < endIdx; ++idx) {
				dst[dstOff++] = cs.charAt(idx);
			}
			Arrays.fill(dst, dstOff, dstOff += rightPadding, ' ');
			return csi.idx < origEndIdx;
		}

		/**
		 * Finds the position of the break that maximizes the visible content that can fit into the specified width.
		 * Upon return, {@code csi.beginIdx} and {@code csi.endIdx} tightly span the content, and {@code csi.idx} points
		 * just past the break.
		 * 
		 * @return the width of the content (in code points).
		 */
		private int findBreak(BreakIterator itr) {
			CharSequenceIterator csi = this.csi;
			CharSequence cs = csi.cs;
			int endIdx = csi.endIdx, beginIdx = csi.beginIdx = ltrim(cs, csi.beginIdx, endIdx), idx = beginIdx, width = this.width, contentWidth = 0;
			while (idx < endIdx) {
				int cp = Character.codePointAt(cs, idx);
				if (cp == '\n') {
					csi.idx = idx + 1;
					return contentWidth - Character.codePointCount(cs, csi.endIdx = rtrim(cs, beginIdx, idx), idx);
				}
				idx += Character.charCount(cp);
				if (++contentWidth >= width) {
					break;
				}
			}
			itr.setText(csi);
			if ((endIdx = itr.following(idx)) == BreakIterator.DONE) {
				endIdx = csi.endIdx;
			}
			contentWidth += Character.codePointCount(cs, idx, idx = endIdx = rtrim(cs, idx, endIdx));
			if (contentWidth > width && (endIdx = itr.previous()) <= 0) {
				do {
					idx -= Character.charCount(Character.codePointBefore(cs, idx));
				} while (--contentWidth > width);
				csi.idx = endIdx = idx;
			}
			return contentWidth - Character.codePointCount(cs, csi.endIdx = rtrim(cs, beginIdx, endIdx), idx);
		}

	}

	public final CharSequence render() {
		return renderTo(new StringBuilder(0));
	}

	public StringBuilder renderTo(StringBuilder sb) {
		Column[] columns = this.columns;
		int sumMinWidth = 0, sumMaxWidth = 0, sumPadding = 0;
		for (Column column : columns) {
			sumMinWidth += column.minWidth;
			sumMaxWidth += column.maxWidth;
			sumPadding += (column.leftBorder == '\0' ? 0 : 1) + column.leftPadding + column.rightPadding + (column.rightBorder == '\0' ? 0 : 1);
		}
		int minWidth = Math.max(this.minWidth - sumPadding, 0);
		if (minWidth > sumMaxWidth) {
			throw new IllegalStateException("table minimum width must not exceed " + (sumMaxWidth + sumPadding));
		}
		int maxWidth = this.maxWidth - sumPadding;
		if (maxWidth < sumMinWidth) {
			throw new IllegalStateException("table maximum width must not be less than " + (sumMinWidth + sumPadding));
		}

		ArrayList<Row> rows = this.rows;
		ColumnRenderState[] states = new ColumnRenderState[columns.length];
		for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
			states[columnIdx] = new ColumnRenderState();
		}

		BreakIterator itr = BreakIterator.getLineInstance();
		for (Row row : rows) {
			CharSequence[] cells = row.cells;
			for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
				states[columnIdx].augmentMinimumMetrics(itr, cells[columnIdx], columns[columnIdx].alignTo);
			}
		}
		int sumWidth = 0, sumSlack = 0;
		for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
			ColumnRenderState state = states[columnIdx];
			Column column = columns[columnIdx];
			int columnWidth = Math.min(Math.max(Math.max(state.contentWidth, state.leadingWidth + state.trailingWidth), column.minWidth), column.maxWidth);
			sumSlack += state.slack = columnWidth - column.minWidth;
			sumWidth += state.width = columnWidth;
		}

		if (sumWidth < maxWidth) {
			for (Row row : rows) {
				CharSequence[] cells = row.cells;
				for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
					ColumnRenderState state = states[columnIdx];
					CharSequence cell = cells[columnIdx];
					int maxContentWidth = findMaxContentWidth(cell, 0, cell.length());
					if (maxContentWidth > state.contentWidth) {
						state.contentWidth = maxContentWidth;
					}
				}
			}
			sumWidth = sumSlack = 0;
			for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
				ColumnRenderState state = states[columnIdx];
				Column column = columns[columnIdx];
				int columnWidth = Math.min(Math.max(Math.max(state.contentWidth, state.leadingWidth + state.trailingWidth), column.minWidth), column.maxWidth);
				sumSlack += state.slack = columnWidth - state.width;
				sumWidth += state.width = columnWidth;
			}
		}

		int delta;
		if ((delta = sumWidth - maxWidth) > 0 || (delta = sumWidth - minWidth) < 0) {
			if (delta < 0) {
				sumSlack = 0;
				for (ColumnRenderState state : states) {
					sumSlack += state.slack = state.contentWidth;
				}
			}
			sumWidth = 0;
			for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
				ColumnRenderState state = states[columnIdx];
				if (state.slack > 0) {
					Column column = columns[columnIdx];
					int columnWidth = Math.min(Math.max(state.width - delta * state.slack / sumSlack, column.minWidth), column.maxWidth);
					delta += columnWidth - state.width;
					state.width = columnWidth;
					sumSlack -= state.slack;
				}
				sumWidth += state.width;
			}
		}

		for (ColumnRenderState state : states) {
			state.resetMetrics();
		}
		for (Row row : rows) {
			CharSequence[] cells = row.cells;
			for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
				states[columnIdx].augmentActualMetrics(itr, cells[columnIdx], columns[columnIdx].alignTo);
			}
		}

		char[] line = new char[sumWidth + sumPadding + 1];
		sb.ensureCapacity(sb.length() + line.length * rows.size());
		for (Row row : rows) {
			int pos = 0, border, vertex;
			if ((border = row.topBorder) != '\0') {
				if ((vertex = row.topBorderVertex) == '\0') {
					vertex = border;
				}
				for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
					ColumnRenderState state = states[columnIdx];
					Column column = columns[columnIdx];
					if (column.leftBorder != '\0') {
						pos += Character.toChars(vertex, line, pos);
					}
					for (int count = column.leftPadding + state.width + column.rightPadding; count > 0; --count) {
						pos += Character.toChars(border, line, pos);
					}
					if (column.rightBorder != '\0') {
						pos += Character.toChars(vertex, line, pos);
					}
				}
				line[pos++] = '\n';
				sb.append(line, 0, pos);
				pos = 0;
			}
			CharSequence[] cells = row.cells;
			for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
				states[columnIdx].csi.reset(cells[columnIdx]);
			}
			for (boolean more = true; more;) {
				more = false;
				for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
					ColumnRenderState state = states[columnIdx];
					Column column = columns[columnIdx];
					if (column.leftBorder != '\0') {
						pos += Character.toChars(column.leftBorder, line, pos);
					}
					Arrays.fill(line, pos, pos += column.leftPadding, ' ');
					more |= state.wrap(line, pos, itr, row.horizontalAlignmentOverride == null ? column.horizontalAlignment : row.horizontalAlignmentOverride, column.alignTo);
					pos += state.width;
					Arrays.fill(line, pos, pos += column.rightPadding, ' ');
					if (column.rightBorder != '\0') {
						pos += Character.toChars(column.rightBorder, line, pos);
					}
				}
				line[pos++] = '\n';
				sb.append(line, 0, pos);
				pos = 0;
			}
			if ((border = row.bottomBorder) != '\0') {
				if ((vertex = row.bottomBorderVertex) == '\0') {
					vertex = border;
				}
				for (int columnIdx = 0; columnIdx < states.length; ++columnIdx) {
					ColumnRenderState state = states[columnIdx];
					Column column = columns[columnIdx];
					if (column.leftBorder != '\0') {
						pos += Character.toChars(vertex, line, pos);
					}
					for (int count = column.leftPadding + state.width + column.rightPadding; count > 0; --count) {
						pos += Character.toChars(border, line, pos);
					}
					if (column.rightBorder != '\0') {
						pos += Character.toChars(vertex, line, pos);
					}
				}
				line[pos++] = '\n';
				sb.append(line, 0, pos);
			}
		}
		return sb;
	}

	/**
	 * Returns the width (in code points) of the longest physical line (delineated by {@code '\n'}) in the given
	 * {@link CharSequence}. The search excludes any leading and/or trailing {@linkplain Character#isWhitespace(int)
	 * whitespace} on each line.
	 */
	private static int findMaxContentWidth(CharSequence cs, int beginIdx, int endIdx) {
		int maxContentWidth = 0;
		endIdx = rtrim(cs, beginIdx = ltrim(cs, beginIdx, endIdx), endIdx);
		for (int idx = beginIdx; idx < endIdx; idx = beginIdx = ltrim(cs, idx, endIdx)) {
			int contentWidth = 0;
			do {
				++contentWidth;
				int cp = Character.codePointAt(cs, idx);
				if (cp == '\n') {
					++idx;
					break;
				}
				idx += Character.charCount(cp);
			} while (idx < endIdx);
			contentWidth -= Character.codePointCount(cs, rtrim(cs, beginIdx, idx), idx);
			if (contentWidth > maxContentWidth) {
				maxContentWidth = contentWidth;
			}
		}
		return maxContentWidth;
	}

	/**
	 * Returns {@code beginIdx}, adjusted toward {@code endIdx} to eliminate any leading
	 * {@linkplain Character#isWhitespace(int) whitespace}.
	 */
	private static int ltrim(CharSequence cs, int beginIdx, int endIdx) {
		while (beginIdx < endIdx) {
			int cp = Character.codePointAt(cs, beginIdx);
			if (!Character.isWhitespace(cp)) {
				break;
			}
			beginIdx += Character.charCount(cp);
		}
		return beginIdx;
	}

	/**
	 * Returns {@code endIdx}, adjusted toward {@code beginIdx} to eliminate any trailing
	 * {@linkplain Character#isWhitespace(int) whitespace}.
	 */
	private static int rtrim(CharSequence cs, int beginIdx, int endIdx) {
		while (endIdx > beginIdx) {
			int cp = Character.codePointBefore(cs, endIdx);
			if (!Character.isWhitespace(cp)) {
				break;
			}
			endIdx -= Character.charCount(cp);
		}
		return endIdx;
	}

	/**
	 * Returns the last index of the specified code point in the specified span of the given {@link CharSequence}, or a
	 * negative number if not found.
	 */
	private static int lastIndexOf(CharSequence cs, int beginIdx, int endIdx, int cp) {
		while (endIdx > beginIdx) {
			int cp1 = Character.codePointBefore(cs, endIdx);
			if (cp1 == cp) {
				return endIdx;
			}
			endIdx -= Character.charCount(cp1);
		}
		return -1;
	}

}

class CharSequenceIterator implements CharacterIterator {

	CharSequence cs;
	int beginIdx, endIdx, idx;

	CharSequenceIterator() {
	}

	public CharSequenceIterator(CharSequence cs) {
		reset(cs);
	}

	public CharSequenceIterator(CharSequence cs, int beginIdx, int endIdx) {
		reset(cs, beginIdx, endIdx);
	}

	public CharSequenceIterator reset(CharSequence cs) {
		Objects.requireNonNull(cs, "cs");
		this.cs = cs;
		beginIdx = idx = 0;
		endIdx = cs.length();
		return this;
	}

	public CharSequenceIterator reset(CharSequence cs, int beginIdx, int endIdx) {
		Objects.requireNonNull(cs, "cs");
		ArgUtil.checkRangeInclusive(ArgUtil.checkNonNegative(beginIdx, "beginIdx"), ArgUtil.checkAtMost(endIdx, cs.length(), "endIdx"), "beginIdx", "endIdx");
		this.cs = cs;
		this.beginIdx = idx = beginIdx;
		this.endIdx = endIdx;
		return this;
	}

	@Override
	public char first() {
		return (idx = beginIdx) == endIdx ? DONE : cs.charAt(idx);
	}

	@Override
	public char last() {
		return (idx = endIdx) == beginIdx ? DONE : cs.charAt(--idx);
	}

	@Override
	public char current() {
		return idx >= endIdx ? DONE : cs.charAt(idx);
	}

	@Override
	public char next() {
		return idx >= endIdx ? DONE : cs.charAt(++idx);
	}

	@Override
	public char previous() {
		return idx <= beginIdx ? DONE : cs.charAt(--idx);
	}

	@Override
	public char setIndex(int position) {
		ArgUtil.checkInRange(position, beginIdx, endIdx, "position");
		return (idx = position) == endIdx ? DONE : cs.charAt(idx);
	}

	@Override
	public int getBeginIndex() {
		return beginIdx;
	}

	@Override
	public int getEndIndex() {
		return endIdx;
	}

	@Override
	public int getIndex() {
		return idx;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException impossible) {
			throw new InternalError(impossible);
		}
	}

}
