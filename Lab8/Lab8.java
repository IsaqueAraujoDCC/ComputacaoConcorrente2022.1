class LE {
  private int leit, escr;
  
  // Construtor
  LE() { 
     this.leit = 0; //leitores lendo (0 ou mais)
     this.escr = 0; //escritor escrevendo (0 ou 1)
  } 
  
  // Entrada para leitores
  public synchronized void EntraLeitor (int id) {
    try { 
      while (this.escr > 0) {
      //if (this.escr > 0) {
         System.out.println ("le.leitorBloqueado("+id+")");
         wait();  //bloqueia pela condicao logica da aplicacao 
      }
      this.leit++;  //registra que ha mais um leitor lendo
      System.out.println ("le.leitorLendo("+id+")");
    } catch (InterruptedException e) { }
  }
  
  // Saida para leitores
  public synchronized void SaiLeitor (int id) {
     this.leit--; //registra que um leitor saiu
     if (this.leit == 0) 
           this.notify(); //libera escritor (caso exista escritor bloqueado)
     System.out.println ("le.leitorSaindo("+id+")");
  }
  
  // Entrada para escritores
  public synchronized void EntraEscritor (int id) {
    try { 
      while ((this.leit > 0) || (this.escr > 0)) {
      //if ((this.leit > 0) || (this.escr > 0)) {
         System.out.println ("le.escritorBloqueado("+id+")");
         wait();  //bloqueia pela condicao logica da aplicacao 
      }
      this.escr++; //registra que ha um escritor escrevendo
      System.out.println ("le.escritorEscrevendo("+id+")");
    } catch (InterruptedException e) { }
  }
  
  // Saida para escritores
  public synchronized void SaiEscritor (int id) {
     this.escr--; //registra que o escritor saiu
     notifyAll(); //libera leitores e escritores (caso existam leitores ou escritores bloqueados)
     System.out.println ("le.escritorSaindo("+id+")");
  }

}

//Class para controlar a variável global
class Global{
    static int global = 0;
    
    public static void incrementa(){
      global++;
    }

    public static void setIdentificador(int n) {
		  System.out.println("#Valor atual: " + global);
		  global = n;
	  }

    public static void ehPar() {
		  System.out.printf("#" + "%d É %s\n", global, global % 2 == 0 ? "par!" : "impar!");
	}
}

class T1 extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo...
  LE monitor; //objeto monitor para coordenar a lógica de execução das threads

  // Construtor
    T1(int id, int delayTime, LE m) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
  }

  // Método executado pela thread
  public void run () {
    try {
      for (;;) {
        this.monitor.EntraEscritor(this.id);
        //Global.global++; 
        Global.incrementa();
        this.monitor.SaiEscritor(this.id); 
        sleep(this.delay); //atraso bobo...
      }
    } catch (InterruptedException e) { return; }
  }
}

class T2 extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo
  LE monitor;//objeto monitor para coordenar a lógica de execução das threads
  // Construtor
  T2 (int id, int delayTime, LE m) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
  }

  // Método executado pela thread
  public void run () {
    try {
      for (;;) {
        this.monitor.EntraLeitor(this.id);
        /*System.out.println(Global.global);
        if(Global.global%2 == 0)
            System.out.println("É par!");
        else
            System.out.println("É impar!");*/
        Global.ehPar();    
        this.monitor.SaiLeitor(this.id);
        sleep(this.delay); 
      }
    } catch (InterruptedException e) { return; }
  }
}

class T3 extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo...
  LE monitor; //objeto monitor para coordenar a lógica de execução das threads
  // Construtor
  T3 (int id, int delayTime, LE m) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
  }

  // Método executado pela thread
  public void run () {
    double j=777777777.7, i;
    try {
      for (;;) {
        this.monitor.EntraLeitor(this.id);
        System.out.println("#Valor atual: " + Global.global);
        for (i=0; i<100000000; i++) {j=j/2;} //...loop bobo para simbolizar o tempo de leitura
        this.monitor.SaiLeitor(this.id);
        this.monitor.EntraEscritor(this.id); 
        //Global.global = this.id;
        Global.setIdentificador(this.id);
        this.monitor.SaiEscritor(this.id); 
        sleep(this.delay); 
      }
    } catch (InterruptedException e) { return; }
  }
}

class Lab8 {
  static final int L = 4;
  static final int E = 3;
  static final int LE = 3;

  public static void main (String[] args) {
    int i;
    LE monitor = new LE();            // Monitor (objeto compartilhado entre leitores e escritores)
    T1[] l = new T1[L];       // Threads leitores
    T2[] e = new T2[E];   // Threads escritores
    T3[] le = new T3[LE];   // Threads leitoras e escritoras

    //inicia o log de saida
    System.out.println ("import verificaLE");
    System.out.println ("le = verificaLE.LE()");
    
    for (i=0; i<L; i++) {
       l[i] = new T1(i+1, (i+1)*500, monitor);
       l[i].start(); 
    }
    for (i=0; i<E; i++) {
       e[i] = new T2(i+1, (i+1)*500, monitor);
       e[i].start(); 
    }
    for (i=0; i<LE; i++) {
       le[i] = new T3(i+1, (i+1)*500, monitor);
       le[i].start(); 
    }
    
  }
}