#include <iostream>
#include <cmath>

#define _USE_MATH_DEFINES

using namespace std;

int CENTER_X = 50;
int CENTER_Y = 50;
double DIST = 50.0;

double distance(int x, int y) {
    int dx = x - CENTER_X;
    int dy = y - CENTER_Y;
    return sqrt((double)(dx*dx + dy*dy));
}

double angle(int x, int y) {
    int dx = x - CENTER_X;
    int dy = y - CENTER_Y;
    return atan2(dy, dx) * (180 / M_PI);
}

bool inRange(int p, int x, int y) {
    double degree = p * 3.6;
    double ang = angle(x, y);
    
    if (ang < 0) ang = 90.0 - ang;
    else if (ang >= 0.0 && ang <= 90.0) ang = 90.0 - ang;
    else ang = 270.0 + (180.0 - ang);

    return (ang <= degree);
}

int main()
{
    int T;

    cin >> T;
    for(int i=1; i<=T; i++) {
        int p, x, y;
        cin >> p >> x >> y;
        cout << "Case #" << i << ": ";
        if (distance(x, y) <= DIST && inRange(p, x, y)) {
            cout << "black" << endl;
        } else {
            cout << "white" << endl;
        }
    }
    return 0;
}
