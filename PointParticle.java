import java.lang.Math;
import java.util.Random;
import java.util.Vector;//

public class PointParticle
{
	static double Ke;
	double G;
	double mass;
	double charge;
	double[] x0;
	double[] v0;
	double[] vnew;
	double[] xnew;
	float[] colour;
	public PointParticle(double theMass,double theCharge, double x, double y, double z, double vx, double vy, double vz,double Gconst, double Kconst)
	{
		G = Gconst;
		Ke = Kconst;
		x0 = new double[3];
		v0 = new double[3];
		colour = new float[3];
		v0[0] = vx;
		v0[1] = vy;
		v0[2] = vz;
		x0[0] = x;
		x0[1] = y;
		x0[2] = z;
		charge = theCharge;
		mass = theMass;
		Random r = new Random();
		colour[0] = r.nextFloat();
		colour[1] = r.nextFloat();
		colour[2] = r.nextFloat();
	}
	double[] getX()//gets current location
	{
		return x0;
	}
	double[] getV()//gets current location
	{
		return v0;
	}
	double getCharge()
	{
		return charge;
	}
	float[] getColour()
	{
		return colour;
	}
	double getMass()
	{
		return mass;
	}
	void calculateNewVectors(Vector<PointParticle> input, double deltaT)
	{
		double temp = 0;// for intermediate calculations
		double [] deltax = new double[3];
		double [] r = new double[input.size()];
		double [][] a = new double[input.size()][3];
		double [] totalA = new double[3];
		for(int x = 0; x < input.size(); x++)
		{	
			PointParticle o = input.get(x);
			if(o.equals(this))//IMPORTANT: THIS MAKES IT SO THAT THE POINT CHARGES DO NOT COMPARE THEMSELVES (Divide by 0)
			{
				continue;
			}
			double [] ox = o.getX();
			deltax[0] = ox[0] - x0[0];//these vars are needed to calculate acceleration
			deltax[1] = ox[1] - x0[1];//*
			deltax[2] = ox[2] - x0[2];//*
			temp = (deltax[0] * deltax[0]) + (deltax[1] * deltax[1]) + (deltax[2] * deltax[2]);
			r[x] = Math.sqrt(temp);
			//******************* need to preserve magnitude and direction
			double GravForce = (G * mass * o.getMass())/(r[x]*r[x]);//F = Kq1q2/r^2
			double ElecForce = (Ke * charge * o.getCharge())/(r[x]*r[x]);//F = Kq1q2/r^2
			double force = GravForce + ElecForce;
			double accel = force/mass;
			double coefficient = accel/r[x];//when you multiply coefficient by dx, you multiply a*dx/r. dx/r is a vector over it's norm, just giving a unit vector
			a[x][0] = coefficient * deltax[0];	
			a[x][1] = coefficient * deltax[1];	
			a[x][2] = coefficient * deltax[2];	
		}
		//add all accelerations
		for(int x = 0; x < input.size(); x++)
		{
			totalA[0] += a[x][0];
			totalA[1] += a[x][1];
			totalA[2] += a[x][2];
		}
		//on to calculating next velocity vector
		vnew = new double[3];
		vnew[0] = v0[0] + (totalA[0] * deltaT);
		vnew[1] = v0[1] + (totalA[1] * deltaT);
		vnew[2] = v0[2] + (totalA[2] * deltaT);
		//on to calculating next position vector
		xnew = new double[3];
		xnew[0] = x0[0] + (v0[0] * deltaT) + (0.5 * totalA[0] * deltaT * deltaT);
		xnew[1] = x0[1] + (v0[1] * deltaT) + (0.5 * totalA[1] * deltaT * deltaT);
		xnew[2] = x0[2] + (v0[2] * deltaT) + (0.5 * totalA[2] * deltaT * deltaT);
		
	}
	void update()//changes position
	{
		x0 = xnew;
		v0 = vnew;
	}
}
