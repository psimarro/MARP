#include <iostream>
#include <algorithm>
using namespace std;



/*Ejercicio 7 Un archipiélago consta de unas cuantas islas y varios puentes que unen ciertos pares de
islas entre sí. Para cada puente (que puede ser de dirección única o doble), además de saber la isla
de origen y la de destino, se conoce su anchura > 0. La anchura de un camino formado por una
sucesión de puentes es la anchura mínima entre las anchuras de los puentes que lo forman. Para
cada isla, se desea saber cuál es el camino de anchura máxima que las une (siempre que exista
alguno).*/

const int N = 5;
int G[N][N] = {
    {0, 20, 0, 3, 9},
    {4, 0, 2, 7, 11},
    {10, 0, 0, 1, 6},
    {0, 17, 16, 0, 0},
    {0, 8,0, 20, 0}
};
int anch[N][N];
int camino[N][N];
void maxAnchura_pd();
void muestraCaminos();

int main(){
    maxAnchura_pd();

    muestraCaminos();
    return 0;
}

void maxAnchura_pd(){
    
    //Inicializamos las tablas
    for(int i = 0; i < N; ++i){
        for(int j = 0; j < N; ++j){
            anch[i][j] = G[i][j];
            if(G[i][j] != 0) camino[i][j] = j;
            else camino[i][j] = -1;
        }
    }

    //Rellenamos la tablas
    for(int k = 0; k < N; k++){
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if(i != j){
                    int temp = min(anch[i][k], anch[k][j]);
                    if(temp > anch[i][j]){
                        anch[i][j] = temp;
                        camino[i][j] = k;
                    }
                }
            }
        }
    }


}

void muestraCaminos(){

    for(int i = 0; i < N; ++i){
        for(int j = 0; j < N; ++j){
            cout << "(" << i << "," << j << ") ";
            if(i != j){
                cout << anch[i][j] << ": ";
                int k = camino[i][j];
                cout << i << ", ";
                while(k != j && k != -1){
                    cout << k;
                    k = camino[k][j];
                    cout << ", ";
                }
                cout << j;
            }
            else{
                cout << "--";
            }
            cout << "\n";
        }
    }
}