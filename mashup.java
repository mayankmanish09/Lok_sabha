package lok_sabha;
/*
Author: Mayank Raj
General Comments: Prefuse is used only to read the csv file...
*/
import java.applet.Applet; //Importing Applet class

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import prefuse.data.Table;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;

public class mashup extends Applet
implements MouseListener, MouseMotionListener {
	public static int type_of_mashup=0, Sort_party_by=0, Sort_states_by=0;	//The Parameters That define The Mashup (they are altered by Drop-Downs)
	
	Font font1 = new Font("Arial", Font.BOLD, 15);
	public static Table t = null;
	
	public static String states[]=new String[36] ;
	public static String states_cons[]=new String[36] ;	//To be initialized in init()
	public static String[] party_cons={"All India Anna Dravida Munnetra Kazhagam","All India Forward Bloc","All India Majlis-E-Ittehadul Muslimmen","All India Trinamool Congress","Asom Gana Parishad","Assam United Democratic Front","Bahujan Samaj Party","Bahujan Vikas Aaghadi","Bharatiya Janata Party","Biju Janata Dal","Bodoland People's Front","Communist Party of India","Communist Party of India (Marxist)","Dravida Munnetra Kazhagam","Haryana Janhit Congress","Independent","Indian National Congress","Jammu and Kashmir National Conference","Janata Dal (Secular)","Janata Dal (United)","Jharkhand Mukti Morcha","Jharkhand Vikas Morcha (Prajatantrik)","Kerala Congress (M)","Marumalarchi Dravida Munnetra Kazhagam","Muslim League Kerala State Committee","Nagaland People's Front","Nationalist Congress Party","Rashtriya Janata Dal","Rashtriya Lok Dal","Revolutionary Socialist Party","Samajwadi Party","Shiromani Akali Dal","Shiv Sena","Sikkim Democratic Front","Swabhimani Paksha","Telangana Rashtra Samithi","Telugu Desam Party","Viduthalai Chiruthaigal Katchi","Yuvajana Sramika Rythu Congress Party"};	
	public static String[] party = {"All India Anna Dravida Munnetra Kazhagam","All India Forward Bloc","All India Majlis-E-Ittehadul Muslimmen","All India Trinamool Congress","Asom Gana Parishad","Assam United Democratic Front","Bahujan Samaj Party","Bahujan Vikas Aaghadi","Bharatiya Janata Party","Biju Janata Dal","Bodoland People's Front","Communist Party of India","Communist Party of India (Marxist)","Dravida Munnetra Kazhagam","Haryana Janhit Congress","Independent","Indian National Congress","Jammu and Kashmir National Conference","Janata Dal (Secular)","Janata Dal (United)","Jharkhand Mukti Morcha","Jharkhand Vikas Morcha (Prajatantrik)","Kerala Congress (M)","Marumalarchi Dravida Munnetra Kazhagam","Muslim League Kerala State Committee","Nagaland People's Front","Nationalist Congress Party","Rashtriya Janata Dal","Rashtriya Lok Dal","Revolutionary Socialist Party","Samajwadi Party","Shiromani Akali Dal","Shiv Sena","Sikkim Democratic Front","Swabhimani Paksha","Telangana Rashtra Samithi","Telugu Desam Party","Viduthalai Chiruthaigal Katchi","Yuvajana Sramika Rythu Congress Party"};
	public static int data[][][]=new int[5][36][39];	//The data for determining size of balls 
	
	//Sorting data of states...size, attendance, sex ratio , age
	public static int state_data[][]={{43,1,42,2,14,41,1,12,1,1,7,2,26,11,4,6,15,29,20,1,29,48,2,2,1,1,21,1,13,25,1,39,2,81,5},{19,0,13,0,16,13,0,20,0,0,16,0,18,22,0,0,0,3,0,0,26,6,0,100,0,0,0,0,44,13,0,2,0,19,0},{62,76,62,78,81,84,0,64,50,63,55,87,70,62,66,74,55,62,71,85,77,64,99,0,97,92,72,0,72,73,90,61,79,74,65},{59,62,55,49,63,59,64,54,40,57,57,62,57,47,58,66,57,56,58,30,55,56,62,38,76,69,54,65,55,55,58,56,73,52,61}};
	
	 //Sorting data of parties... size, attendance, sex ratio , age
 	 public static int party_data[][]={{9,2,1,20,1,1,21,1,117,14,1,4,16,18,2,12,207,3,3,20,2,2,1,1,2,1,9,4,5,2,22,4,11,1,1,2,6,1,2},{78,71,69,45,73,52,82,93,76,73,60,89,85,44,46,68,65,57,56,87,27,52,83,73,38,92,48,89,72,68,83,75,67,90,73,11,68,47,50},{0,0,0,20,0,0,19,0,11,0,0,0,6,5,0,25,11,0,0,10,0,0,0,0,0,0,22,0,20,0,13,50,9,0,0,50,0,0,0},{56,54,43,61,58,62,51,56,56,55,52,58,55,53,62,58,57,71,61,58,62,52,47,65,70,69,48,68,45,64,50,56,56,58,45,52,54,50,53},{62,76,62,78,81,84,0,64,50,63,55,87,70,62,66,74,55,62,71,85,77,64,99,0,97,92,72,0,72,73,90,61,79,74,65},};
	 
 	 int width, height, off;//Applet Coordinates...
 	 
 	 //making the Colour-Gradient array...
	 int N = 25; 
	 Color[] spectrum = new Color[N]; 
	 Color spec[]	  =	new Color[N];
	
	 int mx, my, intx, inty, show=0;  // the mouse coordinates
	 boolean isButtonPressed = false;
	 public Choice choice, sort_state, sort_party, parties;
	 
	 //To give the mouse feeling...planning to implement backbuffer.....
	 Image backbuffer;
	 Graphics backg;
	 
	//------------------------------------------------Global Variables Above---------------------------------------------------
	 public void init() {	//This function is called at startup...making everything ready :)
		width = getWidth();
		height = 600;
		off = 50;
		states[0] = "West Bengal";states[1] = "Andaman and Nicobar Islands";states[2] = "Andhra Pradesh";states[3] = "Arunachal Pradesh";states[4] = "Assam";states[5] = "Bihar";states[6] = "Chandigarh";states[7] = "Chhattisgarh";states[8] = "Dadra and Nagar Haveli";states[9] = "Daman and Diu";states[10] = "Delhi";states[11] = "Goa";states[12] = "Gujarat";states[13] = "Haryana";states[14] = "Himachal Pradesh";states[15] = "Jammu and Kashmir";states[16] = "Jharkhand";states[17] = "Karnataka";states[18] = "Kerala";states[19] = "Lakshadweep";states[20] = "Madhya Pradesh";states[21] = "Maharashtra";states[22] = "Manipur";states[23] = "Meghalaya";states[24] = "Mizoram";states[25] = "Nagaland";states[26] = "Orissa";states[27] = "Puducherry";states[28] = "Punjab";states[29] = "Rajasthan";states[30] = "Sikkim";states[31] = "Tamil Nadu";states[32] = "Tripura";states[33] = "Uttar Pradesh";states[34] = "Uttarakhand";
		for(int pochi=0;pochi<35;pochi++)states_cons[pochi]=states[pochi];
		
		CSVTableReader c = new CSVTableReader(); // Reading the csv File...
		try {
			t = c.readTable("/ass2_1.csv");
		} catch (DataIOException e) {
			e.printStackTrace();
		}// Reading Complete...

		//Make the 3 drop down's
			add(new Label("\t Plot Based on: "));
		    choice = new Choice(); choice.addItem("Attendance"); choice.addItem("Age"); choice.addItem("Rating"); choice.addItem("Sex Ratio"); choice.addItem("Questions...");
		    add(choice);
		    add(new Label("\t Sort States By: "));
		    sort_state=new Choice(); sort_state.addItem("No. Of MP's"); sort_state.addItem("Avg. Attendance"); sort_state.addItem("Sex Ratio"); sort_state.addItem("Avg. Age");
		    add(sort_state);
		    add(new Label("\t Sort Parties By: "));
		    sort_party = new Choice(); sort_party.addItem("Size"); sort_party.addItem("Avg. Attendance"); sort_party.addItem("Sex Ratio"); sort_party.addItem("Avg. Age");
		    add(sort_party);
		    add(new Label("\t Showing Parties: "));
		    parties=new Choice();parties.addItem("First Ten");parties.addItem("Second Ten");parties.addItem("Third Ten");parties.addItem("Last Ten");
		    add(parties);
		 //Drop down adding complete...
		   
		 //Sorting parties by default choice=size
		    int temp;int tempo[]=new int[39];		
	    	  for(int pochi=0;pochi<39;pochi++)	tempo[pochi]=party_data[0][pochi];
	    	  for(int i=0;i<39;i++){
	        		temp=i;
	        		for(int j=i;j<35;j++){
	        			if(tempo[j]>tempo[temp]) temp=j;
	        		}
	        		party[i]=party_cons[temp];
	        		tempo[temp]=-1;
	        	}//End of sort
	    
	   filldata();//Filling in the data to make the mashup....
	   
		// Making the Color Pallete:
		for (int i = 1; i <= N; i++) {
			spec[i-1] = new Color(255, (10 * i), 50+8*i);
		}

		//Initializing Double Buffering Components...
		backbuffer = createImage( width, 800 );
	    backg = backbuffer.getGraphics();
	    backg.setColor( Color.white );
	    
	    //Adding mouse functionality...
		addMouseListener( this );
	    addMouseMotionListener( this );
	}//End of init()

//--------------------------------------------------------Mousie part--------------------------------------------------------
	   public void mouseEntered( MouseEvent e ) {}
	   public void mouseExited( MouseEvent e ) {}
	   public void mousePressed( MouseEvent e ) {}
	   public void mouseReleased( MouseEvent e ) {}
	   public void mouseDragged( MouseEvent e ) {}
	   public void mouseClicked( MouseEvent e ) {}
	   public void mouseMoved( MouseEvent e ) {
		   mx=e.getX();
		   my=e.getY();
		   
		   if(mx<1196 && mx>92 && my<550 && my>90){	//Which ball clicked?
			   intx=(mx-92)/46;
			   inty=(550-my)/46;
		   }
		   
		   	show=1;
		   	backbuffer = createImage( width, 800 );		//Creating the image to be swapped in backbuffer...
		   	backg = backbuffer.getGraphics();
		   	backg.setColor(Color.BLACK);
		   	paint_basic(backg);
		   	
			Font f1=backg.getFont();
			backg.setFont(font1);
			backg.drawString("State: ", 150, 60);
			backg.drawString(states[intx], 200, 60);
			
			backg.drawString("Party: ", 150, 75);
			backg.drawString(party[inty], 200, 75);
			
			backg.drawString("Average "+choice.getSelectedItem(), 550, 70);
			
			String po=":"+data[choice.getSelectedIndex()][intx][inty];
			backg.drawString(po, 700, 70);
			
			backg.setFont(f1);
			backg.setColor(Color.YELLOW);
			backg.fillRect(92, 550-46-inty*46+12, 1104, 23);			//parallel to x axis
			backg.fillRect(92+intx*46+12, 90, 23, 460);				//parallel to y axis
			backg.setColor(Color.BLACK);
			
			for(int i=0;i<24;i++){
				for(int j=0;j<10;j++){
					Artist(backg, i+1, j+1, 24*data[type_of_mashup][i][j]/100);
				}
			}
		   repaint();
		   e.consume();
	   }
	  
	   
//------------------------------------------------^-Mousie Part Finished----^------------------------------------------------
	   
	   
//------------------------------------------------Drop down's Code Below-----------------------------------------------------
public boolean action(Event evt, Object whichAction)
		{if (!(evt.target instanceof Choice))	return false;   
		 Choice whichChoice = (Choice) evt.target;
			 
			 	if (whichChoice == choice){
			    	type_of_mashup=choice.getSelectedIndex();
			        repaint(); return true;
			      }//End of drop down 1
	   //-----------------------------
			    if (whichChoice == sort_state)	//For 2nd drop down...
			      { Sort_states_by=sort_state.getSelectedIndex();
			        int temp;int tempo[]=new int[35];
			        for(int pochi=0;pochi<35;pochi++)	tempo[pochi]=state_data[Sort_states_by][pochi];
			        	
			        for(int i=0;i<35;i++){temp=i;
			        		for(int j=i;j<35;j++) if(tempo[j]>tempo[temp]) temp=j;
			        		states[i]=states_cons[temp];
			        		tempo[temp]=-1;
			        	}//End of sort
			        
			        filldata();
			        repaint();return true;
			      }//End of drop down 2
	   //-----------------------------
			      //3rd drop down: sorting parties...
			     if (whichChoice == sort_party){
			    	  Sort_party_by=sort_party.getSelectedIndex();
			    	  int temp;int tempo[]=new int[39];		
			    	  for(int pochi=0;pochi<39;pochi++)	tempo[pochi]=party_data[Sort_party_by][pochi];
			    	  
			        	for(int i=0;i<39;i++){
			        		temp=i;
			        		for(int j=i;j<35;j++){
			        			if(tempo[j]>tempo[temp]) temp=j;
			        		}
			        		party[i]=party_cons[temp];
			        		tempo[temp]=-1;
			        	}//End of sort
			        	int initial=10*parties.getSelectedIndex();
				    	if (initial==30) initial--;
				    	party_which_ten(initial);
			        	filldata();
			        repaint();
			        return true;
			      }//End of drop down 3
		//-----------------------------      
			     if (whichChoice == parties){
				    	int initial=10*parties.getSelectedIndex();
				    	if (initial==30) initial--;
				    	party_which_ten(initial);
				    	filldata();
				        repaint(); return true;
				      }//End of 4th drop down
			      
			   return false;
		}//End of drop down's Code...
	   
	   /**
	    * Local function for 4th drop down
	   */
	   public void party_which_ten(int initial)		
	   {
	    	int temp;int tempo[]=new int[39];		
	    	for(int pochi=0;pochi<39;pochi++)	tempo[pochi]=party_data[Sort_party_by][pochi];
	    	
	    	for(int i=0;i<39;i++){
       		temp=i;
       		for(int j=i;j<35;j++){
       			if(tempo[j]>tempo[temp]) temp=j;
       		}
       		party[i]=party_cons[temp];
       		tempo[temp]=-1;
	    	}//End of sort
	    	
	    	for(int i=0;i<10;i++){
	    		party[i]=party[initial+i];
	    	}
	   }
//----------------------------------------------Drop-Down's Code finished----------------------------------------------------
	   
	   
	   
//---------------------------------------------------The painter (s)-------------------------------------------
/**
 * Paints the basic components of the graph i.e axes and party and state names...
 * @param g
 */
   public void paint_basic(Graphics g){	 
	 //Drawing Objects...
	 		g.setColor(Color.BLACK);
	 		Graphics2D g1 = (Graphics2D) g; 		// Type casting jugad...Interesting!!!

	 		g.drawLine(92, 550, 1200, 550);						//x-axis
	 		g.drawLine(92, 550, 92, 50);						//y-axis
	 		g.drawLine(1200, 550, 1200 - 3, 550 - 3);	//X-axis arrowhead
	 		g.drawLine(1200, 550, 1200 - 3, 550 + 3);	//X-axis arrowhead
	 		g.drawLine(92, 50, 92 + 3, 50 + 3);				//Y-axis arrowhead
	 		g.drawLine(92, 50, 92 - 3, 50 + 3);				//Y-axis arrowhead

	 		
	 		String temp;
	 		int end;
	 		
	 		//Drawing The TRUNCATED Names Of parties on y axis...(Horizontal Writing)
	 		for (int i = 0; i < 10; i++) {
	 			end=10;
	 			if(party[i].length()<10) end=party[i].length();
	 			temp=party[i].substring(0,end);
	 			g.drawString(temp, 10, 530 - i * 46);	
	 		}	

	 		//This is The reason why graphics2d object was been introduced :p
	 		//Drawing string rotated counter-clockwise 90 degrees	
	 		AffineTransform at = new AffineTransform();
	 		at.setToRotation(-Math.PI / 2.0);
	 		g1.setTransform(at);
	 		
	 		for (int i = 0; i < 24; i++) {
	 			end=10;
	 			if(states[i].length()<10) end=states[i].length();
	 			temp=states[i].substring(0,end);
	 			g1.drawString(temp, -height-50, 120 + i * 46);	
	 		}
	 		
	 		at.setToRotation(0);
	 		g1.setTransform(at); //Rotation Back to Normal
   }//End of paint_basic
	   
	   /**
	 * Helper function to paint()...It makes the varied sized balls...
	 * 
	 */
	public void Artist(Graphics g, int x, int y, int h) {
		h/=2;h*=2;		
		if (h > 24) h = 24;
		int X=92,Y=550;	//Co-ordinates of origin
		
		//Type 2: Circles Based on colour and size!
		g.setColor(spec[24-h]);
		g.fillOval(X+(x-1)*46+21-h, Y-46*y+21-h, 2*h+4, 2*h+4);
		
	}//End of Artist

	public void paint(Graphics g) {
		
		for(int i=0;i<N;i++)	//Display The gradient...OPTIONAL
			{g.setColor(spec[i]);
			 g.fillRect(850+10*i, 50, 4, 10);
			}
		
		//show means Showing the clicked ball...Making the highlights
		if(show==1) {show=0; 
			g.drawImage( backbuffer, 0, 0, this );	//Pushing the secondary buffer on the applet screen
			return;
			}
		
		paint_basic(g);//Make the graph components...
		//Finally making the balls of varying sizes...
		for(int i=0;i<24;i++){
			for(int j=0;j<10;j++){
				Artist(g, i+1, j+1, 24*data[type_of_mashup][i][j]/100);
			}
		}		
	}//End of Paint

//-----------------------------------------Data handler Function(s) Below----------------------------------------------------
	/**
     * makes a 2d array of what the ball size should be
     * @Fills in the global variable data[][][]
     */
	public static void filldata() {								//Time Complexity Improvement possible...Working preety fast!!
		for (int b = 0; b < states.length; b++) {	//Take a State
			for (int c = 0; c < 10; c++) {			//Take a Party
				
				data[0][b][c]=0; data[1][b][c]=0; data[2][b][c]=0; data[3][b][c]=0; data[4][b][c]=0;
				int N = 0, F=0;
				
				for (int i = 0; i < t.getRowCount(); i++) {	//Read The Table One By One...
					
					if (t.getString(i, 4).equals(states[b])
							&& t.getString(i, 6).equals(party[c])) {
						String s;
						
						data[1][b][c]+=t.getInt(i, 10);	//AGE
						data[2][b][c]+=t.getInt(i, 24);	//Rating
						if(t.getString(i, 7).equals("Female")) F++;//Sex Ratio
						
						//Questions...
						s=t.getString(i, 18);
						if(!s.equals("N/A")) data[4][b][c]+=Integer.parseInt(s);
						
						//Attendance...
						s = t.getString(i, 14);
						if(!s.equals("N/A")){
							String[] s1 = s.split("[%]+");
							data[0][b][c]+=Integer.parseInt(s1[0]);	
							}N++;//End_Attendance...
					}
				
				}//end of INNERMOST LOOP...(Table reading row by row)
				
				if(N != 0){//Calculating Averages...
					data[0][b][c]/=N;
					data[1][b][c]/=N;
					data[2][b][c]/=N;
					data[3][b][c]=100*F/N;
					data[4][b][c]=(100*data[4][b][c])/(N*214);
						}
			}//END of c loop
		}//END of b loop
	}//End of filldata()
	
}
