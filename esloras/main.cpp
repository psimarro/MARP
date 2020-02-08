#include <iostream>
#include <algorithm>
using namespace std;



/*
Ejercicio 3: Debido a la gran afluencia de público en pequeñas barcas a la Copa del América, las
autoridades portuarias han habilitado un embarcadero con anchura suficiente para que atraquen
dos filas de embarcaciones, de longitud L cada una. Para amarrar, las embarcaciones esperan a la
entrada del embarcadero en una única fila y una lancha del servicio de guardacostas se encarga de
dirigirlas a la fila de la izquierda o la de la derecha. Cada embarcación tiene una eslora determinada
ei
, de tal forma que sum(i=1 hasta N)[ei] ≥ 2L, siendo N el número de embarcaciones en la cola. Diseñar un
algoritmo que dada una cola de N embarcaciones ayude a los guardacostas a maximizar el número
de estas que pueden amarrar en el embarcadero.

*/


int maxBarcos(int[], int, int, int); //Recursión
int maxBarcos_pd(int E[]); //Programa dinámico
const int N = 10; //Número de embarcaciones
const int L = 20; //Tamaño de cada embarcadero
int E[10] = {9, 3, 4, 12, 15, 2, 7, 8, 10, 5 }; //Lista de esloras
int embarcadero[N]; //Solución: embarcadero[i] = 0 si no se coloca barco i, 
//1 o 2 en otro caso

int main(){
    
    cout << maxBarcos_pd(E) << "\n";
    //int barcos = maxBarcos(E, 9, 20, 20);
    //cout << barcos << "\n";

    for(int i = 0; i < N; ++i){
        if(embarcadero[i] == 0) cout << "e" << i << ": --\n";
        else cout << "e" << i << ": embarcadero " << embarcadero[i] << "\n";
    }
    return 0;
}

/*

//DISEÑO RECURSIVO 

*/
int maxBarcos(int E[], int i, int l1, int l2){

    //Casos base
    //Si ya no hay esloras que procesar
    if(i == -1) return 0;
    //Si ambos embarcaderos están completos
    else if(l1 == 0 && l2 == 0) return 0;

    //Casos recursivos
    //eslora[i] no cabe en ningún embarcadero
    if(l1 - E[i] < 0 && l2 - E[i] < 0){
        return maxBarcos(E, i-1, l1, l2);
    }
    //Eslora[i] no cabe en embarcadero 1 pero si en embacadero 2
    else if(l1 - E[i] < 0){
        return max(maxBarcos(E, i-1, l1, l2-E[i]) + 1, maxBarcos(E, i-1, l1, l2));
    }
    //Eslora[i] no cabe en embarcadero 2 pero si en embacadero 1
    else if(l2 - E[i] < 0){
        return max(maxBarcos(E, i-1, l1-E[i], l2) + 1, maxBarcos(E, i-1, l1, l2));
    }
    else{
       //Eslora[i] cabe en ambos embarcaderos
       return max(max(maxBarcos(E, i-1, l1-E[i], l2) + 1, maxBarcos(E, i-1, l1, l2-E[i]) + 1),
       maxBarcos(E, i-1, l1, l2)); 
    }
}


/*

PROGRAMACIÓN DINÁMICA

Coste en tiempo: 2NL² + N ~~ O(NL²)
Coste en espacio: N + N*L² = (1 + L²)N ~~ O(NL²)
*/
int maxBarcos_pd(int E[]){

    //Inicializamos la tabla
    int t[N+1][L+1][L+1];
    for(int k = 0; k < N+1; ++k){
        for(int i = 0; i < L+1; ++i){
            for (int j = 0; j < L+1; ++j){
                //Casos base
                if(k == 0) t[k][i][j] = 0; 
                else if(i == 0 && j == 0) t[k][i][j] = 0;
            }
        }
    }
    
    //A partir del primer "piso" rellanamos la tabla
    for(int k = 1; k < N+1; ++k){
        for(int i = 0; i < L+1; ++i){
            for(int j = 0; j < L+1; ++j){
                if(i != 0 && j != 0){ //Miramos los problemas que no sean caso base
                    //Si barco k (E[k-1]) no cabe en ningún embarcadero
                    if(i - E[k-1] < 0 && j - E[k-1] < 0){
                        t[k][i][j] = t[k-1][i][j];
                    }
                    //Si barco k (E[k-1]) mide más que el embarcadero i
                    else if(i-E[k-1] < 0){
                        int sinCoger = t[k-1][i][j];
                        int cogiendo = t[k-1][i][j-E[k-1]];
                        t[k][i][j] = max(cogiendo + 1, sinCoger);
                    }
                    //Si barco k (E[k-1]) mide más que el embacadero j
                    else if(j-E[k-1] < 0){
                        int sinCoger = t[k-1][i][j];
                        int cogiendo = t[k-1][i-E[k-1]][j];
                        t[k][i][j] = max(cogiendo+ 1, sinCoger);
                    }
                    //Si ambos embarcaderos están disponibles (emb i, j > barco k(E[k-1]))
                    else{
                        int sinCoger = t[k-1][i][j];
                        int cogiendoL1 = t[k-1][i-E[k-1]][j];
                        int cogiendoL2 = t[k-1][i][j-E[k-1]];
                        t[k][i][j] = max(max(cogiendoL1+ 1, cogiendoL2 + 1), sinCoger);
                    }
                }
            }
        }
    }

    //Recuperamos la solución
    //int embarcaderos[N];
    int j = L, i = L, k = N;
    while(k != 0){
        int pos = t[k][i][j];
        int emb1 = -1, emb2 = -1, noEmb;

        if(i-E[k-1] > 0) emb1 = t[k-1][i-E[k-1]][j];
        if(j-E[k-1] > 0) emb2 = t[k-1][i][j-E[k-1]];
        noEmb = t[k-1][i][j];

        if(pos == emb1 + 1){
            embarcadero[k-1] = 1;
            i = i - E[k-1];
        }
        else if(pos == emb2 + 1){
            embarcadero[k-1] = 2;
            j = j - E[k-1];
        }
        else{
            embarcadero[k-1] = 0;
        }
        k--;
    }
    return t[N][L][L];
}