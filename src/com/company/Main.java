package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    static int ilosc_poprawnie_klasyfikowanych = 0;
    static int ilosc_testowanych = 0;

    public static void main(String[] args) {

    String komenda;
    String wektor;

    int k;

    File train = new File("train.csv");
    File test = new File("test.csv");
    Scanner odczyt = new Scanner(System.in);
    Scanner odczyt_pliku = null;


        do {

            komenda = odczyt.nextLine();

            if(komenda.equals("klasyfikuj plik")){

                ilosc_testowanych = 0;
                ilosc_poprawnie_klasyfikowanych = 0;

                try {
                    odczyt_pliku = new Scanner(test);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println("Okresl K : ");
                k = Integer.parseInt(odczyt.nextLine());

                String linia_pliku;
                String[] wektor_przejsciowy;


                while(odczyt_pliku.hasNext()){
                    ilosc_testowanych++;
                    linia_pliku = odczyt_pliku.nextLine();
                    wektor_przejsciowy = linia_pliku.split(";");
                    String wektor_docelowy = "";
                    String klasyfikacja_prawidlowa = wektor_przejsciowy[wektor_przejsciowy.length-1];
                    //System.out.println(klasyfikacja_prawidlowa);

                    for (int i = 0; i < wektor_przejsciowy.length - 1; i++) {
                        wektor_docelowy = wektor_docelowy + wektor_przejsciowy[i]+";";
                    }
                    klasyfikuj(wektor_docelowy,k,train,klasyfikacja_prawidlowa);
                }

                double wynik = (double)ilosc_poprawnie_klasyfikowanych/(double)ilosc_testowanych;
                System.out.println("Ilosc poprawnie zaklasyfikowanych : " + ilosc_poprawnie_klasyfikowanych);
                System.out.println("Ilosc testowanych                 : " + ilosc_testowanych);
                System.out.println(wynik*100 + " %");
                odczyt_pliku.close();

            }else{
                if(komenda.equals("klasyfikuj wektor")){

                    System.out.println("PODAJ WEKTOR");
                    wektor = odczyt.nextLine();
                    System.out.println("PODAJ K");
                    k = Integer.parseInt(odczyt.nextLine());
                    klasyfikuj(wektor, k, train);

                }
            }

        }while(!komenda.equals("zakoncz"));


    }




    static void klasyfikuj(String wektor, int k, File dane){

        System.out.println(wektor);
        String[] wektor_tab = wektor.split(";");

        ArrayList<Double> list_setosa = new ArrayList<>();
        ArrayList<Double> list_versicolor = new ArrayList<>();
        ArrayList<Double> list_virginica = new ArrayList<>();



        Scanner in = null;
        try {
            in = new Scanner(dane);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        while(in.hasNext()){

            double suma = 0;
            double odleglosc = 0;
            String[] dane_tab = in.nextLine().split(";");

            for (int i = 0; i < dane_tab.length-1; i++) {
                suma = suma + (Math.pow((Double.valueOf(wektor_tab[i]) - Double.valueOf(dane_tab[i])),2));
                //System.out.print(dane_tab[i] + " ");
            }
            //System.out.println();
            odleglosc = Math.sqrt(suma);
            
            //System.out.println("ODLEGLOSC : " + odleglosc);
            //System.out.println(dane_tab[dane_tab.length-1]);

            switch (dane_tab[dane_tab.length-1]){

                case "Iris-setosa" :
                    //System.out.println("SETOSA");
                    list_setosa.add(odleglosc);
                    break;

                case "Iris-versicolor":
                    //System.out.println("VERSICOLOR");
                    list_versicolor.add(odleglosc);
                    break;

                case "Iris-virginica":
                    //System.out.println("VIRGINICA");
                    list_virginica.add(odleglosc);
                    break;

            }
            //System.out.println();
        }

        ///////////////////////////// dostepne 3 listy odleglosci

        HashMap<String,Integer> mapa_najblizszych = new HashMap<>();

        mapa_najblizszych.put("setosa",0);
        mapa_najblizszych.put("versicolor",0);
        mapa_najblizszych.put("virginica",0);

        System.out.println(list_setosa);
        Collections.sort(list_setosa);
        Collections.sort(list_versicolor);
        Collections.sort(list_virginica);



//        System.out.println();
//        System.out.println();
//        System.out.println();

        for (int i = 0; i < k; i++) {

            if(list_setosa.get(0) < list_versicolor.get(0) && list_setosa.get(0)<list_virginica.get(0)){
                mapa_najblizszych.put("setosa", mapa_najblizszych.get("setosa") + 1);
                //System.out.println("setosa : " + mapa_najblizszych.get("setosa"));
                list_setosa.remove(0);
            }

            if(list_versicolor.get(0) < list_setosa.get(0) && list_versicolor.get(0)<list_virginica.get(0)){
                mapa_najblizszych.put("versicolor", mapa_najblizszych.get("versicolor") + 1);
                //System.out.println("versicolor : " + mapa_najblizszych.get("versicolor"));
                list_versicolor.remove(0);
            }

            if(list_virginica.get(0) < list_versicolor.get(0) && list_virginica.get(0)<list_setosa.get(0)){
                mapa_najblizszych.put("virginica", mapa_najblizszych.get("virginica") + 1);
                //System.out.println("virginica : " + mapa_najblizszych.get("virginica"));
                list_virginica.remove(0);
            }

        }

        if(mapa_najblizszych.get("setosa") > mapa_najblizszych.get("versicolor") && mapa_najblizszych.get("setosa") > mapa_najblizszych.get("virginica")){
            System.out.println("kalsyfikacja do SETOSA");
        }

        if(mapa_najblizszych.get("versicolor") > mapa_najblizszych.get("setosa") && mapa_najblizszych.get("versicolor") > mapa_najblizszych.get("virginica")){
            System.out.println("kalsyfikacja do VERSICOLOR");
        }

        if(mapa_najblizszych.get("virginica") > mapa_najblizszych.get("versicolor") && mapa_najblizszych.get("virginica") > mapa_najblizszych.get("setosa")){
            System.out.println("kalsyfikacja do VIRGINICA");
        }


        in.close();


    }



    static void klasyfikuj(String wektor, int k, File dane,String klasyfikacja_prawidlowa){

        System.out.println(wektor);
        String[] wektor_tab = wektor.split(";");

        ArrayList<Double> list_setosa = new ArrayList<>();
        ArrayList<Double> list_versicolor = new ArrayList<>();
        ArrayList<Double> list_virginica = new ArrayList<>();


        Scanner in = null;
        try {
            in = new Scanner(dane);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        while(in.hasNext()){

            double suma = 0;
            double odleglosc = 0;
            String[] dane_tab = in.nextLine().split(";");

            for (int i = 0; i < dane_tab.length-1; i++) {
                suma = suma + (Math.pow((Double.valueOf(wektor_tab[i]) - Double.valueOf(dane_tab[i])),2));
                //System.out.print(dane_tab[i] + " ");
            }
            //System.out.println();
            odleglosc = Math.sqrt(suma);

            //System.out.println("ODLEGLOSC : " + odleglosc);
            //System.out.println(dane_tab[dane_tab.length-1]);

            switch (dane_tab[dane_tab.length-1]){

                case "Iris-setosa" :
                    //System.out.println("SETOSA");
                    list_setosa.add(odleglosc);
                    break;

                case "Iris-versicolor":
                    //System.out.println("VERSICOLOR");
                    list_versicolor.add(odleglosc);
                    break;

                case "Iris-virginica":
                    //System.out.println("VIRGINICA");
                    list_virginica.add(odleglosc);
                    break;

            }
            //System.out.println();
        }

        ///////////////////////////// dostepne 3 listy odleglosci

        HashMap<String,Integer> mapa_najblizszych = new HashMap<>();

        mapa_najblizszych.put("setosa",0);
        mapa_najblizszych.put("versicolor",0);
        mapa_najblizszych.put("virginica",0);

        Collections.sort(list_setosa);
        Collections.sort(list_versicolor);
        Collections.sort(list_virginica);

        list_setosa.forEach((a)->{
            System.out.println(a);
        });


//        System.out.println();
//        System.out.println();
//        System.out.println();

        for (int i = 0; i < k; i++) {

            if(list_setosa.get(0) < list_versicolor.get(0) && list_setosa.get(0)<list_virginica.get(0)){
                mapa_najblizszych.put("setosa", mapa_najblizszych.get("setosa") + 1);
                //System.out.println("setosa : " + mapa_najblizszych.get("setosa"));
                list_setosa.remove(0);
            }

            if(list_versicolor.get(0) < list_setosa.get(0) && list_versicolor.get(0)<list_virginica.get(0)){
                mapa_najblizszych.put("versicolor", mapa_najblizszych.get("versicolor") + 1);
                //System.out.println("versicolor : " + mapa_najblizszych.get("versicolor"));
                list_versicolor.remove(0);
            }

            if(list_virginica.get(0) < list_versicolor.get(0) && list_virginica.get(0)<list_setosa.get(0)){
                mapa_najblizszych.put("virginica", mapa_najblizszych.get("virginica") + 1);
                //System.out.println("virginica : " + mapa_najblizszych.get("virginica"));
                list_virginica.remove(0);
            }

        }

        if(mapa_najblizszych.get("setosa") > mapa_najblizszych.get("versicolor") && mapa_najblizszych.get("setosa") > mapa_najblizszych.get("virginica")){
            System.out.println("kalsyfikacja do SETOSA");
            System.out.println(klasyfikacja_prawidlowa);
            if(klasyfikacja_prawidlowa.equals("Iris-setosa")){
                ilosc_poprawnie_klasyfikowanych++;
            }else{
                System.out.println("                        BLAD");
            }
        }

        if(mapa_najblizszych.get("versicolor") > mapa_najblizszych.get("setosa") && mapa_najblizszych.get("versicolor") > mapa_najblizszych.get("virginica")){
            System.out.println("kalsyfikacja do VERSICOLOR");
            System.out.println(klasyfikacja_prawidlowa);
            if(klasyfikacja_prawidlowa.equals("Iris-versicolor")){
                ilosc_poprawnie_klasyfikowanych++;
            }else{
                System.out.println("                        BLAD");
            }
        }

        if(mapa_najblizszych.get("virginica") > mapa_najblizszych.get("versicolor") && mapa_najblizszych.get("virginica") > mapa_najblizszych.get("setosa")){
            System.out.println("kalsyfikacja do VIRGINICA");
            System.out.println(klasyfikacja_prawidlowa);
            if(klasyfikacja_prawidlowa.equals("Iris-virginica")){
                ilosc_poprawnie_klasyfikowanych++;
            }else{
                System.out.println("                        BLAD");
            }
        }


        in.close();


    }


}
