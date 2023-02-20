
import java.util.List;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class Matrix
{
    private int[][] matrix;
    private int groesse;

    //cons
    public Matrix(int groesse)
    {
        matrix = new int[groesse][groesse];
        this.groesse = groesse;
    }

    public Matrix(Matrix matrix, int toDeleat)
    {
        groesse = matrix.getGroesse();
        this.matrix = new int[groesse][groesse];


        for(int hilfeX = 0; hilfeX< groesse; hilfeX++)
        {
            for(int hilfeY = 0; hilfeY< groesse; hilfeY++)
            {
                if(hilfeX != toDeleat && hilfeY != toDeleat)
                {
                    this.matrix[hilfeX][hilfeY] = matrix.getMatrix()[hilfeX][hilfeY];
                }
            }
        }
    }

    public Matrix(String filePath) throws IOException
    {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add(Arrays.asList(values));
            }
        }

        groesse = records.size();
        matrix = new int[groesse][groesse];
        for (int reihe = 0; reihe < groesse; reihe++)
        {
            for (int spalte = 0; spalte < groesse; spalte++)
            {
                matrix[reihe][spalte] = Integer.parseInt(records.get(reihe).get(spalte));
            }
        }

        for (int reihe = 0; reihe < groesse; reihe++)
        {
            for (int spalte = reihe+1; spalte < groesse; spalte++)
            {
                if(matrix[reihe][spalte] != matrix[spalte][reihe])
                {
                    throw new IllegalStateException("Kanten in CSV file sind nich gespiegelt um die Hauptdiagonale");
                }
            }
        }
    }


    //add
    public void erstelleKante(int von, int bis)
    {
        if(von <= matrix[0].length && bis <= matrix[0].length)
        {
            matrix[von-1][bis-1] = 1;
            matrix[bis-1][von-1] = 1;
        }
    }

    public void loescheKante(int von, int bis)
    {
        if(von <= matrix[0].length && bis <= matrix[0].length)
        {
            matrix[von-1][bis-1] = 0;
            matrix[bis-1][von-1] = 0;
        }
    }


    //getter
    public int[][] getMatrix()
    {
        return matrix;
    }

    public int getGroesse()
    {
        return groesse;
    }
}