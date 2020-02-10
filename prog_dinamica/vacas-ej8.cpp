#include <iostream>
#include <algorithm>
using namespace std;

//int listilla(int[], int, int);
int vacas_pd(int[]);
int vacas_par_impar(int p[]);
const int N = 10;

int main() {

	int p[10] = { 44, 34, 21, 13, 35, 19, 6, 30, 9, 22 };

	//cout << listilla(p, 0, 9) << "\n";
	 cout << vacas_pd(p);
	//vacas_par_impar(p);


	system("pause");
	return 0;
}

/*int listilla(int p[], int i, int j) {

	if (j <= i) return 0;

	int izquierda = 0, derecha = 0;
	if (p[i + 1] > p[j])
		izquierda = p[i] + listilla(p, i + 2, j);
	else
		izquierda = p[i] + listilla(p, i + 1, j - 1);

	if (p[j - 1] > p[i])
		derecha = p[j] + listilla(p, i, j - 2);
	else
		derecha = p[j] + listilla(p, i + 1, j - 1);

	return max(izquierda, derecha);
}*/

int vacas_pd(int p[])
{
	int vacas[N][N];
	for (int i = 0; i <= N - 2; ++i) {
		vacas[i][i + 1] = max(p[i], p[i + 1]);
	}

	for (int d = 3 ; d <= N - 1; d = d + 2) {
		for (int i = 0; i <= N - d; ++i) {
			int j = i + d;
			int como_i, como_j;
			if (p[j] > p[i + 1]) como_i = vacas[i + 1][j - 1];
			else como_i = vacas[i + 2][j];

			if (p[i] > p[j - 1]) como_j = vacas[i + 1][j - 1];
			else como_j = vacas[i][j - 2];

			vacas[i][j] = max(p[i] + como_i, p[j] + como_j);
		}
	}

	return vacas[0][N-1];
}

int vacas_par_impar(int p[]) {

	int impares = 0, pares = 0;

	for (int i = 0; i < N; ++i) {
		if (i % 2 != 0) impares += p[i];
		else pares += p[i];
	}

	int izq = 0, der = N - 1, cuanto = 0;
	while (izq < der) {
		//Come Listilla, hay un numero par de numeros
		if (impares >= pares) {
			//come impar
			if (izq % 2 != 0) {
				cuanto +=  p[izq];
				impares -= p[izq];
				izq++;
			}
			else {
				cuanto += p[der];
				impares -= p[der];
				der--;
			}
		}
		else {
			if (izq % 2 != 0) {
				cuanto += p[der];
				pares -= p[der];
				der--;
			}
			else {
				cuanto += p[izq];
				pares -= p[izq];
				izq++;
			}
		}

		//Come Devoradora
		if (p[izq] >= p[der]) {
			if (izq % 2 != 0) impares -= p[izq];
			else pares -= p[izq];
			izq++;
		}
		else {
			if (der % 2 != 0) impares -= p[der];
			else pares -= p[der];
			der--;
		}
	}

	return cuanto;
}




