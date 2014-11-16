#ifndef POLYGON_H
#define POLYGON_H
#define PI 3.141592653
#define EPSILON 1.0e-12
#include "point.hpp"
#include "line.hpp"
#include <cmath>
#include <iostream>
#include <vector>

namespace gogui {

class Polygon : public GeoObject
{

public:
    friend std::ostream& operator<< (std::ostream &out, Point &point);

    std::vector<Point> points;
    std::vector<Line> lines;

    Polygon(std::vector<Point> points) {
        this->points = points;

        for(int i = 1; i<points.size(); i++) {
            lines.push_back(Line(points[i-1], points[i]));
        }
    }

    Polygon(std::vector<Line> lines) {
        this->lines = lines;
    }
};

std::ostream& operator<< (std::ostream& out, Polygon &polygon)
{
    // TODO: implement

    return out;
}

}

#endif // POLYGON_H

