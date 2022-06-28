#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>

//gcc lab9.c -o lab9 -Wall -lpthread

#define NTHREADS  5
int x = 0;
sem_t condt2, condt3, excluMut;

/* Thread A */
void *A (void *t) {
  int boba1, boba2;
  /* faz alguma coisa pra gastar tempo... */
  boba1=10000; boba2=-10000; while (boba2 < boba1) boba2++;
  printf("Seja bem-vindo!\n");
  x++;
  sem_post(&condt2);
  sem_post(&condt2);
  sem_post(&condt2);
  pthread_exit(NULL);
}

/* Thread B */
void *B (void *t) {
  sem_wait(&condt2);
  printf("Fique a vontade.\n");
  sem_wait(&excluMut);
  x++;
  if(x > 3)
    sem_post(&condt3);
  sem_post(&excluMut);
  pthread_exit(NULL);
}

/* Thread C */
void *C (void *t) {
  sem_wait(&condt2);
  printf("Sente-se por favor.\n");
  sem_wait(&excluMut);
    x++;
  if(x > 3)
    sem_post(&condt3);
  sem_post(&excluMut);
  pthread_exit(NULL);
}

/* Thread D */
void *D (void *t) {
  sem_wait(&condt2);
  printf("Aceita um copo d’agua?.\n");
  sem_wait(&excluMut);
    x++;
  if(x > 3)
    sem_post(&condt3);
  sem_post(&excluMut);
  pthread_exit(NULL);
}

/* Thread E */
void *E (void *t) {
  sem_wait(&condt3);
  printf("Volte sempre!.\n");
  pthread_exit(NULL);
}

/* Funcao principal */
int main(int argc, char *argv[]) {
  int i; 
  pthread_t threads[NTHREADS];

  /* Inicilaiza os semaforos */
  sem_init(&condt2, 0, 0);
  sem_init(&condt3, 0, 0);
  sem_init(&excluMut, 0, 1);

  /* Cria as threads */
  pthread_create(&threads[4], NULL, A, NULL);
  pthread_create(&threads[3], NULL, B, NULL);
  pthread_create(&threads[2], NULL, C, NULL);
  pthread_create(&threads[1], NULL, D, NULL);
  pthread_create(&threads[0], NULL, E, NULL);

  /* Espera todas as threads completarem */
  for (i = 0; i < NTHREADS; i++) {
    pthread_join(threads[i], NULL);
  }
  printf ("\nFIM\n");

  /* Desaloca variaveis e termina */
}