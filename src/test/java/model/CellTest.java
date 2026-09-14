package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CellTest {

    @Test
    void newCellHasNoValueByDefault() {
        Cell cell = new Cell();
        assertNull(cell.getValue());
    }

    @Test
    void setValueThenGetValueReturnsThatValue() {
        Cell cell = new Cell();

        cell.setValue(Player.X);
        assertEquals(Player.X, cell.getValue());

        cell.setValue(Player.O);
        assertEquals(Player.O, cell.getValue());
    }

    @Test
    void setValueToNullClearsTheCell() {
        Cell cell = new Cell();
        cell.setValue(Player.X);

        cell.setValue(null);

        assertNull(cell.getValue());
    }
}
