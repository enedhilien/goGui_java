#ifndef RANDOM_HPP
#define RANDOM_HPP

#include <cstdlib>

namespace gogui {
    template <typename T>
    T real_rand(T r_min, T r_max)
    {
        if(r_min > r_max) {
            T tmp = r_min;
            r_min = r_max;
            r_max = tmp;
        }

        T f = (T)std::rand() / RAND_MAX;
        return r_min + f * (r_max - r_min);
    }

    class RandomPointGenerator {
        public:
        static Point withinRectangle(double xmin, double xmax, double ymin, double ymax);
        static Point onRing(double r, double xOffset, double yOffset);
        static Point randomOnRectangle(Point p1, Point p2, Point p3, Point p4);
        static Point onXaxis(double xmin, double xmax);
        static Point onYaxis(double ymin, double ymax);
        static Point onDiagonalFromOrigin(Point p);
        static Point onDiagonalNotFromOrigin(Point p);
    };

    Point RandomPointGenerator::withinRectangle(double xmin, double xmax, double ymin, double ymax)
    {
        return Point(real_rand(xmin, xmax), real_rand(ymin, ymax));
    }

    Point RandomPointGenerator::onRing(double r, double xOffset, double yOffset)
    {
        double alpha = real_rand(0.0L, 360.0L);
        return Point(r * cosl(alpha) + xOffset, r * sinl(alpha) + yOffset);
    }

    // p1 p2
    // p4 p3
    Point RandomPointGenerator::randomOnRectangle(Point p1, Point p2, Point p3, Point p4)
    {
        int side = rand() % 4;

        switch(side)
        {
            // top
            case 0:
                return Point(real_rand(p1.x, p2.x), p1.y);

            // left
            case 1:
                return Point(p2.x, real_rand(p3.y, p2.y));

            // down
            case 2:
                return Point(real_rand(p4.x, p3.x), p3.y);

            // right
            case 3:
                return Point(p4.x, real_rand(p4.y, p3.y));

            // will not happen just to not have -Wreturn-type
            default:
                return Point(real_rand(p1.x, p2.x), p1.y);
        }
    }

    Point RandomPointGenerator::onXaxis(double xmin, double xmax)
    {
        return Point(real_rand(xmin, xmax), 0.0);
    }

    Point RandomPointGenerator::onYaxis(double ymin, double ymax)
    {
        return Point(0.0, real_rand(ymin, ymax));
    }

    //    p1
    //   /
    //  /
    Point RandomPointGenerator::onDiagonalFromOrigin(Point p)
    {
        double i_x1 = 0.0L;
        double i_y1 = 0.0L;

        double i_x2 = p.x;
        double i_y2 = p.y;

        double t = real_rand(0.0L, 1.0L);
        return Point((1-t)*i_x1 + t*i_x2, (1-t)*i_y1 + t*i_y2);
    }

    //  \--p1
    //   \ '
    //    \'
    Point RandomPointGenerator::onDiagonalNotFromOrigin(Point p)
    {
        double i_x1 = 0.0L;
        double i_y1 = p.y;

        double i_x2 = p.x;
        double i_y2 = 0.0L;

        double t = real_rand(0.0L, 1.0L);
        return Point((1-t)*i_x1 + t*i_x2, (1-t)*i_y1 + t*i_y2);
    }
}

#endif // RANDOM_HPP
