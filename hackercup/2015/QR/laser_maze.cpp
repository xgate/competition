#include <iostream>
#include <queue>
#include <climits>

using namespace std;

typedef struct {
	int mazeNo;
	int i, j;
	int visitCount;
} Node;

const int MAX_ROW = 100;
const int MAX_COL = 100;

int M, N;
char maze[MAX_ROW][MAX_COL];
char snapshot[4][MAX_ROW][MAX_COL];

char rotateTurret(int i, int j, char turret)
{
	switch(turret)
	{
		case '^': return '>';
		case '>': return 'v';
		case 'v': return '<';
		case '<': return '^';
		default:
			break;
	}

	return turret;
}

void getIncrByTurret(int &i, int &j, char turret)
{
	switch(turret)
	{
		case '^': 
			i = -1; j = 0;
			break;
		case '>': 
			i = 0; j = 1;
			break;
		case 'v': 
			i = 1; j = 0;
			break;
		case '<': 
			i = 0; j = -1;
			break;
		default:
			break;	
	}
}

bool isTurret(char value)
{
	return (value == '^' || value == '>' || value == 'v' || value == '<');
}

bool canThrough(int i, int j)
{
	if (maze[i][j] == '#' || isTurret(maze[i][j])) 
		return false;

	if (i < 0 || i >= M) return false;
	if (j < 0 || j >= N) return false;

	return true;
}

void shoot(int i, int j, int no)
{
	int pi, pj;
	
	getIncrByTurret(pi, pj, maze[i][j]);
	
	i += pi; j += pj;

	while(canThrough(i, j))
	{
		snapshot[no][i][j] = 'x';
		i += pi; j += pj;
	} 
}

void rotateTurretsAndShoot(int no)
{
	for(int i=0; i<M; i++) {
		for(int j=0; j<N; j++) {
			if (isTurret(maze[i][j])) {
				maze[i][j] = rotateTurret(i, j, maze[i][j]);
				shoot(i, j, no);
			}
		}
	}
}

void makeSnapshot()
{
	for(int i=0; i<4; i++) {
		rotateTurretsAndShoot(i);
	}
}

bool findGoal(Node node)
{
	if (maze[node.i][node.j] == 'G' && snapshot[node.mazeNo][node.i][node.j] != 'x')
		return true;

	return false;
}

void getStartPos(int &si, int &sj)
{
	for(int i=0; i<M; i++) {
		for(int j=0; j<N; j++) {
			if (maze[i][j] == 'S') {
				si = i; sj = j;
				return ;
			}
		}
	}
}

bool canMove(int i, int j, int no)
{
	if (i < 0 || i >= M) return false;
	if (j < 0 || j >= N) return false;
	
	if (isTurret(maze[i][j]) || maze[i][j] == '#') return false;

	char val = snapshot[no][i][j];
	
	if (val == '*' || val == 'x')
		return false;

	return true;
}

int solve()
{
	queue<Node> que;

	// up, right, down, left
	int incr[4][2] = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
	int minCount = INT_MAX;
	
	int si, sj;
	getStartPos(si, sj);

	Node initNode = {-1, si, sj, 0};	
	que.push(initNode);

	while(!que.empty()) 
	{
		Node node = que.front(); que.pop();

		for(int i=0; i<4; i++) {

			int newMazeNo = (node.mazeNo+1)%4;
			int newi = node.i+incr[i][0];
			int newj = node.j+incr[i][1];
			
			if(canMove(newi, newj, newMazeNo)) {
				Node newNode = {newMazeNo, newi, newj, node.visitCount+1};
				que.push(newNode);
				snapshot[newMazeNo][newi][newj] = '*';
			}
		}

		if (findGoal(node)) {
			minCount = (minCount < node.visitCount ? minCount : node.visitCount);
		}
	}

	return minCount;
}

int main()
{
	int T;

	cin >> T;

	for(int t=1; t<=T; t++) {
		
		cin >> M >> N;

		for(int i=0; i<M; i++)
			cin >> maze[i];

		makeSnapshot();
		int res = solve();
		cout << "Case #" << t << ": " << (res < INT_MAX ? to_string(res) : "impossible") << endl;

		memset(snapshot, 0, sizeof(snapshot));
		memset(maze, 0, sizeof(maze));
	}

	return 0;
}
