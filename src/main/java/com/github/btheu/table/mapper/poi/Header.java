package com.github.btheu.table.mapper.poi;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.github.btheu.table.mapper.internal.Columns;
import com.github.btheu.table.mapper.internal.Columns.Entry;
import com.github.btheu.table.mapper.poi.Header.HeaderCell;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author btheu
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Header extends ArrayList<HeaderCell> {

    private static final long serialVersionUID = 5677851148798609450L;

    /**
     * The POI header row of the header
     */
    protected Row headerRow;

    public Header(Cell firstLeftCell, Columns columns) {

        headerRow = firstLeftCell.getRow();

        for (Entry entry : columns.getColumns()) {

            HeaderCell hc = findHeaderCell(entry, firstLeftCell);

            this.add(hc);
        }
    }

    private HeaderCell findHeaderCell(Entry entry, Cell firstLeftCell) {
        Row row = firstLeftCell.getRow();
        int columnIndex = firstLeftCell.getColumnIndex();

        for (int i = columnIndex; i < columnIndex + 10000; i++) {

            Cell headerCell = row.getCell(i);

            if (entry.match(headerCell.getStringCellValue())) {
                return new HeaderCell(headerCell, entry);
            }
        }

        return null;
    }

    @Data
    public static class HeaderCell {

        /**
         * The data column
         */
        protected Entry entry;

        /**
         * The POI header cell for the column
         */
        protected Cell headerCell;

        public HeaderCell(Cell headerCell2, Entry entry2) {
            headerCell = headerCell2;
            entry = entry2;
        }
    }

}
