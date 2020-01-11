package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SettingsTest {
    
    @Test
    public void testGetSingleProperty(){
        assertEquals("3307", Settings.getPropertyValue("db.port"));
    }
    
    @Test
    public void testGetDEV_DBConnection(){
        String dbName = Settings.getPropertyValue("db.database");
        assertEquals(String.format("jdbc:mysql://localhost:3307/%s",dbName), Settings.getDEV_DBConnection());
    }
    
    @Test
    public void testGetTEST_DBConnection(){
        String dbName = Settings.getPropertyValue("dbtest.database");
        assertEquals(String.format("jdbc:mysql://localhost:3307/%s",dbName), Settings.getTEST_DBConnection());
    }
    
}
