
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Berechnung
{
    private int[][] temp;
    private int[][] tempHilfe;
    private int[][] distanzMatrix;
    private int[][] wegMatrix;
    private int anzahlKomponenten = 0;
    private int anzahlBruecken = 0;
    private int anzahlArtikulationen = 0;
    private int[][] exzentrizitaeten;
    private int radius = 0;
    private int durchmesser = 0;
    private ArrayList<Integer> zentrum = new ArrayList<>();
    private ArrayList<String> euler = new ArrayList<>();
    private ArrayList<String> bruecken = new ArrayList<>();
    private ArrayList<Integer> artikulationen = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> komponente = new ArrayList<>();
    private static final int UNENDLICH = Integer.MAX_VALUE;
    private static String infinitySymbol = null;



    //ausführen
    public void programmAblauf(Matrix matrix)
    {
        try {
            infinitySymbol = new String(String.valueOf(Character.toString('\u221E')).getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            infinitySymbol = "?";
            //ex.printStackTrace(); //print the unsupported encoding exception.
        }

        long start = System.nanoTime();

        int groesse = matrix.getGroesse();

        int[][] aMatrix = matrix.getMatrix();

        ertstelleDistanzMatrix(aMatrix,groesse);

        temp = new int[groesse][groesse];

        multiplikation1(aMatrix,groesse);

        int anz = 2;
        while(true)
        {
            boolean dMatrixFertig = true;
            dMatrixFertig = distanzMatrix(temp, anz, groesse);
            if(anz >= aMatrix.length - 1 || dMatrixFertig)
            {
                break;
            }
            multiplikationN(aMatrix, groesse);
            anz++;
        }

        wegMatrix(groesse);

        komponenten(wegMatrix,groesse);

        System.out.println("Adjazenz Matrix:");
        print(aMatrix, groesse);
        System.out.println();

        System.out.println("Distanz Matrix:");
        print(distanzMatrix, groesse);
        System.out.println();

        System.out.println("Weg Matrix:");
        print(wegMatrix, groesse);
        System.out.println();
        System.out.println("Die Matrix hat:" +anzahlKomponenten+" komponenten");
        for(ArrayList<Integer> arrays:komponente)
        {
            System.out.println(arrays);
        }

        VergleichsBerechnung rV = new VergleichsBerechnung();
        rV.brueckenBerechnung(matrix, groesse, this);

        System.out.println();
        System.out.println("Die Matrix hat:" +anzahlBruecken+" brücken");
        System.out.println(bruecken);
        for(int toDeleat = 0; toDeleat < groesse; toDeleat++)
        {
            Matrix amatrixVergleich = new Matrix(matrix, toDeleat);
            rV.artitkulationBerechnung(amatrixVergleich, this, toDeleat);
        }

        System.out.println();
        System.out.println("Die Matrix hat:" +anzahlArtikulationen+" artikulationen");
        System.out.println(artikulationen);

        exzentrizitaeten(distanzMatrix, groesse);
        System.out.println();
        System.out.println("Exzentrizitaeten Matrix:");
        print(exzentrizitaeten, groesse, 3);

        radius(exzentrizitaeten, groesse);
        System.out.println();
        zentren(exzentrizitaeten, groesse, radius);
        if(radius != UNENDLICH)
        {
            System.out.println("Die Matrix hat: radius " +radius+" und das zentrum ist der knoten " +zentrum);
        }
        else
        {
            System.out.println("Die Matrix hat: radius " +infinitySymbol);
        }

        durchmesser(exzentrizitaeten, groesse);
        System.out.println();
        if(durchmesser != UNENDLICH)
        {
            System.out.println("Die Matrix hat: durchmesser " +durchmesser);
        }
        else
        {
            System.out.println("Die Matrix hat: durchmesser " +infinitySymbol);
        }

        boolean[][] besucht = new boolean[groesse][groesse];

        if(anzahlKomponenten == 1 && alleGerade(groesse, aMatrix))
        {
            findStart(0, aMatrix, groesse, besucht, 0);
        }
        else
        {
            euler.add("Dieser Graph hat keinen Euler zyklus");
        }
        System.out.println();
        System.out.println("Der Euler zyklus dieses Graphen ist: ");
        System.out.print(euler);

        long ende = System.nanoTime();

        System.out.println();
        System.out.println("\ntime: " + (ende-start));
    }


    //getter/add
    public int getAnzahlKomponenten()
    {
        return anzahlKomponenten;
    }

    public void addBruecke(String kante)
    {
        anzahlBruecken++;
        bruecken.add(kante);
    }

    public void addArtikulationen(int toDeleat)
    {
        anzahlArtikulationen++;
        artikulationen.add(toDeleat);
    }


    //Matrixen
    private void multiplikation1(int[][] basisMatrix, int grosse)
    {
        for(int reihe=0; reihe<grosse; reihe++)
        {
            for(int spalte = 0; spalte<grosse;spalte++)
            {
                int fill=0;
                for(int r = 0; r<grosse; r++)
                {
                    fill += basisMatrix[reihe][r] * basisMatrix[r][spalte];
                }
                temp[reihe][spalte] = fill;
            }
        }
    }

    private void multiplikationN(int[][] basisMatrix, int groesse)
    {
        tempHilfe = new int[groesse][groesse];
        for(int hilfeX = 0; hilfeX<groesse; hilfeX++)
        {
            for(int hilfeY =0; hilfeY<groesse; hilfeY++)
            {
                tempHilfe[hilfeX][hilfeY] = temp[hilfeX][hilfeY];
            }
        }
        for(int reihe=0; reihe<groesse; reihe++)
        {
            for(int spalte = 0; spalte<groesse;spalte++)
            {
                int fill=0;
                for(int r = 0; r<groesse; r++)
                {
                    fill += basisMatrix[reihe][r] * tempHilfe[r][spalte];
                }
                temp[reihe][spalte] = fill;
            }
        }
    }

    private void ertstelleDistanzMatrix(int[][] basisMatrix, int groesse)
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

    private boolean distanzMatrix(int[][] nMatrix, int anzahl, int groesse)
    {
        boolean geaendert = false;
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            for(int spalte = 1+reihe; spalte < groesse;spalte++)
            {
                if(distanzMatrix[reihe][spalte] == 0 && distanzMatrix[reihe][spalte] != nMatrix[reihe][spalte])
                {
                    distanzMatrix[reihe][spalte] = anzahl;
                    distanzMatrix[spalte][reihe] = anzahl;
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

    private void exzentrizitaeten(int[][] distanzMatrix, int groesse)
    {
        exzentrizitaeten = new int[groesse][3];
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            int max = -1;
            for(int spalte = 0; spalte < groesse;spalte++)
            {
                if(reihe != spalte)
                {
                    if(distanzMatrix[reihe][spalte] == 0)
                    {
                        exzentrizitaeten[reihe][0] = reihe;
                        exzentrizitaeten[reihe][1] = UNENDLICH;
                        exzentrizitaeten[reihe][2] = spalte;
                        break;
                    }
                    else if(distanzMatrix[reihe][spalte] > max)
                    {
                        max = distanzMatrix[reihe][spalte];
                        exzentrizitaeten[reihe][0] = reihe;
                        exzentrizitaeten[reihe][1] = max;
                        exzentrizitaeten[reihe][2] = spalte;
                    }
                }
            }
        }
    }


    //werte
    private void radius(int[][] exzentrizitaeten, int groesse)
    {
        int min = Integer.MAX_VALUE;
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            if(exzentrizitaeten[reihe][1] < min)
            {
                min = exzentrizitaeten[reihe][1];
            }
        }
        radius = min;
    }


    private void zentren(int[][] exzentrizitaeten, int groesse, int radius)
    {
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            if(exzentrizitaeten[reihe][1] == radius)
            {
                zentrum.add(exzentrizitaeten[reihe][0]);
            }
        }
    }

    private void durchmesser(int[][] exzentrizitaeten, int groesse)
    {
        int max = -1;
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            if(exzentrizitaeten[reihe][1] > max)
            {
                max = exzentrizitaeten[reihe][1];
            }
        }
        durchmesser = max;
    }

    public void komponenten(int[][] wegMatrix, int groesse)
    {
        boolean fertig = false;
        boolean[] schonGefunden = new boolean [groesse];

        while(!fertig)
        {
            fertig = true;
            for(int startReihe = 0; startReihe < groesse; startReihe++)
            {
                if(!schonGefunden[startReihe])
                {
                    this.komponente.add(new ArrayList<>());
                    schonGefunden[startReihe] = true;
                    this.komponente.get(anzahlKomponenten).add(startReihe);
                    for(int vergleichsReihe = startReihe + 1; vergleichsReihe < groesse; vergleichsReihe++)
                    {
                        for(int spalte = 0; spalte < groesse; spalte++)
                        {
                            if(wegMatrix[startReihe][spalte] != wegMatrix[vergleichsReihe][spalte])
                            {
                                break;
                            }
                            else
                            if(spalte == groesse-1)
                            {
                                this.komponente.get(anzahlKomponenten).add(vergleichsReihe);
                                schonGefunden[vergleichsReihe] = true;
                            }
                        }
                    }
                    for(int f=0; f<groesse; f++)
                    {
                        //wenn noch nicht alle Reihen gefunden wurden, wird fertig auf false gesetzt
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


    //print
    private void print(int[][] matrix, int groesse)
    {
        for(int reihe = 0; reihe < groesse; reihe++)
        {
            for(int spalte = 0; spalte < groesse;spalte++)
            {
                System.out.print(matrix[reihe][spalte]+" ");
            }
            System.out.println();
        }
    }

    private void print(int[][] matrix, int groesseX, int groesseY)
    {
        for(int reihe = 0; reihe < groesseX; reihe++)
        {
            for(int spalte = 0; spalte < groesseY;spalte++)
            {
                if(matrix[reihe][spalte] == UNENDLICH)
                {
                    System.out.print(infinitySymbol+" ");
                }
                else
                {
                    System.out.print(matrix[reihe][spalte]+" ");
                }
            }
            System.out.println();
        }
    }

    public void findStart(int reihe, int[][] amatrix, int breite, boolean[][] besucht, int pos)
    {
        for (int spalte = 0; spalte < breite; spalte++)
        {
            if (amatrix[reihe][spalte] == 1 && !besucht[reihe][spalte])
            {
                besucht[reihe][spalte] = true;
                besucht[spalte][reihe] = true;
                euler.add(pos,"("+reihe+"/"+spalte+")");
                pos++;
                findStart(spalte, amatrix, breite, besucht, pos);
                pos--;
            }
        }
    }

    public boolean alleGerade(int breite, int[][] amatrix)
    {
        for(int reihe = 0; reihe<breite; reihe++)
        {
            if(berechneKnotengrad(reihe , amatrix, breite)%2 == 1)
            {
                return false;
            }
        }
        return true;
    }

    public int berechneKnotengrad(int reihe, int[][]amatrix, int breite)
    {
        int anz = 0;
        for(int spalte = 0; spalte<breite; spalte++)
        {
            if(amatrix[reihe][spalte] == 1)
            {
                anz++;
            }
        }
        return anz;
    }
}