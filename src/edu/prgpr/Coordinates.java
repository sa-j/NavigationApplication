/*
 * Coordinates-class for use in
 * "Praktikum Grundlagen der Programmierung Wintersemester 2011/12"
 * Nägel/Rupp/Wittum
 *
 * This class provides you structures and functions for converting
 * WGS84 and UTM coordinates.
 * For more information see
 * Bernhard Hofmann-Wellenhof, "GPS in der Praxis", page 93
 *
 *
 * intended use:
 * UTMPoint utm;
 * double latDeg = java.lang.Double.parseDouble(atts.getValue("lat"));
 * double lonDeg = java.lang.Double.parseDouble(atts.getValue("lon"));
 * WGS84Point wgs = new WGS84Point();
 * wgs.SetDegrees(latDeg, lonDeg);
 * utm = new UTMPoint();
 * Coordinates.convertWGS84toUTM(wgs, utm);
 *
 * If you have maps which cover two UTM zones (like from 5.999° to 6.001° longitude),
 * you need to fix a zone with
 * utm.SetZone(32);
 * or determine a (global) myUTMZone with your first conversion by
 * if(myUTMZone != -1) utm.SetZone(myUTMZone);
 * Coordinates.convertWGS84toUTM(wgs, utm);
 * myUTMZone = utm.GetZone();
 *
 * @see UTMPoint
 * @see WGS84Point
 * @author Martin Rupp
 */

package edu.prgpr;

import java.awt.geom.Point2D;

/**
 *
 * @author mrupp

 */
public class Coordinates
{
    final static double fRadianToDegree = 360/(2*Math.PI);
    final static double fDegreeToRadian = (2*Math.PI)/360;

    public static double RadianToDegree(double alpha)
    {
	return alpha*fRadianToDegree;
    }

    public static double DegreeToRadian(double alpha)
    {
	return alpha*fDegreeToRadian;
    }

    /**
     * note that latitude = german "geographische Breite" = phi
     * and longitude = german "geographische Länge" = lambda
     * (the equator is the line with latitude = 0, N=+, S=-,
     * Greenwich is longitude = 0, W=-, E=+)
     */
    public static class WGS84Point
    {        
        double lat, lon; // phi, lambda

	public WGS84Point() {}

	/**
	 * @param _lat latitude in radiants (-Pi/2..Pi/2)
	 * @param _lon longitude in radiants (-Pi..Pi)
	 */
        public void SetRadians(double _lat, double _lon)
        {
            lat = _lat;
            lon = _lon;
        }

	/**
	 * @param _lat latitude in degrees (-90-90)
	 * @param _lon longitude in degrees (-180..180)
	 */
        public void SetDegrees(double _lat, double _lon)
        {
            SetRadians(DegreeToRadian(_lat), DegreeToRadian(_lon));
        }    

        double GetLatDeg()
        {
            return RadianToDegree(lat);
        }

        double GetLonDeg()
        {
            return RadianToDegree(lon);
        }

        double GetLatRadian()
        {
            return lat;
        }

        double GetLonRadian()
        {
            return lon;
        }

        @Override
        public String toString()
        {
            return "WGS84Point(lat:"+GetLatDeg()+"°, lon:"+GetLonDeg()+"°)";
        }
    }

    /**
     * UTM-Point class. contains x, y, and a utm-zone.
     * Can be used like a Point2D.Double. Zone = -1 means
     * zone has to be calculated. If you set a specified zone before
     * doing a conversion, you force the point to be in this zone.
     * You might need this in Germany if you have maps which are 
     * around 6° or 12° longitude.
     * <br>
     * Note for displaying coordinates that UTMPoint.y is positive on the
     * northern hemisphere and negative on the southern hemisphere.
     * @see Point2D.Double
     */
    public static class UTMPoint extends Point2D.Double
    {
        public UTMPoint()
        {
            x = 0;
            y = 0;
            zone = -1;
        }
        public UTMPoint(double _x, double _y, int _zone)
        {
            x = _x;
            y = _y;
            zone = _zone;
        }

        public void SetZone(int z)
        {
            zone = z;
        }

        public int GetZone()
        {
	    return zone;
        }

	public void UnsetZone()
	{
	    zone = -1;
	}
	public boolean IsZoneSet()
	{
	    return zone != -1;
	}

	public void Set(double _x, double _y, int _zone)
	{
	    zone = _zone;
	    x = _x;
	    y = _y;
	}

        @Override
        public String toString()
        {
            return "UTMPoint(zone:"+GetZone()+" x:"+x+", y:"+y+")";
        }

        private int zone;
    }

    
    final static double UTMValueA  = 6378137.0;
    final static double UTMValueB  = 6356752.31425;
    final static double UTMScale   = 0.9996;
    final static double UTMEccentricity2 = 0.00673949674226;

    /**
     * @param fPhi
     * @return the meridian length to phi
     */
    static double UTMGetMeridianLength(double fPhi)
    {
	final double UTMAlpha   = 6367449.1458258;
	final double UTMBeta    = -0.0025188279155515;
	final double UTMGamma   = 2.6435410586161e-006;
	final double UTMDelta   = -3.4526234201967e-009;
	final double UTMEpsilon = 4.8918303238935e-012;

	return UTMAlpha*(fPhi + UTMBeta * Math.sin(2.0*fPhi)+UTMGamma*Math.sin(4.0*fPhi));
		//+ UTMDelta*Math.sin(6.0*fPhi) + UTMEpsilon*Math.sin(8.0*fPhi));
    }


    /**
     * Valid numbers are from 1 to 60, and are, with the exception of Svalbard,
     * defined as lonZone = (longitude+180)/6 + 1.
     * @link http://www.dmap.co.uk/utmworld.htm
     * @param p WGS84Point
     * @return the UTM-Longitude-Zone number
     */
    static int GetUTMLongitudeZoneNumber(WGS84Point p)
    {
	double fLon = p.GetLonDeg();
	double fLat = p.GetLatDeg();
	int zone = (int)((fLon+ 180)/6) + 1;

	if( fLat >= 56.0 && fLat < 64.0 && fLon >= 3.0 && fLon < 12.0 )
		zone = 32;

	// Special zones for Svalbard
	if( fLat >= 72.0 && fLat < 84.0 )
	{
		if(fLon >= 0.0  && fLon <  9.0 ) zone = 31;
		else if( fLon >= 9.0  && fLon < 21.0 ) zone = 33;
		else if( fLon >= 21.0 && fLon < 33.0 ) zone = 35;
		else if( fLon >= 33.0 && fLon < 42.0 ) zone = 37;
	}
	return zone;
    }

    /**
     * Converts a WGS84-Point to a UTM Point.
     * If the zone field int UTMPoint is set, the setted zone is used.
     * Unsetted zones are -1.
     * @param utm UTMPoint, in
     * @param wgs WGS84Point, out
     * @see UTMPoint
     */
    public static void convertWGS84toUTM(WGS84Point wgs, UTMPoint utm)
    {
	double	fCosPhi;
	double	fTanPhi;
	double	fTanPhi2, fTanPhi4;
	double	fEta2;

	double	fDeltaLength;
	double  fRadiusN;
	double  fCosL, fCosL2;
	double  fMeridian;
	double	l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;
	double	x, y;

	int zone;
	if(utm.IsZoneSet())
	    zone = utm.GetZone();
	else
	    zone = GetUTMLongitudeZoneNumber(wgs);

	double fPhi = wgs.GetLatRadian();
	double fLambda = wgs.GetLonRadian();

	fCosPhi = Math.cos(fPhi);
	fTanPhi = Math.tan(fPhi);

	fMeridian=  DegreeToRadian((zone*6.0)-183.0);

	fDeltaLength = (fLambda - fMeridian);

	fCosL2 = fCosPhi * fDeltaLength;

	fEta2 = UTMEccentricity2 * fCosPhi * fCosPhi;
	fRadiusN = UTMValueA*UTMValueA/(UTMValueB*Math.sqrt(1+fEta2));
	fTanPhi2 = fTanPhi*fTanPhi;

	fCosL = fCosPhi * fDeltaLength;
	fCosL2 = fCosL*fCosL;

	fTanPhi2 = fTanPhi * fTanPhi;
	fTanPhi4 = fTanPhi2*fTanPhi2;

	l3coef = 1.0 - fTanPhi2 + fEta2;
	l4coef = 5.0 - fTanPhi2 + 9 * fEta2 + 4.0 * (fEta2 * fEta2);
	l5coef = 5.0 - 18.0 * fTanPhi2 + (fTanPhi4) + 14.0 * fEta2 - 58.0 * fTanPhi2 * fEta2;
	l6coef = 61.0 - 58.0 * fTanPhi2 + (fTanPhi4) + 270.0 * fEta2 - 330.0 * fTanPhi2 * fEta2;
	l7coef = 61.0 - 479.0 * fTanPhi2 + 179.0 * (fTanPhi4) - (fTanPhi4 * fTanPhi2);
	l8coef = 1385.0 - 3111.0 * fTanPhi2 + 543.0 * (fTanPhi4) - (fTanPhi4* fTanPhi2);

	// Horner-Schema
	x = fRadiusN * fCosL *
		(1.0 + fCosL2 *
			(l3coef/6.0 + fCosL2 *
				(l5coef/120.0 + fCosL2 * l7coef/5040.0)));

	y = UTMGetMeridianLength(fPhi) +
		fTanPhi*fRadiusN * fCosL2 *
			(1.0/2.0 + fCosL2 *
				(l4coef/24.0 + fCosL2*
					(l6coef/720.0 * fCosL2 * l8coef/40320.0)));

	utm.Set(500000.0 + x*UTMScale, y * UTMScale, zone);
    }

    static double UTMFootpointLatitude(UTMPoint utm)
    {
	final double UTMAlpha_ = 6367449.14582582;
	final double UTMBeta_ = 2.51882658382898e-03;
	final double UTMGamma_ = 3.70094903394928e-06;
	final double UTMDelta_ = 7.44777026493115e-09;
	final double UTMEpsilon_ = 1.70359932232102E-11;

	double x = utm.x/UTMScale;
	double f = x/UTMAlpha_;
	return f+(UTMBeta_ * Math.sin(2.0*f) + UTMGamma_*Math.sin(4.0*f) + UTMDelta_*Math.sin(6.0*f) +
	UTMEpsilon_*Math.sin(8.0*f));
    }


    /**
     * converts a UTM-Point to a WGS84 Point.
     * @param utm UTMPoint, in
     * @param wgs WGS84Point, out
     */
    public static void convertUTMtoWGS84(UTMPoint utm, WGS84Point wgs)
    {
	double fLambda0, fPhiF, fCosF, fEtaF2, fNf, fTf, fTf2, fNfpow;
	double x2poly, x3poly, x4poly, x5poly, x6poly, x7poly, x8poly;

	double x1frac, x2frac, x3frac, x4frac, x5frac, x6frac, x7frac, x8frac;
	double y2, y3, y4, y5, y6, y7, y8;

	double y = (utm.y - 500000.0)/UTMScale;
	double x = utm.x/UTMScale;
	fLambda0 = DegreeToRadian(-183 + (utm.GetZone() * 6.0));

	fPhiF = UTMFootpointLatitude(utm);
	fCosF = Math.cos(fPhiF);
	fEtaF2 = fCosF*fCosF*UTMEccentricity2;

	fNf = UTMValueA*UTMValueA/(UTMValueB*Math.sqrt(1+fEtaF2));
	fTf = Math.tan(fPhiF);
	fTf2 = fTf*fTf;

	fNfpow = fNf;

	x1frac = 1.0 / (fNfpow * fCosF);
	fNfpow *= fNf;	// ^2
	x2frac = fTf/(2.0*fNfpow);
	fNfpow *= fNf;	// ^3
	x3frac = 1.0/(6.0*fNfpow * fCosF);
	fNfpow *= fNf;	// ^4
	x4frac = fTf/(24.0*fNfpow);
	fNfpow *= fNf;	// ^5
	x5frac = 1.0/(120.0*fNfpow * fCosF);
	fNfpow *= fNf;	// ^6
	x6frac = fTf/(720.0*fNfpow);
	fNfpow *= fNf; // ^7
	x7frac = 1.0/(5040.0*fNfpow * fCosF);
	fNfpow *= fNf; // ^8
	x8frac = 1.0/(40320.0*fNfpow * fCosF);

	x2poly = -1.0 - fEtaF2;
	x3poly = -1.0 - 2 * fTf2 - fEtaF2;
	x4poly = 5.0 + 3.0 * fTf2 + 6.0 * fEtaF2 - 6.0 * fTf2 * fEtaF2 - 3.0 * (fEtaF2 * fEtaF2) - 9.0 * fTf2 * (fEtaF2 * fEtaF2);
	x5poly = 5.0 + 28.0 * fTf2 + 24.0 * fTf2*fTf2 + 6.0 * fEtaF2 + 8.0 * fTf2 * fEtaF2;
	x6poly = -61.0 - 90.0 * fTf2 - 45.0 * fTf2*fTf2 - 107.0 * fEtaF2 + 162.0 * fTf2 * fEtaF2;
	x7poly = -61.0 - 662.0 * fTf2 - 1320.0 * fTf2*fTf2 - 720.0 * (fTf2*fTf2 * fTf2);
	x8poly = 1385.0 + 3633.0 * fTf2 + 4095.0 * fTf2*fTf2 + 1575 * (fTf2*fTf2 * fTf2);

	y2 = y*y;
	y3 = y2*y;
	y4 = y3*y;
	y5 = y4*y;
	y6 = y5*y;
	y7 = y6*y;
	y8 = y7*y;

	double phi = fPhiF + x2frac * x2poly * y2 + x4frac * x4poly * y4
		+ x6frac * x6poly * y6 + x8frac * x8poly * y8;

	double lambda = fLambda0 + x1frac * y + x3frac * x3poly * y3 + x5frac * x5poly * y5
		+ x7frac * x7poly * y7;


	wgs.SetRadians(phi, lambda);
    }
}
