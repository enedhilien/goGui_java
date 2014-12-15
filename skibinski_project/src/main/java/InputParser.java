import gogui.Point;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by testuser on 12/11/14.
 */
public class InputParser {

    private String filePath;
    private Vector<Point> points;
    private Vector<Vector<Point>> triangles;

    public InputParser(String filePath){
        this.filePath = filePath;
    }

    public void parse() throws IOException {
        Path path = Paths.get(filePath);
        Vector<Point> points = new Vector<>();
        Vector<Vector<Point>> triangles = new Vector<>();
        double x,y;
        int a,b,c;
        try(Scanner scanner = new Scanner(path)){
            while(scanner.hasNextLine()){
                int pointsNumber = scanner.nextInt();
                for(int i=0;i<pointsNumber;i++){
                    x = scanner.nextDouble();
                    y = scanner.nextDouble();
                    points.add(new Point(x,y));
                }
                int triangleNumber = scanner.nextInt();
                for(int i=0;i<triangleNumber;i++){
                    a = scanner.nextInt();
                    b = scanner.nextInt();
                    c = scanner.nextInt();
                    Vector<Point> p = new Vector<>();
                    p.add(points.get(a));p.add(points.get(b));p.add(points.get(c));
                    triangles.add(p);
                }
            }
            this.points = points;
            this.triangles = triangles;
            return;
        }
    }

    public Vector<Vector<Point>> getTriangles() {
        return triangles;
    }

    public Vector<Point> getPoints() {
        return points;
    }
}
