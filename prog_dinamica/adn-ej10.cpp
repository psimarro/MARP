#include <iostream>
#include <algorithm>
#include <map>
#include <string>
using namespace std;

const int TAM_ALFA = 5;
const int N = 5;
const int M = 7;
const int x[N] = {3, 3, 2, 0, 1}; //T, T, G, A, C
const int y[M] = {2, 0, 0, 1, 3, 1, 2}; //G, A, A, C, T, C, G
const char alfabeto[TAM_ALFA+1] = {'A', 'C', 'G', 'T', '-'};



/*
    Tabla de puntuaciones {A, C, G, T, -}
*/
const int P[TAM_ALFA][TAM_ALFA] ={
    15, 18, 9, 10, 16, 
    6, 28, 12, 30, 3,
    8, 11, 5, 2, 22, 
    27, 7, 4, 29, 25, 
    21, 23, 14, 26, 17
};

string sol1, sol2;

int adn_pd();

int main(){
    cout << adn_pd() << "\n";
    cout << sol1 << "\n" << sol2 << "\n";

    system("pause");
    return 0;
}


/*
    RECURSION

    adn(i, j) = maxima puntuacion emparejando las subcadenas de 1 a x_i y 1 a y_j

    Casos base:
    adn(0, 0) = 0
    adn(i, 0) = P[x_i, -]
    adn(0, j) = P[-, y_j]

    
    Casos recursivos:
    adn(i, j) = max(adn(i-1, j-1) + P[x_i, y_j], adn(i-1, j) +  P[x_i, -], adn(i, j-1) + P[-, y_j])


*/
int adn_pd(){

    int tabla[N+1][M+1];
    tabla[0][0] = 0;
    for(int j = 1; j < M+1; ++j)
        tabla[0][j] = P[4][y[j-1]];
    for(int i = 1; i < N+1; ++i)
        tabla[i][0] = P[x[i-1]][4];

    //Rellenamos la tabla
    int parejas = 0;
    for(int i = 1; i < N+1; ++i){
        for(int j = 1; j < M+1; ++j){
            //Solo usamos una letra para el emparejamiento
            int maxPunt = max(tabla[i-1][j] + P[x[i-1]][4], tabla[i][j-1] + P[4][y[j-1]]);
            maxPunt = max(maxPunt, tabla[i-1][j-1] + P[x[i-1]][y[j-1]]);
            tabla[i][j] = maxPunt;
            parejas++;
        }
    }

    //Recuperamos la solución;
    int i = N, j = M;
    
    while(i != 0 || j != 0){
        if(tabla[i][j] == tabla[i-1][j-1] + P[x[i-1]][y[j-1]]){
            sol1.insert(sol1.begin(), alfabeto[x[i-1]]);
            sol2.insert(sol2.begin(), alfabeto[y[j-1]]);
            i--;
            j--;
        }
        else if(tabla[i][j] == tabla[i-1][j] + P[x[i-1]][4]){
            sol1.insert(sol1.begin(), alfabeto[x[i-1]]);
            sol2.insert(sol2.begin(), alfabeto[4]);
            i--;
        }
        else{
            sol1.insert(sol1.begin(), alfabeto[4]);
            sol2.insert(sol2.begin(), alfabeto[y[j-1]]);
            j--;
        }
    }

    return tabla[N][M];
}