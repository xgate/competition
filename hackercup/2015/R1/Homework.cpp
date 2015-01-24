#include <iostream>
#include <vector>

using namespace std;

const int MAX_PRIMES = 10000000;

int numbers[MAX_PRIMES+1];
vector<int> primes;

void makePrimCache()
{
	for(int i=2; i<=MAX_PRIMES; i++) {
		for(int j=1; j<=MAX_PRIMES; j++) {
			if (i*j > MAX_PRIMES) break;
			numbers[i*j]++;
		}
	}

	for(int i=2; i<=MAX_PRIMES; i++)
		if (numbers[i] == 1) 
			primes.push_back(i);

	memset(numbers, 0, sizeof(numbers));

	for(int i=0; i<primes.size(); i++) {
		int num = primes[i];
		int sum = num;

		while(sum <= MAX_PRIMES) {
			numbers[sum]++;
			sum += num;
		}
	}
}

int main()
{
	makePrimCache();

	int T;
	cin >> T;

	for(int t=1; t<=T; t++)
	{
		int A, B, K;

		cin >> A >> B >> K;

		int count = 0;

		for(int i=A; i<=B; i++) {
			if (numbers[i] == K)
				count++;
		}

		cout << "Case #" << t << ": " << count << endl;
	}
}
