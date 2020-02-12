#include <iostream>
#include <algorithm>
using namespace std;


/*
Ejercicio 12 Una de las pruebas habituales del concurso Supervivientes consiste en procurarse de
manera autónoma la alimentación. A los concursantes se les facilitan m pequeñas huertas h1, . . . , hm
y tendrán qué decidir cómo invertirán en ellas los n días de trabajo disponibles. Saben el beneficio
b(i, d) que sacarán de cada huerta hi en función del número de días d que trabajen en ella. Obsérvese
que es posible obtener un beneficio b(i, 0) > 0 aunque no se trabaje ningún día en esa huerta.
Encontrar la asignación de días a huertas que maximiza el beneficio total.
*/
const int N = 10;
const int M = 9;

int B[N][M+1] = {
    48, 31, 25, 23, 21, 4, 1, 34, 34, 27,
    26, 31, 4, 21, 27, 29, 42, 14, 33, 18, 
    11, 42, 35, 46, 43, 23, 13, 47, 27, 3, 
    46, 3, 8, 9, 33, 17, 19, 41, 21, 13,
    47, 12, 10, 43, 13, 38, 5, 40, 50, 1, 
    20, 36, 50, 48, 39, 1, 28, 49, 48, 44, 
    16, 39, 15, 35, 27, 6, 0, 24, 38, 8, 
    6, 36, 19, 30, 13, 38, 39, 26, 27, 38, 
    23, 21, 33, 5, 31, 31, 34, 13, 47, 12,
    22, 39, 25, 36, 42, 26, 13, 3, 29, 14
};

int sol[N];

int huertas_pd();

int main(){

    huertas_pd();

    return 0;
}

int huertas_pd(){

    int tabla[N+1][M+1];
    int maximos[N+1][M+1];
    //Inicializamos las tablas
    for(int j = 0; j < M+1; ++j){
        tabla[0][j] = 0;
        maximos[0][j] = 0;
    }
    tabla[1][0] = B[0][0];
    maximos[1][0] = 0;
    for(int i = 2; i < N+1; ++i){
        tabla[i][0] = tabla[i-1][0] + B[i-1][0];
        maximos[i][0] = 0;
    }

    //Rellenamos las tablas
    for(int i = 1; i < N+1; ++i){
        for (int j = 1; j < M+1; ++j){
            maximos[i][j] = 0;
            int beneficioMax = max(B[i-1][0] + tabla[i-1][j], B[i-1][1] + tabla[i-1][0]);
            for(int k = 0; k <= j; ++k){
                if(B[i-1][k] + tabla[i-1][j-k] > beneficioMax){
                    beneficioMax = B[i-1][k] + tabla[i-1][j-k];
                    maximos[i][j] = k;
                }
            }
            tabla[i][j] = beneficioMax;
        }
    }

    //Recuperamos la solución

    int i = N, j = M;
    while(i > -1){
        sol[i-1] = maximos[i][j];
        j -= maximos[i][j];
        i--;
    }

    return tabla[N][M];
}
