#include <iostream>
#include <algorithm>
using namespace std;


int ladrillos(int[], int, int);
int main() {

	int l[10] = { 12, 26, 3, 17, 35, 5, 10, 15, 22, 30 };

	int suma = 0;
	for (int i = 0; i < size(l); ++i)
		suma += l[i];

	cout << ladrillos(l, 10, suma) << "\n";

	system("pause");
	return 0;
}

int ladrillos(int l[], int i, int suma) {

	if (i == -1) return 0;

	return min(suma - ladrillos(l, i-1, suma-l[i]) - l[i], suma - ladrillos(l,i - 1, suma));
}