

import org.junit.jupiter.api.Test;
import java.io.IOException;

public class MatrixTest
{
    @Test
    public void AmatrixTest1()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(100);
        for(int anz = 1 ; anz < a.getMatrix().length; anz++)
        {
            a.erstelleKante(anz, anz+1);
        }
        //a.erstelleKante(1,3);
        //a.loescheKante(2,3);
        //a.loescheKante(5,6);
        r.programmAblauf(a);
        System.out.println();
    }

    @Test
    public void test()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(4);
        a.erstelleKante(1,4);
        a.erstelleKante(2,4);
        a.erstelleKante(3,4);
        r.programmAblauf(a);
    }

    @Test
    public void AmatrixTest2()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(3);
        r.programmAblauf(a);
        System.out.println();
    }

    @Test
    public void AmatrixTest3()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(13);

        a.erstelleKante(1,2);
        a.erstelleKante(2,4);
        a.erstelleKante(2,3);
        a.erstelleKante(3,5);
        a.erstelleKante(5,6);
        a.erstelleKante(6,7);
        a.erstelleKante(6,8);
        a.erstelleKante(7,8);
        a.erstelleKante(8,9);
        a.erstelleKante(8,10);
        a.erstelleKante(10,12);
        a.erstelleKante(10,11);
        a.erstelleKante(12,13);
        a.erstelleKante(4,5);
        a.erstelleKante(11,12);

        //a.loescheKante(2,3);
        //a.loescheKante(5,6);
        r.programmAblauf(a);
        System.out.println();
    }

    @Test
    public void AmatrixTest4()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(26);

        a.erstelleKante(2,3);
        a.erstelleKante(2,4);
        a.erstelleKante(3,4);
        a.erstelleKante(3,5);
        a.erstelleKante(4,5);
        a.erstelleKante(4,6);
        a.erstelleKante(4,7);
        a.erstelleKante(6,8);
        a.erstelleKante(6,9);
        a.erstelleKante(9,10);
        a.erstelleKante(9,11);
        a.erstelleKante(10,11);
        a.erstelleKante(11,7);
        a.erstelleKante(12,13);
        a.erstelleKante(13,14);
        a.erstelleKante(15,16);
        a.erstelleKante(15,17);
        a.erstelleKante(15,18);
        a.erstelleKante(16,18);
        a.erstelleKante(16,17);
        a.erstelleKante(18,19);
        a.erstelleKante(18,20);
        a.erstelleKante(19,20);
        a.erstelleKante(20,21);
        a.erstelleKante(20,22);
        a.erstelleKante(20,23);
        a.erstelleKante(21,22);
        a.erstelleKante(21,23);
        a.erstelleKante(22,23);
        a.erstelleKante(22,24);
        a.erstelleKante(24,25);
        a.erstelleKante(24,26);
        a.erstelleKante(25,26);

        r.programmAblauf(a);
    }

    @Test
    public void AmatrixTest5()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(6);


        a.erstelleKante(1,2);
        a.erstelleKante(2,3);
        a.erstelleKante(3,4);
        a.erstelleKante(4,5);
        a.erstelleKante(5,6);
        a.erstelleKante(6,1);

        r.programmAblauf(a);
    }

    @Test
    public void AmatirxEuler()
    {
        Berechnung r = new Berechnung();
        Matrix a = new Matrix(9);


        a.erstelleKante(1,2);
        a.erstelleKante(2,4);
        a.erstelleKante(3,1);
        a.erstelleKante(3,5);
        a.erstelleKante(3,7);
        a.erstelleKante(4,5);
        a.erstelleKante(4,6);
        a.erstelleKante(4,3);
        a.erstelleKante(5,8);
        a.erstelleKante(5,7);
        a.erstelleKante(6,8);
        a.erstelleKante(7,8);
        a.erstelleKante(7,9);
        a.erstelleKante(8,9);


        r.programmAblauf(a);
    }

    @Test
    public void AmatirxCSV() throws IOException
    {
//        Berechnung r = new Berechnung();
//        Matrix a = new Matrix("PATH");
//
//
//        r.programmAblauf(a);
    }
}