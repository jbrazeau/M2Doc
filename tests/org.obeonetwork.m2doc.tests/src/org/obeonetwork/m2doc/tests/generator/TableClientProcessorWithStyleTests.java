/*******************************************************************************
 *  Copyright (c) 2016 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.m2doc.tests.generator;

import java.util.List;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.obeonetwork.m2doc.element.MStyle;
import org.obeonetwork.m2doc.element.MTable.MRow;
import org.obeonetwork.m2doc.generator.TableClientProcessor;
import org.obeonetwork.m2doc.provider.ProviderException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link TableClientProcessor} with tables that have styles.
 * 
 * @author ldelaigue
 */
// CHECKSTYLE:OFF
public class TableClientProcessorWithStyleTests extends TableClientProcessorTests {

    private static final String COL_BG_COLOR = "123456";
    private static final String COL_FG_COLOR = "654321";
    private static final String ROW_BG_COLOR = "99AABB";
    private static final String ROW_FG_COLOR = "FF00FF";
    private static final String CELL_BG_COLOR = "00FFAA";
    private static final String CELL_FG_COLOR = "EEFF99";

    private static final int COL_FONT_SIZE = 11;
    private static final int ROW_FONT_SIZE = 12;
    private static final int CELL_FONT_SIZE = 10;

    @Test
    @Override
    public void testWithoutAnyParameter() throws ProviderException {
        super.testWithoutAnyParameter();

        // Check styles
        List<XWPFTable> tables = doc.getTables();
        XWPFTable table = tables.get(0);

        // First Row (header)
        XWPFTableRow row = table.getRow(0);

        // Header => col 1 has "column style"
        XWPFTableCell cell = row.getCell(1);
        XWPFParagraph cellParagraph = cell.getParagraphs().get(0);
        XWPFRun cellRun = cellParagraph.getRuns().get(0);
        assertEquals(COL_BG_COLOR, cell.getColor().toUpperCase());
        assertEquals(COL_FG_COLOR, cellRun.getColor().toUpperCase());
        assertEquals(COL_FONT_SIZE, cellRun.getFontSize());
        assertTrue(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // Header => no style
        cell = row.getCell(2);
        cellParagraph = cell.getParagraphs().get(0);
        cellRun = cellParagraph.getRuns().get(0);
        assertNull(cell.getColor());
        assertNull(cellRun.getColor());
        assertFalse(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // First row
        row = table.getRow(1);

        // first row, row header => no style
        cell = row.getCell(0);
        cellParagraph = cell.getParagraphs().get(0);
        cellRun = cellParagraph.getRuns().get(0);
        assertNull(cell.getColor());
        assertNull(cellRun.getColor());
        assertFalse(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // first row, first cell => cell style
        cell = row.getCell(1);
        cellParagraph = cell.getParagraphs().get(0);
        cellRun = cellParagraph.getRuns().get(0);
        assertEquals(ROW_BG_COLOR, cell.getColor().toUpperCase());
        assertEquals(ROW_FG_COLOR, cellRun.getColor().toUpperCase());
        assertEquals(ROW_FONT_SIZE, cellRun.getFontSize());
        assertFalse(cellRun.isBold());
        assertTrue(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // first row, second cell => no style
        cell = row.getCell(2);
        cellParagraph = cell.getParagraphs().get(0);
        assertNull(cell.getColor());
        cellRun = cellParagraph.getRuns().get(0);
        assertFalse(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // Second row
        row = table.getRow(2);

        // second row, row header => no style
        cell = row.getCell(0);
        cellParagraph = cell.getParagraphs().get(0);
        cellRun = cellParagraph.getRuns().get(0);
        assertNull(cell.getColor());
        assertNull(cellRun.getColor());
        assertFalse(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // second row, first cell => no style
        cell = row.getCell(1);
        cellParagraph = cell.getParagraphs().get(0);
        assertNull(cell.getColor());
        cellRun = cellParagraph.getRuns().get(0);
        assertFalse(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.NONE, cellRun.getUnderline());

        // second row, second cell => cell style
        cell = row.getCell(2);
        cellParagraph = cell.getParagraphs().get(0);
        cellRun = cellParagraph.getRuns().get(0);
        assertEquals(CELL_BG_COLOR, cell.getColor().toUpperCase());
        assertEquals(CELL_FG_COLOR, cellRun.getColor().toUpperCase());
        assertEquals(CELL_FONT_SIZE, cellRun.getFontSize());
        assertTrue(cellRun.isBold());
        assertFalse(cellRun.isItalic());
        assertFalse(cellRun.isStrikeThrough());
        assertEquals(UnderlinePatterns.SINGLE, cellRun.getUnderline());
    }

    @Override
    public void testWithParamHideTitleFalse() throws ProviderException {
        super.testWithParamHideTitleFalse();
    }

    @Override
    public void testWithParamHideTitleTrue() throws ProviderException {
        super.testWithParamHideTitleTrue();
    }

    @Override
    protected TestTable getTestTable() {
        TestTable table = super.getTestTable();

        // Add styles
        List<MRow> rows = table.getRows();
        rows.get(0).getCells().get(1).setStyle(getColumnStyle());
        rows.get(1).getCells().get(1).setStyle(getRowStyle());
        rows.get(2).getCells().get(2).setStyle(getCellStyle());

        return table;
    }

    protected MStyle getColumnStyle() {
        TestStyle style = new TestStyle();
        style.setBackgroundColor(COL_BG_COLOR);
        style.setForegroundColor(COL_FG_COLOR);
        style.setFontSize(COL_FONT_SIZE);
        style.setFontModifiers(MStyle.FONT_BOLD);
        return style;
    }

    protected MStyle getRowStyle() {
        TestStyle style = new TestStyle();
        style.setBackgroundColor(ROW_BG_COLOR);
        style.setForegroundColor(ROW_FG_COLOR);
        style.setFontSize(ROW_FONT_SIZE);
        style.setFontModifiers(MStyle.FONT_ITALIC);
        return style;
    }

    protected MStyle getCellStyle() {
        TestStyle style = new TestStyle();
        style.setBackgroundColor(CELL_BG_COLOR);
        style.setForegroundColor(CELL_FG_COLOR);
        style.setFontSize(CELL_FONT_SIZE);
        style.setFontModifiers(MStyle.FONT_UNDERLINE | MStyle.FONT_BOLD);
        return style;
    }
}
// CHECKSTYLE:ON
