class Contador {
    private int contador;

    public Contador(){
        this.contador = 0;
    }

    public void inc(){
        this.contador++;
    }

    public int get(){
        return this.contador;
    }
}

class T implements Runnable{
    private int[] numeros;
    private Contador contador;
    private int indice;
    private int nThreads;

    public T(Contador contador, int[] numeros, int indice,int nThreads){
        this.contador = contador;
        this.numeros = numeros;
        this.indice = indice;
        this.nThreads = nThreads;
    }
    
    public void run(){
        for(int i = indice; i < this.numeros.length; i+=this.nThreads)
            if(this.numeros[i]%2 == 0)
                this.contador.inc();
    }
}

class Lab7{
    static final int N = 3;
    static final int[] numeros = {1, 2, 3, 1, 1, 1, 6,1, 4};
    
    public static void main(String[] args){
        Thread[] threads = new Thread[N];
        Contador contador = new Contador();
        
        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(new T(contador, numeros, i, N));
        }

        for (int i=0; i<threads.length; i++) {
         threads[i].start();
        }

        for (int i=0; i<threads.length; i++) {
            try { 
                threads[i].join();
            } 
            catch (InterruptedException e) { return; }
        } 

        System.out.println("Valor de contador = " + contador.get()); 
    }
}