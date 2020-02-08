#include <iostream>
#include <algorithm>
using namespace std;

/*
Ejercicio 2 Dado un vector V [1..n] de números enteros mayores que 0, 
diseña un algoritmo que encuentre un subconjunto de suma C > 0 de cardinal mínimo.
*/

const int C = 10; //Suma C
const int N = 10; //Número de elementos del vector
int V[10] = {2, 3, 3, 1, 6, 14, 8, 11, 1, 5}; //Vector de números
bool sol[N]; //Solución: true si V[i] se utiliza para la suma, false si no 
int suma(int[], int, int); //Diseño recursivo
int suma_pd(int []); //Diseño con programación dinámica

int main(){

    /*int cardinal = suma(V, 9, C);
    cout << cardinal << "\n";*/
    int cardinal = suma_pd(V);
    cout << "Minimo cardinal:" << cardinal << "\n";

    cout << "Sol: {";
    int i = 0, cont = 0;
    while(i < N && cont != cardinal){
        if(sol[i]){
            cout << V[i];
            cont++;
            if(cont != cardinal) cout << ", ";
        }
        ++i;
    }
    cout << "}\n";
    return 0;
}

/*

DISEÑO RECURSIVO

*/
int suma(int V[], int i, int C){
    //CASOS BASE
    //Si no hay números que procesar
    if(i == -1) return __INT_MAX__ -100;
    //Para sumar 0 no se necesitan valores
    else if(C == 0) return 0;

    //Casos recursivos
    //Si el número es mayor que la suma
    if(C - V[i] < 0){
        return suma(V, i-1, C);
    }
    //Si el número es más pequeño 
    else{
        return min(suma(V, i-1, C-V[i]) + 1, suma(V, i-1, C));
    }
}

/*

PROGRAMCIÓN DINÁMICA

*/
int suma_pd(int V[]){


    int t[N+1][C+1];
    //Inicializamos la tabla
    for(int i = 0; i < N+1; ++i) t[0][i] = __INT_MAX__ - 100;
    for(int j = 0; j < C+1; ++j) t[j][0] = 0;

    //Rellenamos la tabla
    for(int i = 1; i < N+1; ++i){
        for(int j = 1; j < C+1; ++j){
            if(j - V[i-1] < 0) t[i][j] = t[i-1][j];
            else{
                t[i][j] = min(t[i-1][j], t[i-1][j-V[i-1]] + 1);
            }
        }
    }

    //Recuperamos la solución
    int i = N, j = C;
    while(i != 0){
        int cogiendo =__INT_MAX__ - 100;
        int sinCoger = t[i-1][j];
        int pos = t[i][j];
        if(j - V[i-1] >= 0){
            cogiendo = t[i-1][j-V[i-1]];
        }

        if(pos == cogiendo + 1){
            sol[i-1] = true;
            j = j - V[i-1];
        }
        else{
            sol[i-1] = false;
        }
        i--;
    }

    //Delvomes el resultado del cardinal
    return t[N][C];
}