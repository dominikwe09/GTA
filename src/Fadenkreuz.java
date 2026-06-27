import GLOOP.*;
class Fadenkreuz {
    private double kameraAbstand = 100;
    private double laenge        = 4;
    private double dicke         = 0.9;

    private GLQuader horizontal;
    private GLQuader vertikal;

    Fadenkreuz() {
        horizontal = new GLQuader(0, 0, 0, laenge * 2, dicke, dicke);
        vertikal   = new GLQuader(0, 0, 0, dicke, laenge * 2, dicke);

        horizontal.setzeFarbe        (1, 1, 1);
        horizontal.setzeSelbstleuchten(1, 1, 1);
        vertikal.setzeFarbe          (1, 1, 1);
        vertikal.setzeSelbstleuchten  (1, 1, 1);
    }

    void aktualisiere(double[] kameraPos, double vorwaertsX, double vorwaertsY, double vorwaertsZ, double drehWinkel) {
        double neueX = kameraPos[0] + vorwaertsX * kameraAbstand;
        double neueY = kameraPos[1] + vorwaertsY * kameraAbstand;
        double neueZ = kameraPos[2] + vorwaertsZ * kameraAbstand;

        horizontal.setzePosition(neueX, neueY, neueZ);
        vertikal.setzePosition  (neueX, neueY, neueZ);
        horizontal.setzeDrehung(0, drehWinkel, 0);
        vertikal.setzeDrehung  (0, drehWinkel, 0);
    }

    void verstecken() {
        horizontal.setzeSichtbarkeit(false);
        vertikal.setzeSichtbarkeit  (false);
    }

    void zeigen() {
        horizontal.setzeSichtbarkeit(true);
        vertikal.setzeSichtbarkeit  (true);
    }
}
