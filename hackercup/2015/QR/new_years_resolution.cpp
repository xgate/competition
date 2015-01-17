#include <iostream>

using namespace std;

typedef struct {
	bool visited;
	int p, c, f;
} Node;

int N;

int compare(Node limit, Node sum)
{
	if (limit.p == sum.p && limit.c == sum.c && limit.f == sum.f) return 0;
	if (limit.p < sum.p || limit.c < sum.c || limit.f < sum.f) return 1;
	return -1;
}

Node add(Node n1, Node n2)
{
	Node sum;

	sum.p = n1.p + n2.p;
	sum.c = n1.c + n2.c;
	sum.f = n1.f + n2.f;

	return sum;
}

Node sub(Node n1, Node n2)
{
	Node diff;

	diff.p = n1.p - n2.p;
	diff.c = n1.c - n2.c;
	diff.f = n1.f - n2.f;

	return diff;
}

bool solve(Node *nodes, Node limit, Node sum)
{
	int val = compare(limit, sum);

	if (val == 0) {
		return true;
	} else if (val > 0) {
		return false;
	}

	for(int i=0; i<N; i++) {
		if (!nodes[i].visited) {
			nodes[i].visited = true;
			
			sum = add(sum, nodes[i]);
			if (solve(nodes, limit, sum) == true) return true;
			sum = sub(sum, nodes[i]);

			nodes[i].visited = false;
		}
	}

	return false;
}

int main()
{
	int T;
	Node limit, nodes[50];

	cin >> T;

	for(int t=0; t<T; t++) {
		
		cin >> limit.p >> limit.c >> limit.f;
		
		cin >> N;

		for(int i=0; i<N; i++) {
			cin >> nodes[i].p >> nodes[i].c >> nodes[i].f;
			nodes[i].visited = false;
		}

		Node sum = {0,0,0};
		if (solve(nodes, limit, sum) == true) 
			cout << "Case #" << t+1 << ": yes" << endl;
		else 
			cout << "Case #" << t+1 << ": no" << endl;
	}
	return 0;
}
