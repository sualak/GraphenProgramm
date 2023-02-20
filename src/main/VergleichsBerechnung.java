

public class VergleichsBerechnung
{
    private int[][] temp;
    private int[][] tempHilfe;
    private int[][] distanzMatrix;
    private int[][] wegMatrix;
    private int anzahlKomponenten = 0;


    //ausführen
    private void execute(Matrix matrix)
    {
        int groesse = matrix.getGroesse();
        int[][] aMatrix = matrix.getMatrix();

        erstelleDistanzMatrix(aMatrix,groesse);

        temp = new int[groesse][groesse];

        multiplikation1(aMatrix,groesse);

        int anz = 2;
        while(true)
        {
            boolean distanzMatrixFertig = distanzMatrix(temp, anz, groesse);
            if(anz >= aMatrix.length - 1 || distanzMatrixFertig)
            {
                break;
            }
            multiplikationN(aMatrix, groesse);
            anz++;
        }

        wegMatrix(groesse);
        komponenten(wegMatrix);
    }

    private void executeKnoten(Matrix adjazentsMatrix, int toDeleat)
    {
        int groesse = adjazentsMatrix.getGroesse();
        int[][] aMatrix = adjazentsMatrix.getMatrix();

        erstelleDistanzMatrix(aMatrix,groesse);

        temp = new int[groesse][groesse];

        multiplikation1(aMatrix,groesse);

        int anz = 2;
        while(true)
        {
            boolean distanzMatrixFertig = distanzMatrix(temp, anz, groesse);
            if(anz >= aMatrix.length - 1 || distanzMatrixFertig)
            {
                break;
            }
            multiplikationN(aMatrix, groesse);
            anz++;
        }

        wegMatrixKnoten(groesse, toDeleat);
        komponentenKnoten(wegMatrix, toDeleat);
    }


    //start für brücken berechnung
    public void brueckenBerechnung(Matrix adjanzentsMatrix, int groesse, Berechnung berechnung)
    {
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            for(int spalte = 1+reihe; spalte < groesse;spalte++)
            {
                if(adjanzentsMatrix.getMatrix() [reihe][spalte] == 1)
                {
                    anzahlKomponenten = 0;
                    adjanzentsMatrix.loescheKante(reihe+1, spalte+1);
                    execute(adjanzentsMatrix);
                    if(berechnung.getAnzahlKomponenten() < anzahlKomponenten)
                    {
                        berechnung.addBruecke("("+reihe+","+spalte+")");
                    }
                    adjanzentsMatrix.erstelleKante(reihe+1, spalte+1);
                }
            }
        }
    }

    //start für artikulationen berechnung
    public void artitkulationBerechnung(Matrix adjazentMatrix, Berechnung berechnung, int toDeleat)
    {
        anzahlKomponenten = 0;

        executeKnoten(adjazentMatrix, toDeleat);
        if(berechnung.getAnzahlKomponenten() < anzahlKomponenten)
        {
            berechnung.addArtikulationen(toDeleat);
        }
    }


    //matrixen
    private void multiplikation1(int[][] basisMatrix, int groesse)
    {
        for(int reihe=0; reihe<groesse; reihe++)
        {
            for(int spalte = 0; spalte<groesse;spalte++)
            {
                int fill=0;
                for(int r = 0; r<groesse; r++)
                {
                    fill += basisMatrix[reihe][r] * basisMatrix[r][spalte];
                }
                temp[reihe][spalte] = fill;
            }
        }
    }

    private void multiplikationN(int[][] basisMatrix, int grosse)
    {
        tempHilfe = new int[grosse][grosse];
        for(int hilfeX = 0; hilfeX<grosse; hilfeX++)
        {
            for(int hilfeY =0; hilfeY<grosse; hilfeY++)
            {
                tempHilfe[hilfeX][hilfeY] = temp[hilfeX][hilfeY];
            }
        }
        for(int reihe=0; reihe<grosse; reihe++)
        {
            for(int spalte = 0; spalte<grosse;spalte++)
            {
                int fill=0;
                for(int r = 0; r<grosse; r++)
                {
                    fill += basisMatrix[reihe][r] * tempHilfe[r][spalte];
                }
                temp[reihe][spalte] = fill;
            }
        }
    }

    private void erstelleDistanzMatrix(int[][] basisMatrix, int groesse)
    {
        distanzMatrix = new int [groesse][groesse];
        for(int hilfeX = 0; hilfeX<groesse; hilfeX++)
        {
            for(int hilfeY =0; hilfeY<groesse; hilfeY++)
            {
                distanzMatrix[hilfeX][hilfeY] = basisMatrix[hilfeX][hilfeY];
            }
        }
    }

    private boolean distanzMatrix(int[][] nMatrix, int anz, int groesse)
    {
        boolean geaendert = false;
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            for(int spalte = 1+reihe; spalte < groesse;spalte++)
            {
                if(distanzMatrix[reihe][spalte] == 0 && distanzMatrix[reihe][spalte] != nMatrix[reihe][spalte])
                {
                    distanzMatrix[reihe][spalte] = anz;
                    distanzMatrix[spalte][reihe] = anz;
                    geaendert = true;
                }
            }
        }
        return !geaendert;
    }

    private void wegMatrix(int groesse)
    {
        wegMatrix = new int[groesse][groesse];
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            for(int spalte = 0; spalte < groesse;spalte++)
            {
                if(distanzMatrix[reihe][spalte] != 0 || reihe == spalte)
                {
                    wegMatrix[reihe][spalte] = 1;
                }
            }
        }
    }

    private void wegMatrixKnoten(int groesse, int toDeleat)
    {
        wegMatrix = new int[groesse][groesse];
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            for(int spalte = 0; spalte < groesse;spalte++)
            {
                if(distanzMatrix[reihe][spalte] != 0 || reihe == spalte && reihe != toDeleat && spalte != toDeleat)
                {
                    wegMatrix[reihe][spalte] = 1;
                }
            }
        }
    }


    //werte
    public void komponenten(int[][] wegMatrix)
    {
        boolean fertig = false;
        boolean[] schonGefunden = new boolean [wegMatrix.length];

        while(!fertig)
        {
            fertig = true;
            for(int startReihe = 0; startReihe < wegMatrix.length; startReihe++)
            {
                if(!schonGefunden[startReihe])
                {
                    schonGefunden[startReihe] = true;

                    for(int vergleichsReihe = 1+startReihe; vergleichsReihe < wegMatrix.length; vergleichsReihe++)
                    {
                        schonGefunden[vergleichsReihe] = true;
                        for(int spalte = 0; spalte < wegMatrix.length; spalte++)
                        {
                            if(wegMatrix[startReihe][spalte] != wegMatrix[vergleichsReihe][spalte])
                            {
                                schonGefunden[vergleichsReihe] = false;
                                break;
                            }
                        }
                    }
                    for(int f=0; f<wegMatrix.length; f++)
                    {
                        if(!schonGefunden[f])
                        {
                            fertig = false;
                        }
                    }
                    anzahlKomponenten++;
                }
            }
        }
    }

    public void komponentenKnoten(int[][] wegMatrix, int toDeleat)
    {
        boolean fertig = false;
        boolean[] schonGefunden = new boolean [wegMatrix.length];

        while(!fertig)
        {
            fertig = true;
            for(int startReihe = 0; startReihe < wegMatrix.length; startReihe++)
            {
                if(!schonGefunden[startReihe] && startReihe != toDeleat)
                {
                    schonGefunden[startReihe] = true;

                    for(int vergleichsReihe = 1+startReihe; vergleichsReihe < wegMatrix.length; vergleichsReihe++)
                    {
                        schonGefunden[vergleichsReihe] = true;
                        for(int spalte = 0; spalte < wegMatrix.length; spalte++)
                        {
                            if(wegMatrix[startReihe][spalte] != wegMatrix[vergleichsReihe][spalte])
                            {
                                schonGefunden[vergleichsReihe] = false;
                                break;
                            }
                        }
                    }
                    for(int f=0; f<wegMatrix.length; f++)
                    {
                        if(!schonGefunden[f])
                        {
                            fertig = false;
                        }
                    }
                    anzahlKomponenten++;
                }
            }
        }
    }
}