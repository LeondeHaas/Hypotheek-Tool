package nl.rocnijmegen.testing;

import java.util.Scanner;

public class App {

    // Methode om te controleren of de postcode in het aardbevingsgebied ligt
    public boolean controleerPostcode(String postcode) {
        // Return false als de postcode in het aardbevingsgebied ligt (9679, 9681, 9682)
        return !(postcode.equals("9679") || postcode.equals("9681") || postcode.equals("9682"));
    }

    // Methode om het maximale leenbedrag te berekenen op basis van inkomen en partnerinkomen
    public double berekenMaximaalLeenbedrag(double inkomen, double partnerInkomen, boolean heeftStudieschuld) {
        double totaalInkomen = inkomen + partnerInkomen;
        double maximaalLeenbedrag = totaalInkomen * 12 * 5; // Test value: 5 times the annual income
        // Apply a smaller reduction of 20% if the user has a student debt
        if (heeftStudieschuld) {
            maximaalLeenbedrag *= 0.80;  // Adjusted reduction from 25% to 20%
        }

        return maximaalLeenbedrag;
    }

    // Methode om maandelijkse hypotheeklasten te berekenen
    public double berekenMaandlasten(double leenbedrag, double rentePercentage, int looptijdInJaren) {
        double maandRente = rentePercentage / 100 / 12;
        int aantalBetalingen = looptijdInJaren * 12;

        // Hypotheekformule
        return leenbedrag * (maandRente * Math.pow(1 + maandRente, aantalBetalingen)) / (Math.pow(1 + maandRente, aantalBetalingen) - 1);
    }

    // Methode om rente te bepalen op basis van de rentevaste periode
    public double bepaalRente(int jaren) {
        switch (jaren) {
            case 1:
                return 2.0;
            case 5:
                return 3.0;
            case 10:
                return 3.5;
            case 20:
                return 4.5;
            case 30:
                return 5.0;
            default:
                return -1;  // Ongeldige periode
        }
    }

    // CLI-hoofdmethode voor gebruikersinvoer
    public static void main(String[] args) {
        App app = new App();
        Scanner scanner = new Scanner(System.in);

        // Vraag gebruikers om hun postcode in te voeren
        System.out.print("Wat is uw postcode? ");
        String postcode = scanner.next();
        if (!app.controleerPostcode(postcode)) {
            System.out.println("Hypotheekberekeningen zijn niet toegestaan voor deze postcode vanwege het aardbevingsgebied.");
            System.exit(0);
        }

        // Vraag gebruikers om hun maandinkomen in te voeren
        System.out.print("Wat is uw maandinkomen? ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Ongeldige invoer, probeer het opnieuw.");
            System.exit(0);
        }
        double inkomen = scanner.nextDouble();

        // Vraag gebruikers om hun partnersituatie en inkomen in te voeren
        System.out.print("Heeft u een partner? (ja/nee): ");
        boolean heeftPartner = scanner.next().equalsIgnoreCase("ja");
        double partnerInkomen = 0;
        if (heeftPartner) {
            System.out.print("Wat is het maandinkomen van uw partner? ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Ongeldige invoer, probeer het opnieuw.");
                System.exit(0);
            }
            partnerInkomen = scanner.nextDouble();
        }

        // Vraag gebruikers om hun studieschuld in te voeren
        System.out.print("Heeft u een studieschuld? (ja/nee): ");
        boolean heeftStudieschuld = scanner.next().equalsIgnoreCase("ja");

        // Vraag gebruikers om hun rentevaste periode in te voeren
        System.out.print("Kies een rentevaste periode: 1, 5, 10, 20 of 30 jaar: ");
        int jaren = scanner.nextInt();
        double rentePercentage = app.bepaalRente(jaren);

        // Controleer of de rentevaste periode geldig is
        if (rentePercentage == -1) {
            System.out.println("Ongeldige rentevaste periode. Voer een geldige periode in: 1, 5, 10, 20, of 30 jaar.");
            System.exit(0);
        }

        // Bereken het maximale leenbedrag en de maandelijkse hypotheeklasten
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(inkomen, partnerInkomen, heeftStudieschuld);
        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rentePercentage, jaren);

        // Toon de resultaten aan de gebruiker
        System.out.println("Op basis van uw inkomen kunt u maximaal lenen: " + maximaalLeenbedrag);
        System.out.println("Maandelijkse hypotheeklasten: " + maandlasten);

        scanner.close();
    }
}
