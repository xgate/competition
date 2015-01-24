#include <iostream>
#include <cstdio>

using namespace std;

int dp[2001][2001];
int loseFinal;

int modulo(int i, int j, int incr)
{
	return ((dp[i][j] + incr) % 1000000007);
}

int stressFree(int win, int lose)
{
	if (dp[win][lose]) 
		return modulo(win, lose, 0);

	if (win == 0 || lose == 0) 
		return modulo(win, lose, 1);

	if (win <= lose)
		return modulo(win, lose, 0);

	dp[win][lose] += stressFree(win-1, lose);
	dp[win][lose] += stressFree(win, lose-1);

	return modulo(win, lose, 0);
}

int stressFull(int win, int lose)
{
	if (dp[win][lose]) 
		return modulo(win, lose, 0);

	if (win > lose && lose != loseFinal)
		return modulo(win, lose, 0);

	if (win == 0 || lose == 0) {
		return modulo(win, lose, 1);
	}

	dp[win][lose] += stressFull(win-1, lose);
	dp[win][lose] += stressFull(win, lose-1);
	
	return modulo(win, lose, 0);
}

int main()
{
	int T;

	cin >> T;

	for(int t=1; t<=T; t++) {
		char score[16];
		cin >> score;

		int win, lose;

		sscanf(score, "%d-%d", &win, &lose);

		int r1 = stressFree(win, lose);
		memset(dp, 0, sizeof(dp));

		loseFinal = lose;
		int r2 = stressFull(win, lose);
		memset(dp, 0, sizeof(dp));

		cout << "Case #" << t << ": " << r1 << " " << r2 << endl;
	}
}