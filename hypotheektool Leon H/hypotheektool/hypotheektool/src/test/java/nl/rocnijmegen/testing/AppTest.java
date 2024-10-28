package nl.rocnijmegen.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
    }

    // Test Case 1: Postcode validation (TC001)
    @Test
    void testControleerPostcode_ValidPostcode() {
        assertTrue(app.controleerPostcode("1234"));  // Postcode outside aardbevingsgebied
    }

    @Test
    void testControleerPostcode_InvalidPostcode() {
        assertFalse(app.controleerPostcode("9679"));  // Postcode in aardbevingsgebied
    }

    // Test Case 2: Loan calculation without partner and without student debt (TC002)
    @Test
    void testBerekenMaximaalLeenbedrag_ZonderPartnerZonderStudieschuld() {
        double inkomen = 3000.0;
        double partnerInkomen = 0.0;
        boolean heeftStudieschuld = false;

        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(180000.0, maximaalLeenbedrag, 0.01);  // 5x annual income
    }

    // Test Case 3: Loan calculation with partner and without student debt (TC003)
    @Test
    void testBerekenMaximaalLeenbedrag_MetPartnerZonderStudieschuld() {
        double inkomen = 4000.0;
        double partnerInkomen = 2000.0;
        boolean heeftStudieschuld = false;

        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(360000.0, maximaalLeenbedrag, 0.01);  // Combined income, no debt
    }

    // Test Case 4: Loan calculation with partner and with student debt (TC004)
    @Test
    void testBerekenMaximaalLeenbedrag_MetPartnerMetStudieschuld() {
        double inkomen = 4000.0;
        double partnerInkomen = 1500.0;
        boolean heeftStudieschuld = true;

        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(247500.0, maximaalLeenbedrag, 0.01);  // 25% reduction due to student debt
    }

    // Test Case 5: Invalid interest period (TC005)
    @Test
    void testBepaalRente_InvalidPeriod() {
        double rente = app.bepaalRente(15);  // Invalid period
        assertEquals(-1, rente);  // Expect -1 for invalid period
    }

    // Test Case 6: Loan calculation for 30-year fixed interest period (TC006)
    @Test
    void testBerekenMaandlasten_ZonderPartnerZonderStudieschuld_30Jaar() {
        double inkomen = 5000.0;
        double partnerInkomen = 0.0;
        boolean heeftStudieschuld = false;
        int jaren = 30;
        double rente = 5.0;

        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(300000.0, maximaalLeenbedrag, 0.01);  // Max loan calculation

        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Check that monthly payments are positive
    }
}
