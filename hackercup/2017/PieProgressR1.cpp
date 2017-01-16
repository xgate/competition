#include <iostream>
#include <queue>
#include <vector>
#include <cstdlib>

using namespace std;

const int MAX_N = 300;
const int MAX_M = 300;

int compare (const void * a, const void * b)
{
  return ( *(int*)a - *(int*)b );
}

void sortCosts(int (&array)[MAX_N][MAX_M], int rows, int cols)
{
    for(int i=0; i<rows; i++) {
        qsort(array[i], cols, sizeof(int), compare);
    }
}

int main()
{
    int T;
    cin >> T;

    for(int t=1; t<=T; t++) {
        int N, M;
        int costs[MAX_N][MAX_M];

        cin >> N >> M;
        for(int i=0; i<N; i++) {
            for(int j=0; j<M; j++) {
                cin >> costs[i][j];
            }
        }

        sortCosts(costs, N, M);

        int solution = 0;
        priority_queue<int, vector<int>, greater<int> > costQ;

        for(int i=0; i<N; i++) {
            int acc = 0;
            int before = 0;
            for(int j=0; j<M; j++) {
                int tax = (j+1)*(j+1);
                acc += costs[i][j];
                int totalCost = acc + tax;
                int increased = totalCost - before;
                before = totalCost;
                costQ.push(increased);
            }
            solution += costQ.top();
            costQ.pop();
        }

        cout << "Case #" << t << ": " << solution << endl;
    }

    return 0;
}
