package MyGIS;

import java.awt.image.BufferedImage;

//import .Cords;

import Geom.Point3D;
import MyCoords.MyCoords;
/**
 * This is a class that represents a map for this project
 * @author David Tover
 *
 */
public class Map {

	private final Point3D RT = new Point3D(32.105886,35.212337,650);
	private final Point3D RB = new Point3D(32.101900,35.212337,650);
	private final Point3D LT = new Point3D(32.105886,35.202424,650);
	private final Point3D LB = new Point3D(32.101900,35.202424,650);
	private final double diffLat = .003986;
	private final double diffLon = .009913;

	private String myImage;
	private double latPerHeight;
	private double lonPerWidth;
	private double width;
	private double height;
	/**
	 * This is a constructor for the Map
	 * @param w The Width of the Map
	 * @param h The Height of the Map
	 * @param image the path of the Image you want to put in
	 */
	public Map(double w, double h , String image) {
		width = w;
		height = h;
		myImage = image;
		lonPerWidth = diffLon/width;
		latPerHeight = diffLat/height;
	}
	/**
	 * This is a method that converts from coordinates to pixels
	 * @param p1 A Point in coordinates
	 * @return A Point in pixels
	 */
	public Point3D Coords2Pixels(Point3D gps) {


		double dx = Math.abs(gps.x()-LT.x());
		double dy = Math.abs(gps.y()-LT.y());

		double rx = dx/diffLat;
		double ry = dy/diffLon;

		int w = (int)(ry*width) +1;
		int h = (int)(rx*height)+1;

		Point3D ans = new Point3D(w,h,gps.z());
		return ans;

	}
	/**
	 * This is a method to convert from pixels to coordinates 
	 * @param p1 the Point in pixels
	 * @param w the width of the frame
	 * @param h the height of the frame
	 */
	public Point3D Pixels2Coords(Point3D p1, double w, double h) {

		double vX = LT.x()-p1.y()*latPerHeight;
		double vY = LT.y() + p1.x()*lonPerWidth;



		Point3D p2 = new Point3D(vX,vY,p1.z());

		return p2;

	}
	/**
	 * This method finds the distance between 2 pixels in meters based on the image
	 * @param p1 first pixel
	 * @param p2 second pixel
	 * @return distance between the pixels
	 */
	public double DistanceBetweenPixels(Point3D p1, Point3D p2) {
		MyCoords mc = new MyCoords();
		double dx = mc.distance3d(LT, RT);
		double dy = mc.distance3d(LT, LB);
		int y = Math.abs(p2.iy()-p1.iy());
		int x = Math.abs(p2.ix()-p1.ix());
		double ry = y/getHeight();
		double rx = x/getWidth();
		double disy = ry*dy;
		double disx = rx*dx;

		return Math.sqrt(disy*disy + disx*disx);


	}
	public int distanceInPixels(double dist) {
		MyCoords mc = new MyCoords();

		double dCoor = mc.distance3d(LT,RT);
		double dPix = getWidth();

		double r = dPix / dCoor;
		return (int)(dist*r);

	}
	public double findAngle(Point3D gps1, Point3D gps2) {
		MyCoords mc = new MyCoords();
		double[] aed = mc.azimuth_elevation_dist(gps1, gps2);
		return  aed[0];

	}

	public Point3D pointInTime(Point3D gps1, double dist,double angle) {
		MyCoords mc = new MyCoords();
		double x = dist*Math.cos(toRad(angle));
		double y = dist*Math.sin(toRad(angle));
		Point3D v = new Point3D(x,y);
		Point3D point = mc.add(gps1,v);
		return point;

	}
	public double getLatPerHeight() {
		return latPerHeight;
	}
	public double getLonPerWidth() {
		return lonPerWidth;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public String getMyImage() {
		return myImage;
	}
	private double toRad(double deg) {
		double rad = (deg*Math.PI)/180;
		return rad;

	}
	private double toDeg(double rad) {
		double deg = (rad*180)/Math.PI;
		return deg;
	}
	public static void main(String []args) {


		//		Map m = new Map(1386,642,"Ariel1.png");
		//		Point3D p1 = new Point3D(32.10255812461059,35.207452022366525,650);
		//		Point3D p2 = new Point3D(32.10484293457944,35.20783109090909,650);
		//		MyCoords mc = new MyCoords();
		//		
		//		double ans = mc.distance3d(p1, p2);


		//		Point3D p1 = new Point3D(703,536);
		//		System.out.println(m.Pixels2Coords(p1, m.getWidth(), m.getHeight()));
		//		Point3D p2 = new Point3D(756,168);
		//		System.out.println(m.Pixels2Coords(p2, m.getWidth(), m.getHeight()));
		//		Point3D p3 = m.Coords2Pixels(p2);
		//		p3 = m.Pixels2Coords(p3, m.getWidth(), m.getHeight());
		//		double ans = m.DistanceBetweenPixels(p1, p2);
		//		System.out.println(ans);
		//		System.out.println(p3);

		MyCoords mc = new MyCoords();
		Point3D p1 = new Point3D(32.103315,35.209039);
		Point3D p2 = new Point3D(32.106352,35.205225);
		double [] aed = mc.azimuth_elevation_dist(p1, p2);
//		System.out.println(aed[0]);
		Map m = new Map(1386,642,"Ariel1.png");
		double angle = m.findAngle(p1, p2);
//		Point3D p3 = m.pointInTime(p1,aed[2],aed[0]);
		System.out.println(angle);
	}
}
