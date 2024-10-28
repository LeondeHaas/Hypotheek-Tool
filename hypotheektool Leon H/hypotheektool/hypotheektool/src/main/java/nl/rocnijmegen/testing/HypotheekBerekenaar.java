package nl.rocnijmegen.testing;

public class HypotheekBerekenaar {

    public double  berekenMaximaalLeenbedrag(double inkomen, double partnerInkomen, boolean heeftStudieschuld) {
        double totaalInkomen = inkomen + partnerInkomen;
        double maximaalLeenbedrag = totaalInkomen * 12 * 5;  // 5x het jaarinkomen

        if (heeftStudieschuld) {
            maximaalLeenbedrag *= 0.75;  // Verminder met 25% als er een studieschuld is
        }

        return maximaalLeenbedrag;
    }

    public double berekenMaandlasten(double maximaalLeenbedrag, double rentePercentage, int jaren) {
        double maandRente = rentePercentage / 100 / 12;
        int aantalBetalingen = jaren * 12;

        return maximaalLeenbedrag * (maandRente * Math.pow(1 + maandRente, aantalBetalingen)) / (Math.pow(1 + maandRente, aantalBetalingen) - 1);
    }
}
