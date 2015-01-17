#include <iostream>
#include <algorithm>

using namespace std;

int main()
{
	int T;

	cin >> T;

	for(int t=1; t<=T; t++) {
		
		string N;
		cin >> N;

		int len = N.length();
		int min = stoi(N), max = min;

		for(int i=0; i<len; i++) {
			for(int j=i+1; j<len; j++) {
				swap(N[i], N[j]);
				if (N[0] != '0') { 			
					int num = stoi(N);
					min = (num < min ? num : min);
					max = (num > max ? num : max);
				}
				swap(N[i], N[j]);
			}
		}

		cout << "Case #" << t << ": " << min << " " << max << endl;
	}

	return 0;
}