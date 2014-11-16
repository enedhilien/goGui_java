#ifndef LINE_H
#define LINE_H
#define PI 3.141592653
#define EPSILON 1.0e-12
#include "point.hpp"
#include <cmath>
#include <iostream>

namespace gogui {

class Line : public GeoObject
{
private:
    struct Parameters {
        double A;           //Ax+By+C=0
        double B;
        double C;
    };

    Parameters parameters;

    double getA() const {
        return point1.y - point2.y;
    }

    double getB() const {
        return point2.x - point1.x;
    }

    double getC() const {
        return (point1.x * point2.y) - (point2.x * point1.y);
    }

    bool compareDouble (const double a, const double b, const double e = EPSILON) const {
        return fabs(a - b) < e;
    }

    // y = ax + b;
    double inline get_a() const {
        return -(this->getA() / this->getB());
    }

    double inline get_b() const {
        return -(this->getC() / this->getB());
    }

public:
    Line(const Point& p1, const Point& p2)
    : point1(p1), point2(p2) {
        parameters.A=getA();
        parameters.B=getB();
        parameters.C=getC();
    }

    Line() {}

    friend std::ostream& operator<< (std::ostream &out, Point &point);

    const Point point1;
    const Point point2;
private:
    bool isEqualToWithOrder(const Line & that) const {
        return (point1 == that.point1) && (point2 == that.point2);
    }

    bool isEqualToReversed(const Line & that) const {
        return (point1 == that.point2) && (point2 == that.point1);
    }

public:
    bool operator ==(const Line & that) const {
        return isEqualToWithOrder(that) || isEqualToReversed(that);
    }

    bool operator !=(const Line & that) const {
        return !(*this == that);
    }

    Line& operator=(const Line &rhs) {
        // Only do assignment if RHS is a different object from this.
        if (this != &rhs) {
          // Deallocate, allocate new space, copy values...
          //this = Line(rhs.point1, rhs.point2);
          *const_cast<Point*> (&this->point1)= rhs.point1;
          *const_cast<Point*> (&this->point2)= rhs.point2;
        }
        return *this;
    }

    bool isParallel (const Line & line) const {
        return compareDouble(parameters.A, line.parameters.A) &&
                            compareDouble(parameters.B, line.parameters.B);
    }

    bool isPerpendicular (const Line & line) const {
        return compareDouble(parameters.A*line.parameters.A, -parameters.B*line.parameters.B);
    }

    double distance (const Line & line) const {
        if(!this->isParallel(line))
            return 0;
        return (fabs(parameters.C - line.parameters.C))/
                (sqrt(parameters.A*parameters.A + parameters.B*parameters.B));
    }

    double distance (const Point & p) const {
        return fabs(parameters.A*p.x + parameters.B*p.y + parameters.C)/
                        sqrt(parameters.A*parameters.A + parameters.B*parameters.B);
    }

    double angleBetweenLines (const Line & line) const {
        if(isPerpendicular(line)) return PI/2;
        return atan((parameters.A*line.parameters.B - line.parameters.A*parameters.B)/
                    (parameters.A*line.parameters.A + parameters.B*line.parameters.B));
    }

    double angleBetweenLines2(const Line & line) const {
        /*Point top = line.getTopPoint();
        Point bottom = line.getBottomPoint();

        Point this_top = getTopPoint();
        Point this_bottom = getBottomPoint();&\*/

        double angle = atan2(point2.y - point1.y, point2.x - point1.x) - atan2(line.point2.y - line.point1.y, line.point2.x - line.point1.x);
        if (angle < 0) {
            angle += 2 * M_PI;
        }
        return angle;
    }

    const Point getLeftPoint()  {
        if (point1.x < point2.x) {
            return point1;
        } else {
            return point2;
        }
    }


    const Point getRightPoint() {
        if (point1.x < point2.x) {
            return point2;
        } else {
            return point1;
        }
    }


    const Point getBottomPoint() {
        if (point1.y < point2.y) {
            return point1;
        } else {
            return point2;
        }
    }

    const Point getTopPoint() {
        if (point1.y < point2.y) {
            return point2;
        } else {
            return point1;
        }
    }

    bool containsPoint(const Point& point) {
        Point left = getLeftPoint();
        Point right = getRightPoint();
        Point top = getTopPoint();
        Point bottom = getBottomPoint();

        if(left.x < point.x && point.x < right.x && bottom.y < point.y && point.y < top.y) {
            float a = get_a();
            float b = get_b();
            if (compareDouble(point.y, a*point.x+b, 1.0e-3))
            {
                return true;
            }
        }
        return false;
    }

    Point intersectionPoint(const Line& line) const {
        //Ax+By+C=0
        // y = -A/Bx -C/B

        // y = ax + c
        // y = bx + d

        double a = get_a();
        double c = get_b();

        double b = line.get_a();
        double d = line.get_b();

        Point p;
        p.x = ((d - c) / (a - b));
        p.y = ((a * d - b * c) / (a - b));

        return p;
    }

    // y = -(Ax + C) / B
    double getY (double x) {
        return - ((getA() * x + getC()) / getB());
    }
};

std::ostream& operator<< (std::ostream& out, Line &line)
{
    out << "(" << line.point1.x << ", " << line.point1.y << ")" << " -> " <<
           "(" << line.point2.x << ", " << line.point2.y << ")" << std::endl;

    return out;
}

    // Converts degrees to radians.
    double degreesToRadians(double angleDegrees) {
        return angleDegrees * M_PI / 180.0;
    }

    // Converts radians to degrees.
    double radiansToDegrees(double angleRadians) {
        return angleRadians * 180.0 / M_PI;
    }

}

#endif // LINE_H
