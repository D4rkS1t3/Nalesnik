/*Napisz program symulujący działanie maszyny wykonującej trzy zadania: smażenie naleśników, smarowanie ich dżemem,
zwijanie w rulon. Naleśniki mają być przekazywane jak  na  taśmie  produkcyjnej  do  kolejnych  etapów.
Wykorzystaj  kolejkę  synchronizowaną. Każdy  konsumowany  naleśnik  musi  być  usmażony, posmarowany  dżemem  i  zwinięty wrulon*/

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Pancake{
    boolean isCooked=false;
    boolean isJammed=false;
    boolean isRolled=false;
}

class Cook implements Runnable {
    private BlockingQueue<Pancake> queue;
    public Cook(BlockingQueue<Pancake> queue) {
        this.queue=queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pancake pancake=new Pancake();
                pancake.isCooked=true;
                System.out.println("Pancake cooked");
                queue.put(pancake);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
class Jammer implements Runnable{
    private BlockingQueue<Pancake> inqueue;
    private BlockingQueue<Pancake> outqueue;
    public Jammer(BlockingQueue<Pancake> inqueue,BlockingQueue<Pancake> outqueue) {
        this.inqueue=inqueue;
        this.outqueue=outqueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pancake pancake=inqueue.take();
                pancake.isJammed=true;
                System.out.println("Pancake jammed.");
                outqueue.put(pancake);
                Thread.sleep(1000);
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
class Roller implements Runnable {
    private BlockingQueue<Pancake> queue;
    public Roller(BlockingQueue<Pancake> queue) {
        this.queue=queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pancake pancake=queue.take();
                pancake.isRolled=true;
                System.out.println("Pancake rolled");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
public class MaszynaNalesnikowa{
    public static void main(String[] args) {
        BlockingQueue<Pancake> cookedQue=new LinkedBlockingQueue<>();
        BlockingQueue<Pancake> jammedQue=new LinkedBlockingQueue<>();

        Thread cookThread = new Thread(new Cook(cookedQue));
        Thread jammerThread = new Thread(new Jammer(cookedQue, jammedQue));
        Thread rollerThread = new Thread(new Roller(jammedQue));

        cookThread.start();
        jammerThread.start();
        rollerThread.start();
    }
}