import static org.lwjgl.opengl.GL11.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;
import java.lang.Double;
import javax.swing.JOptionPane;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

public class exec
{
	public static void main(String[] args) throws Exception
	{
		Vector<PointCharge> list =  new Vector<PointCharge>();
		
		Vector<Sphere> planets = new Vector<Sphere>();
		double deltaT = 0.001;
        double posx;
        double posy;
        double posz;
        double velx;
        double vely;
        double velz;
        double mass;
        double charge;
	int index = 0;
        PrintWriter pw = null;
        BufferedReader br = null;
        //*************************************************************************************
        boolean cont = true;
        try
        {
        	br = new BufferedReader(new FileReader("Settings.txt"));
        	//pw = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
        	//pw.println("Initial variables; G = 1" + ", \u0394t = " + deltaT );
        	while(cont)
        	{
        		String datum;
        		if((datum = br.readLine()) != null)//for some reason, at the beginning , readline returns null
        		{
            		String[] data = datum.split(",");
            		charge = Double.parseDouble(data[0]);
			mass = Double.parseDouble(data[1]);
            		posx = Double.parseDouble(data[2]);
            		posy = Double.parseDouble(data[3]);
            		posz = Double.parseDouble(data[4]);
            		velx = Double.parseDouble(data[5]);
            		vely = Double.parseDouble(data[6]);
            		velz = Double.parseDouble(data[7]);
            		PointCharge pm = new PointCharge(charge,mass,posx,posy,posz,velx,vely,velz);
            		//pw.println("Body " + index++ + " : pos:" + posx + ", " + posy + ", " + posz + ". vel:" + velx + ", " + vely + ", " + velz + ". ");
            		Sphere s = new Sphere();
            		list.add(pm);
            		planets.add(s);
        		}
        		else
        		{
        			System.out.println(list.size());
        			cont = false;
        		}

        	}
        	br.close();
        }
        catch(Exception e)
        {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        	System.exit(1);
        }

		//***************************************************************************************
		try 
		{
			Display.setDisplayMode(new DisplayMode(800,800));
			Display.create();
	        GL11.glMatrixMode(GL11.GL_PROJECTION);
	        GL11.glLoadIdentity();
	        GLU.gluPerspective(45f,1,0.1f,100f);
	        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	        GLU.gluLookAt(2f,2f,-52f, 0f, 0f, -50f, 0f, 8f, 0f);
	        glEnable(GL_LIGHTING);
	        glEnable(GL_LIGHT0);
	        glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_DEPTH_TEST);//Even though it looks like depth buffering is failling, it's because one sphere is much larger than the other
		glDepthFunc(GL_LESS);
		glDepthMask(true);
	        glClearColor(0f,0f,0f,1f);
		}
		catch (LWJGLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		}        
        //***************************************************************************************
    	int i = 1;
        while(Display.isCloseRequested() == false)
        {
        	//pw.println("loop: "+ i);
        	for(PointCharge p : list)
        	{
        		p.calculateNewVectors(list, deltaT);
        	}
        	for(PointCharge p : list)
        	{
        		p.update();
        	}
        	index = 0;
        	/*for(PointCharge p : list)
        	{
        		double [] x = p.getX();
        		double [] v = p.getV();
        		pw.println("Body " + index++ + ": pos:" + x[0] + ", " + x[1] + ", " + x[2] + ". vel:" + v[0] + ", " + v[1] + ", " + v[2] + ". ");
        	}
        	if((i % 100) == 0)
        	{
        		pw.flush();
        		System.out.println(i);
        	}*/
        	//****************
        	if((i % (1000)) == 0)
        	{
        		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        		index = 0;
				glBegin(GL_LINES);
				glColor3f(1f,1f,1f);
				glLineWidth(2.5f);
		    	glVertex3f(0f,0f,-50f);
		    	glVertex3f(1f,0f,-50f);
		    	glVertex3f(0f,0f,-50f);
		    	glVertex3f(0f,1f,-50f);
		    	glVertex3f(0f,0f,-50f);
		    	glVertex3f(0f,0f,-51f);		    	
		    	glEnd();
		    	for(PointCharge p: list)
        		{
    				glPushMatrix();
    				glColor3f(p.getColour()[0],p.getColour()[1],p.getColour()[2]);
    				glTranslatef((float)p.getX()[0]/100, (float)p.getX()[1]/100, (float)p.getX()[0]/100- 50f);
    				planets.get(index++).draw(radius(p.getMass()), 10, 10);
    				glPopMatrix();
   				
        		}
        		
        		Display.update();
        	}
        	i++;
        }
        /*try
        {
        pw.close();
        }
        catch(Exception e)
        {
        
        }*/
	}
	static float radius(double mass)
	{
		return (float) Math.pow((Math.abs(mass)*3/4/Math.PI/10000), 1.0/3.0);
	}
}
