#include <iostream>

using namespace std;

const int MAX_LEN = 1000000;

typedef struct Node {
	int childCount;
	bool end;
	Node *children[26];
} Node;

int toInt(char c)
{
	return (c - 'a');
}

int find(Node *node, char *item)
{
	int len = strlen(item);
	
	for(int i=0; i<len; i++) {
		int index = toInt(item[i]);
		node = node->children[index];
		if (node->childCount <= 1 && !node->end) 
			return i+1;
	}

	return len;
}

void insert(Node *node, char *item)
{
	int len = strlen(item);

	for(int i=0; i<len; i++) {

		int index = toInt(item[i]);
		
		if (node->children[index] == 0) {
			Node *newNode = new Node();
			node->childCount++;
			node->children[index] = newNode;
			node = newNode;
		} else {
			node->childCount++;
			node = node->children[index];
		}

		if (i == len-1)
			node->end = true;
	}
}

int main()
{
	int T;

	cin >> T;

	char word[MAX_LEN+1];

	for(int t=1; t<=T; t++) {
		
		int N, sum = 0;
		
		cin >> N;

		Node *root = new Node();

		for(int i=0; i<N; i++) {
			cin >> word;

			insert(root, word);
			int count = find(root, word);
			sum += count;
		}

		cout << "Case #" << t << ": " << sum << endl;
	}

	return 0;
}
