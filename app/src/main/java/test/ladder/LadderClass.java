//
//
package test.ladder;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class LadderClass
{
	private Canvas m_cCanvas = null;		// A rajzterület
	private OPMethod m_OPMethod = null;		// A labirintus készítő
	
	private int m_iSizeX;					// A labirintus szélessége
	private int m_iSizeY;					// A labairintus magassága
	private int m_iWidth;					// Az út szélessége
	private int m_iHeight;					// Az út magassága
	
	private ArrayList<ArrayList<Integer>> m_aCellData = null;		// Mező értéke
	private ArrayList<ArrayList<Integer>> m_aLevelData = new ArrayList<ArrayList<Integer>>();	// A pálya elemei
	
	private int m_iStX = 0;
	private int m_iStY = 0;
	private int m_iEdX = 0;
	private int m_iEdY = 0;
	
	private Paint paintLadder = new Paint();
	private Paint paintFloor = new Paint();
	private Paint paintDoor = new Paint();
	
	public LadderClass(Canvas canvas, int iWidth, int iHeight)
	{
		this.m_cCanvas = canvas;
		this.m_iWidth = iWidth;
		this.m_iHeight = iHeight;
					
		this.m_iSizeX = canvas.getWidth() / this.m_iWidth;
		this.m_iSizeY = canvas.getHeight() / this.m_iHeight;
		
		this.m_OPMethod = new OPMethod(this.m_iSizeX, this.m_iSizeY, this.m_iWidth, this.m_iHeight);
		
		this.drawLadders();
/*		
		this.m_cCanvas.addChild(darwLadder01());
		this.m_cCanvas.addChild(darwLadder02());
		this.m_cCanvas.addChild(darwLadder03());
		this.m_cCanvas.addChild(darwLadder04());
		this.m_cCanvas.addChild(darwLadder05());
		this.m_cCanvas.addChild(darwLadder06());
		this.m_cCanvas.addChild(darwLadder07());
		this.m_cCanvas.addChild(darwLadder08());
		this.m_cCanvas.addChild(darwLadder09());
		this.m_cCanvas.addChild(darwLadder10());
		this.m_cCanvas.addChild(darwLadder11());
		this.m_cCanvas.addChild(darwLadder12());
		this.m_cCanvas.addChild(darwLadder13());
		this.m_cCanvas.addChild(darwLadder14());
		this.m_cCanvas.addChild(darwLadder15());
*/		
//		this.m_cCanvas.addChild(drawDoor());

//		ar:ArrayCollection =  this.m_OPMethod.solveLabyrinth(this.m_iStX, this.m_iStY, this.m_iEdX, this.m_iEdY);
//		this.drawSolve(this.m_cCanvas, ar, 0, this.m_iHeight / 4);
	}
	
	// Kirajzolja a bejárást
	public void drawSolve(Canvas canvas, ArrayList<int[]> ar, int iOffX, int iOffY)
	{
		int[] aCell1;
		int[] aCell2;
		Paint paint = new Paint();
		 
		for(int i = 1; i < ar.size(); i++)
		{
			aCell1 = ar.get(i - 1);
			aCell2 = ar.get(i);
			canvas.drawLine(iOffX + this.m_iWidth * aCell1[0] + this.m_iWidth / 2, 
					iOffY + this.m_iHeight * aCell1[1] + this.m_iHeight / 2,  
					iOffX + this.m_iWidth * aCell2[0] + this.m_iWidth / 2, 
					iOffY + this.m_iHeight * aCell2[1] + this.m_iHeight / 2, paint);
		}
	}
		
	// Pálya kirajzolása
	public void drawLadders()
	{
		Bitmap bitmap;
		this.m_aCellData = this.m_OPMethod.createLabyrinth();

		for(int i = 0; i < this.m_iSizeX; i++)
		{
			for(int j = 0; j < this.m_iSizeY; j++)
			{
				bitmap = this.getCell(i, j);
				if(bitmap != null)
				{
					this.m_cCanvas.drawBitmap(bitmap, i * this.m_iWidth, j * this.m_iHeight + this.m_iHeight / 2, paintLadder);
				}
			}
		}
		
		if(this.addDoors(true) == false)
		{
			this.drawLadders();
		}	
		if(this.addDoors(false) == false)
		{
			this.drawLadders();
		}
	}
	
	// Ajtók hozzáadása
	private boolean addDoors(boolean st)
	{
		Bitmap bitmap;
		
		if(st == true)
		{
			for(int j = 0; j < this.m_iSizeY; j++)
			{
				for(int i = 0; i < this.m_iSizeX; i++)
				{
					if(this.m_aLevelData.get(i).get(j) == 5 
					|| this.m_aLevelData.get(i).get(j) == 11
					|| this.m_aLevelData.get(i).get(j) == 12)
					{
						bitmap = drawDoor(this.m_iWidth, this.m_iHeight);
						this.m_cCanvas.drawBitmap(bitmap, i * this.m_iWidth, j * this.m_iHeight, paintDoor); 
						this.m_iStX = i;
						this.m_iStY = j;
						
						return true;
					}
				}
			}
		}
		else
		{
			for(int j = this.m_iSizeY - 1; j >= 0 ; j--)
			{
				for(int i = this.m_iSizeX - 1; i >= 0 ; i--)
				{
					if(this.m_aLevelData.get(i).get(j) == 5
					|| this.m_aLevelData.get(i).get(j) == 11
					|| this.m_aLevelData.get(i).get(j) == 12)
					{
						bitmap = drawDoor(this.m_iWidth, this.m_iHeight);
						this.m_cCanvas.drawBitmap(bitmap, i * this.m_iWidth, j * this.m_iHeight, paintDoor); 
						this.m_iEdX = i;
						this.m_iEdY = j;
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	// Ajtó kirajzolása
	private Bitmap drawDoor(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		
		canvas.drawLine(w / 2 - w / 8 - 2, h / 3, w / 2 - w / 8 - 2, h, paintDoor);
		canvas.drawLine(w / 2 + w / 8 + 2, h / 3, w / 2 + w / 8 + 2, h, paintDoor);
		canvas.drawLine(w / 2 - w / 8 - 2, h / 3, w / 2 + w / 8 + 2, h / 3, paintDoor);
		
		return bitmap;
	}
	
	// jobról le, lentről jobbra
	private Bitmap darwLadder01(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// balról le
	// lentől balra
	private Bitmap darwLadder02(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}

	// fentről balra
	// balról fel
	private Bitmap darwLadder03(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// fentről jobbra, balról fel
	private Bitmap darwLadder04(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// vízszintes			
	private Bitmap darwLadder05(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// fügőleges
	private Bitmap darwLadder06(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		
		return bitmap;
	}
	
	// balról jobbra és le
	private Bitmap darwLadder07(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// fentről le és jobbra
	private Bitmap darwLadder08(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// fentről le és balra
	private Bitmap darwLadder09(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// jobbról balra és fel
	private Bitmap darwLadder10(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	} 

	// jobbról vég
	private Bitmap darwLadder11(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	} 
	
	// balról vég
	private Bitmap darwLadder12(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// lentről vég
	private Bitmap darwLadder13(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(w / 4, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// fentről vég
	private Bitmap darwLadder14(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(w / 4, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// jobbról balra és fel és le
	private Bitmap darwLadder15(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
		
		return bitmap;
	}

	// fél létra		
	private void Ladder(Canvas canvas, int o, int w, int h)
	{
		canvas.drawLine(w / 2 - w / 8, o, w / 2 - w / 8, o + h / 2, paintLadder);
		canvas.drawLine(w / 2 + w / 8, o, w / 2 + w / 8, o + h / 2, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o, w / 2 + w / 8, o, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o + h / 8, w / 2 + w / 8, o + h / 8, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o + h / 4, w / 2 + w / 8, o + h / 4, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o + h / 4 + h / 8, w / 2 + w / 8, o + h / 4 + h / 8, paintLadder);
	}
	
	// Visszaad egy cellát
	public Bitmap getCell(int x, int y)
	{
		 Bitmap bitmap = null;
		 boolean bLeft = false;
		 boolean bRight = false;
		 boolean bUp = false;
		 boolean bDown = false;

		// balra fal
		if((this.m_aCellData.get(x).get(y) & 2) == 0)
		{
			bLeft = true;
		}
		// jobbra fal vagy pályavég
		if(x + 1 == this.m_iSizeX)
		{
			bRight = true;
		}
		else if((this.m_aCellData.get(x + 1).get(y) & 2) == 0)
		{
			bRight = true;
		}	
		// fent fal
		if((this.m_aCellData.get(x).get(y) & 1) == 0)
		{
			bUp = true; 
		}
		// lent fal vagy pályavég
		if(y + 1 == this.m_iSizeY)
		{
			bDown = true;
		}
		else if((this.m_aCellData.get(x).get(y + 1) & 1) == 0)
		{
			bDown = true;
		}	
		// jobról le, balra, fent fal
		if(bLeft == true && bRight == false && bUp == true && bDown == false)
		{
			bitmap = darwLadder01(this.m_iWidth, this.m_iHeight);
		}	
		// balról le, jobbra, fent fal
		if(bLeft == false && bRight == true && bUp == true && bDown == false)
		{
			bitmap = darwLadder02(this.m_iWidth, this.m_iHeight);
		}	
		// fentről balra, jobbra, lent fal
		if(bLeft == false && bRight == true && bUp == false && bDown == true)
		{
			bitmap = darwLadder03(this.m_iWidth, this.m_iHeight);
		}	
		// fentről jobbra, balra, lent fal
		if(bLeft == true && bRight == false && bUp == false && bDown == true)
		{
			bitmap = darwLadder04(this.m_iWidth, this.m_iHeight);
		}	
		// vízszintes, fent, lent fal			
		if(bLeft == false && bRight == false && bUp == true && bDown == true)
		{
			bitmap = darwLadder05(this.m_iWidth, this.m_iHeight);
		}	
		// fügőleges, balra, jobbra fal
		if(bLeft == true && bRight == true && bUp == false && bDown == false)
		{
			bitmap = darwLadder06(this.m_iWidth, this.m_iHeight);
		}	
		// balról le és jobbra, fent fal
		if(bLeft == false && bRight == false && bUp == true && bDown == false)
		{
			bitmap = darwLadder07(this.m_iWidth, this.m_iHeight);
		}	
		// fentről jobbra és le, balra fal
		if(bLeft == true && bRight == false && bUp == false && bDown == false)
		{
			bitmap = darwLadder08(this.m_iWidth, this.m_iHeight);
		}	
		// fentről balra és le, jobbra fal
		if(bLeft == false && bRight == true && bUp == false && bDown == false)
		{
			bitmap = darwLadder09(this.m_iWidth, this.m_iHeight);
		}	
		// fentről jobbra és balra, lenn fal
		if(bLeft == false && bRight == false && bUp == false && bDown == true)
		{
			bitmap = darwLadder10(this.m_iWidth, this.m_iHeight);
		}	
		// jobbról vég, jobbra, fenn, lenn fal
		if(bLeft == false && bRight == true && bUp == true && bDown == true)
		{
			bitmap = darwLadder11(this.m_iWidth, this.m_iHeight);
		}	
		// balról vég, balra, fenn, lenn fal
		if(bLeft == true && bRight == false && bUp == true && bDown == true)
		{
			bitmap = darwLadder12(this.m_iWidth, this.m_iHeight);
		}	
		// lentről vég, balra, jobbra, fenn fal
		if(bLeft == true && bRight == true && bUp == true && bDown == false)
		{
			bitmap = darwLadder13(this.m_iWidth, this.m_iHeight);
		}	
		// fentről vég, balra, jobbra, lenn fal
		if(bLeft == true && bRight == true && bUp == false && bDown == true)
		{
			bitmap = darwLadder14(this.m_iWidth, this.m_iHeight);
		}	
		// fentről le jobbra és balra, nincs fal
		if(bLeft == false && bRight == false && bUp == false && bDown == false)
		{
			bitmap = darwLadder15(this.m_iWidth, this.m_iHeight);
		}
		
		return bitmap;			
	}
	
    // Játékmező átméretezésének lekezelése
    public void setSize(int iCW, int iCH)
    {
//			this.m_iSizeX = iCW / this.m_iWidth;
//			this.m_iSizeY = iCH / this.m_iHeight;
    }
}
