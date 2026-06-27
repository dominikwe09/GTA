import GLOOP.*;
class FirstPerson {
    private GLKamera     kamera;
    private Spieler      spieler;
    private Gegner[]     gegner;
    private Mausbewegung maus;
    private Fadenkreuz   fadenkreuz;
    private Waffe        waffe;

    private double blickNeigung = 0.0;
    private int cooldown = 0;

    FirstPerson(GLKamera kamera, Spieler spieler, Gegner[] gegner, Mausbewegung maus) {
        this.kamera  = kamera;
        this.spieler = spieler;
        this.gegner  = gegner;
        this.maus    = maus;

        fadenkreuz = new Fadenkreuz();
        waffe      = new Waffe();
    }

    void aktualisiere() {
        blickNeigung -= maus.gibBewegungY() * 0.25;

        double vx = spieler.gibVorwaertsX();
        double vy = blickNeigung / 100.0;
        double vz = spieler.gibVorwaertsZ();

        double[] pos = spieler.gibKameraPosition();
        kamera.setzePosition(pos[0], pos[1], pos[2]);
        kamera.setzeBlickpunkt(pos[0] + vx * 1000, pos[1] + vy * 1000, pos[2] + vz * 1000);

        fadenkreuz.aktualisiere(pos, vx, vy, vz, spieler.gibDrehWinkel());
        waffe.aktualisiere();
        waffe.aktualisiereUI(pos, vx, vy, vz, spieler.gibRechtsX(), spieler.gibRechtsZ());

        if (cooldown > 0) cooldown--;

        if (maus.gibMaus().linksklick() && cooldown == 0 && waffe.schiessen()) {
            cooldown = 3;
            schiessen(pos);
        }
    }

    void initialisiereKamera() {
        double[] pos = spieler.gibKameraPosition();
        kamera.setzePosition(pos[0], pos[1], pos[2]);
        kamera.setzeBlickpunkt(pos[0], pos[1], pos[2] - 1000);
    }

    void verbergeUI() {
        fadenkreuz.verstecken();
        waffe.versteckenUI();
    }

    void zeigeUI() {
        fadenkreuz.zeigen();
        waffe.zeigenUI();
    }

    // trifft sofort den nächsten Gegner auf der Schusslinie (nur xz)
    private void schiessen(double[] pos) {
        double sx = spieler.gibVorwaertsX();
        double sz = spieler.gibVorwaertsZ();

        Gegner ziel        = null;
        double naechste    = Double.MAX_VALUE;

        for (Gegner g : gegner) {
            if (!g.istLebendig()) continue;

            // Vektor vom Spieler zum Gegner
            double dx = g.gibX() - pos[0];
            double dz = g.gibZ() - pos[2];

            double proj = dx * sx + dz * sz;
            if (proj < 0) continue;

            // Senkrechter Abstand des Gegners von der Schusslinie
            double senkX = dx - proj * sx;
            double senkZ = dz - proj * sz;

            double r = g.gibRadius();
            // Treffer wenn senkrechter Abstand kleiner als Gegner-Radius und näher als bisheriges Ziel
            if (senkX * senkX + senkZ * senkZ <= r * r && proj < naechste) {
                naechste = proj;
                ziel     = g;
            }
        }

        if (ziel != null) {
            ziel.treffer(waffe.gibSchaden());
        }
    }
}
