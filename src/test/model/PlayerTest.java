package model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void hasExactlyTwoValuesInDeclarationOrder() {
        assertArrayEquals(new Player[] { Player.X, Player.O }, Player.values());
    }

    @Test
    void valueOfReturnsMatchingConstant() {
        assertEquals(Player.X, Player.valueOf("X"));
        assertEquals(Player.O, Player.valueOf("O"));
    }

    @Test
    void xAndOAreDistinct() {
        assertNotEquals(Player.X, Player.O);
    }
}
