package com.github.btheu.table.mapper.poi;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.github.btheu.table.mapper.internal.TableData;
import com.github.btheu.table.mapper.internal.TableData.ColumnData;
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

    public Header(Cell firstLeftCell, TableData columns) {

        headerRow = firstLeftCell.getRow();

        for (ColumnData column : columns.getColumns()) {

            HeaderCell hc = findHeaderCell(column, firstLeftCell);

            this.add(hc);
        }
    }

    private HeaderCell findHeaderCell(ColumnData column, Cell firstLeftCell) {
        Row row = firstLeftCell.getRow();
        int columnIndex = firstLeftCell.getColumnIndex();

        for (int i = columnIndex; i < columnIndex + 10000; i++) {

            Cell headerCell = row.getCell(i);

            if (column.match(headerCell.getStringCellValue())) {
                return new HeaderCell(headerCell, column);
            }
        }

        return null;
    }

    @Data
    public static class HeaderCell {

        /**
         * The data column
         */
        protected ColumnData column;

        /**
         * The POI header cell for the column
         */
        protected Cell headerCell;

        public HeaderCell(Cell headerCell, ColumnData column) {
            this.headerCell = headerCell;
            this.column = column;
        }
    }

}
