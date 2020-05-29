#include <iostream>
#include <algorithm>
using namespace std;

const int N = 4;
int l[N] = {4, 7, 10, 3/*, 5, 2, 1, 6, 3, 8*/};
int sumaCortes[N];


int minCortes_pd();

int main(){

    int suma = l[0];
    sumaCortes[0] = l[0];
    for(int i = 1; i <N; ++i){
        suma += l[i];
        sumaCortes[i] = suma;
    }

    minCortes_pd();

    return 0;
}



int minCortes_pd(){

    int cortes[N][N];
    int mejorCorte[N][N];
    for(int i = 0; i < N; ++i){
        cortes[i][i+1] = l[i] + l[i+1];
        cortes[i][i] = 0;
        mejorCorte[i][i+1] = i;
        mejorCorte[i][i] = -1;
    }

    for(int d = 2; d < N; ++d){
        for(int i = 0; i < (N -d); ++i){
            int j = i + d;
            cortes[i][j] = __INT_MAX__ ;
            for(int k = i; k < j; ++k){
                int temp = cortes[i][k] + cortes[k+1][j] + sumaCortes[j];
                if(temp < cortes[i][j]){
                    cortes[i][j] = temp;
                    mejorCorte[i][j] = k;
                }
            }
        }
    }
    
    return cortes[0][N-1];
}