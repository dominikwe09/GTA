import GLOOP.*;
class ThirdPerson {
    private double abstand = 600;
    private double hoehe   = 400;

    private GLKamera kamera;
    private Spieler  spieler;

    ThirdPerson(GLKamera kamera, Spieler spieler) {
        this.kamera  = kamera;
        this.spieler = spieler;
    }

    void aktualisiere() {
        double kameraX = spieler.gibX() - spieler.gibVorwaertsX() * abstand;
        double kameraY = spieler.gibY() + hoehe;
        double kameraZ = spieler.gibZ() - spieler.gibVorwaertsZ() * abstand;

        kamera.setzePosition(kameraX, kameraY, kameraZ);
        kamera.setzeBlickpunkt(spieler.gibX(), spieler.gibY() + 60, spieler.gibZ());
    }
}
