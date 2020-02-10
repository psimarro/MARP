#include <iostream>
#include <algorithm>
using namespace std;

bool esPosibleVarillas(int[], int, int);
int numCombinaciones(int[], int, int);
int minVarillas(int v[], int i, int j);
int minCoste(int v[], int costes[], int i, int j);

int main() {

	int v[5] = { 4, 7, 9, 2, 1 };
	int costes[5] = { 2, 3, 10, 1, 5 };
	int n = 5;
	int L = 10;

	cout << minVarillas(v, 5, 10) << "\n";

	system("pause");
	return 0;
}

bool esPosibleVarillas(int v[], int i, int j) {

	if (i == -1 && j == 0) return true;
	else if(i == -1 && j > 0) return false;

	if (j - v[i] >= 0)
		return esPosibleVarillas(v, i - 1, j) || esPosibleVarillas(v, i - 1, j - v[i]);
	else
		return esPosibleVarillas(v, i - 1, j);
}

int numCombinaciones(int v[], int i, int j) {

	if (i == -1 && j > 0) return 0;
	else if (i == -1 && j == 0) return 1;

	if (j - v[i] >= 0)
		return numCombinaciones(v, i - 1, j) + numCombinaciones(v, i - 1, j - v[i]);
	else
		return numCombinaciones(v, i - 1, j);
}

int minVarillas(int v[], int i, int j) {

	if (i == -1 && j > 0) return INT_MAX - 1;
	else if (i == -1 && j == 0) return 0;

	if (j - v[i] >= 0)
		return min(minVarillas(v, i - 1, j - v[i]) + 1, minVarillas(v, i - 1, j));
	else
		return minVarillas(v, i - 1, j);
}

int minCoste(int v[], int costes[], int i, int j) {
	/*if (i == -1 && j != 0) return INT_MAX - 1000;
	else if (i == -1 && j == 0) return 0;

	return min(minCoste(v, costes, i-1, j - v[i]) + costes[i], minCoste(v, costes, i-1, j));*/

	if (i == -1 && j > 0) return INT_MAX - 100;
	else if (i == -1 && j == 0) return 0;

	if (j - v[i] >= 0) {
		return  min(minCoste(v, costes, i - 1, j - v[i]) + costes[i], minCoste(v, costes, i - 1, j));
	}
	else return minCoste(v, costes, i - 1, j);
}


