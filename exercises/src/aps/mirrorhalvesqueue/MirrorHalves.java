package aps.mirrorhalvesqueue;


import java.util.NoSuchElementException;
import java.util.stream.IntStream;

interface Queue<E> {

    // Elementi na redicata se objekti od proizvolen tip.

    // Metodi za pristap:

    boolean isEmpty();
    // Vrakja true ako i samo ako redicata e prazena.

    int size();
    // Ja vrakja dolzinata na redicata.

    E peek();
    // Go vrakja elementot na vrvot t.e. pocetokot od redicata.

    // Metodi za transformacija:

    void clear();
    // Ja prazni redicata.

    void enqueue(E x);
    // Go dodava x na kraj od redicata.

    E dequeue();
    // Go otstranuva i vrakja pochetniot element na redicata.
}

interface Stack<E> {

    // Elementi na stekot se objekti od proizvolen tip.

    // Metodi za pristap:

    boolean isEmpty();
    // Vrakja true ako i samo ako stekot e prazen.

    E peek();
    // Go vrakja elementot na vrvot od stekot.

    // Metodi za transformacija:

    void clear();
    // Go prazni stekot.

    void push(E x);
    // Go dodava x na vrvot na stekot.

    E pop();
    // Go otstranuva i vrakja elementot shto e na vrvot na stekot.
}

class ArrayQueue<E> implements Queue<E> {

    // Redicata e pretstavena na sledniot nacin:
    // length go sodrzi brojot na elementi.
    // Ako length > 0, togash elementite na redicata se zachuvani vo elems[front...rear-1]
    // Ako rear > front, togash vo  elems[front...maxlength-1] i elems[0...rear-1]
    E[] elems;
    int length, front, rear;

    // Konstruktor ...

    @SuppressWarnings("unchecked")
    public ArrayQueue(int maxlength) {
        elems = (E[]) new Object[maxlength];
        clear();
    }

    public boolean isEmpty() {
        // Vrakja true ako i samo ako redicata e prazena.
        return (length == 0);
    }

    public int size() {
        // Ja vrakja dolzinata na redicata.
        return length;
    }

    public E peek() {
        // Go vrakja elementot na vrvot t.e. pocetokot od redicata.
        if (length > 0)
            return elems[front];
        else
            throw new NoSuchElementException();
    }

    public void clear() {
        // Ja prazni redicata.
        length = 0;
        front = rear = 0;  // arbitrary
    }

    public void enqueue(E x) {
        // Go dodava x na kraj od redicata.
        elems[rear++] = x;
        if (rear == elems.length) rear = 0;
        length++;
    }

    public E dequeue() {
        // Go otstranuva i vrakja pochetniot element na redicata.
        if (length > 0) {
            E frontmost = elems[front];
            elems[front++] = null;
            if (front == elems.length) front = 0;
            length--;
            return frontmost;
        } else
            throw new NoSuchElementException();
    }
}

class ArrayStack<E> implements Stack<E> {

    // Stekot e pretstaven na sledniot nacin:
    //depth e dlabochinata na stekot, a
    // elems[0...depth-1] se negovite elementi.
    private E[] elems;
    private int depth;

    @SuppressWarnings("unchecked")
    public ArrayStack(int maxDepth) {
        // Konstrukcija na nov, prazen stek.
        elems = (E[]) new Object[maxDepth];
        depth = 0;
    }


    public boolean isEmpty() {
        // Vrakja true ako i samo ako stekot e prazen.
        return (depth == 0);
    }


    public E peek() {
        // Go vrakja elementot na vrvot od stekot.
        if (depth == 0)
            throw new NoSuchElementException();
        return elems[depth - 1];
    }


    public void clear() {
        // Go prazni stekot.
        for (int i = 0; i < depth; i++) elems[i] = null;
        depth = 0;
    }


    public void push(E x) {
        // Go dodava x na vrvot na stekot.
        elems[depth++] = x;
    }


    public E pop() {
        // Go otstranuva i vrakja elementot shto e na vrvot na stekot.
        if (depth == 0)
            throw new NoSuchElementException();
        E topmost = elems[--depth];
        elems[depth] = null;
        return topmost;
    }
}

public class MirrorHalves {

    private static ArrayQueue<Integer> mirrorHalves(ArrayQueue<Integer> queue) {
        if (queue.isEmpty())
            return queue;
        if (queue.size() % 2 == 1)
            throw new IllegalArgumentException();

        int n = queue.size();
        ArrayStack<Integer> stack = new ArrayStack<>(n / 2 + 1);

        int iterations = 2;

        while (iterations > 0) {
            for (int i = 0; i < n / 2; i++) {
                stack.push(queue.peek());
                queue.enqueue(queue.dequeue());
            }
            while (!stack.isEmpty())
                queue.enqueue(stack.pop());
            iterations--;
        }

        return queue;
    }

    public static void main(String[] args) {
        ArrayQueue<Integer> queue = new ArrayQueue<>(13);

        IntStream.of(10, 50, 19, 54, 30, 67).forEach(queue::enqueue);

        queue = mirrorHalves(queue);

        System.out.print("Front ");
        while (!queue.isEmpty())
            System.out.printf("%d ", queue.dequeue());
        System.out.print("Back");

    }
}
