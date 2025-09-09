package parcial2.Service;
import java.util.ArrayDeque;
import java.util.List;


import parcial2.Model.LinkedListImpl;
import parcial2.Model.Nodo;
import parcial2.Util.ApiDataFetcher;

public class Frase {
    private static List<String> quotes;
    private static ArrayDeque<LinkedListImpl> encriptedQuotes;
    private static ArrayDeque<LinkedListImpl> encriptedSwappedQuotes;

    public Frase() {
        quotes = ApiDataFetcher.fetchDefaultData();
    }

    public void encriptQuote() {
        String auxQuote;
        String[] auxQuoteArray;
        LinkedListImpl auxLinkedList = new LinkedListImpl();
        Nodo auxNodo;
        char auxChar;
        int auxAsciiValue;
        int auxCounter = 1;
        for (int i = 0; i < quotes.size(); i++) {
            auxQuote = quotes.get(i);

            auxQuoteArray = auxQuote.split(" ");

            for (int j = 0; j < auxQuoteArray.length; j++) {
                auxChar = auxQuoteArray[j].charAt(0);
                auxAsciiValue = ((int) auxChar) + auxCounter;
                auxCounter += 2;

                auxNodo = new Nodo(auxAsciiValue);

                auxLinkedList.agregarFinal(auxNodo);
            }

        }
        encriptedQuotes.add(auxLinkedList);

    }

    public void encriptSwappedQuotes() {
        Nodo auxNodo;
        for (LinkedListImpl encriptedQuote : encriptedQuotes) {
            for (int i = 0; i < encriptedQuote.counter; i++) {
                auxNodo = encriptedQuote.cabeza; // 1
                encriptedQuote.cabeza.siguiente = auxNodo; // 2
                auxNodo = encriptedQuote.cabeza.siguiente.siguiente;
            }
            encriptedSwappedQuotes.add(encriptedQuote);
        }


    }

    public void showSwaw() {
        System.out.println("Mostrando frases encriptadas e intercambiadas:");
        for (LinkedListImpl quote : encriptedSwappedQuotes) {
            quote.mostrar();
        }
    }
}
