#ifndef POINT_HPP
#define POINT_HPP

#include "common.hpp"

#include <cmath>
#include <ostream>

namespace gogui {

class Point : public GeoObject
{
public:
    double x, y;

    constexpr Point(const double & x1, const double & y1)
    : x(x1), y(y1)
    {}

    Point() : x(0.0) , y(0.0) {}

    friend std::ostream& operator<< (std::ostream &out, Point &point);

    bool operator ==(const Point & that) const {
        return compareDouble(this->x, that.x) && compareDouble(this->y, that.y);
    }

    bool operator !=(const Point & that) const {
        return !(*this == that);
    }

    Point& operator=(const Point &rhs) {
        // Only do assignment if RHS is a different object from this.
        if (this != &rhs) {
          // Deallocate, allocate new space, copy values...
          this->x = rhs.x;
          this->y = rhs.y;
        }
        return *this;
    }


    bool operator <(const Point & that) const {
        if(x != that.x)
            return x < that.x;
        return y < that.y;
    }

    double distance (const Point & p) const {
        return sqrt((x-p.x)*(x-p.x) + (y-p.y)*(y-p.y));
    }

    double angleBetweenXAxis(const Point& p) const
    {
        double oposite = p.y - y;
        double adjacent = p.x - x;

        return atan2(oposite, adjacent);
    }

    static double inline orient2d(Point a, Point b, Point c) {
         return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x));
    }

    template<class T>
    double distance (const T& that) const {
        return that.distance(*this);
    }
};

std::ostream& operator<< (std::ostream& out, Point &point)
{
    out << "(" << point.x << ", " <<
        point.y << ")" << std::endl;

    return out;
}

}
#endif //POINT_HPP
