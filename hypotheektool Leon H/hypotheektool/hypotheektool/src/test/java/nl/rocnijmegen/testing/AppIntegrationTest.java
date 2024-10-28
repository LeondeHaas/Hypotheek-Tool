package nl.rocnijmegen.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppIntegrationTest {

    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
    }

    // Integration Test 1: Full Loan Calculation Process Without Partner or Student Debt
    @Test
    void testLoanCalculationProcess_ZonderPartnerZonderStudieschuld() {
        // Step 1: Validate postcode (assuming this affects loan calculations)
        assertTrue(app.controleerPostcode("1234"));  // Valid postcode

        // Step 2: Calculate maximum loan amount
        double inkomen = 3000.0;
        double partnerInkomen = 0.0;
        boolean heeftStudieschuld = false;
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(180000.0, maximaalLeenbedrag, 0.01);  // 3000 * 12 * 5 = 180000

        // Step 3: Calculate monthly payments for a 30-year fixed period
        int jaren = 30;
        double rente = app.bepaalRente(jaren);  // Using actual method to get rate
        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Check if monthly payments are correctly positive
    }

    // Integration Test 2: Full Loan Calculation Process With Partner and Student Debt
    @Test
    void testLoanCalculationProcess_MetPartnerMetStudieschuld() {
        // Step 1: Validate postcode (assuming this affects loan calculations)
        assertTrue(app.controleerPostcode("1234"));  // Valid postcode

        // Step 2: Calculate maximum loan amount with partner income and student debt
        double inkomen = 4000.0;
        double partnerInkomen = 2000.0;
        boolean heeftStudieschuld = true;
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(480000.0 * 0.80, maximaalLeenbedrag, 0.01);  // 6000 * 12 * 5 = 360000, reduced by 20%

        // Step 3: Calculate monthly payments for a 20-year fixed period
        int jaren = 20;
        double rente = app.bepaalRente(jaren);  // Using actual method to get rate
        assertTrue(rente > 0);  // Ensure we get a valid interest rate

        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Check if monthly payments are positive
    }

    // Integration Test 3: Full Loan Calculation Process With Student Debt Only
    @Test
    void testLoanCalculationProcess_ZonderPartnerMetStudieschuld() {
        // Step 1: Validate postcode (assuming this affects loan calculations)
        assertTrue(app.controleerPostcode("1234"));  // Valid postcode

        // Step 2: Calculate maximum loan amount with student debt and no partner income
        double inkomen = 2500.0;  // Individual's income
        double partnerInkomen = 0.0;  // No partner income
        boolean heeftStudieschuld = true;  // Individual has student debt
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        assertEquals(150000.0, maximaalLeenbedrag, 0.01);  // 2500 * 12 * 5 = 150000

        // Step 3: Calculate monthly payments for a 25-year fixed period
        int jaren = 25;  // Change to 25 years for the test case
        double rente = 3.5; // Assuming 3.5% for a 25-year fixed period (you might want to define it in your App class)
        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);  // Check if monthly payments are positive
    }
}
