//
//
package org.landroo.ladder;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;

public class LadderClass
{
	private Canvas mCanvas = null;// drawing canvas
	private OPMethod mOPMethod = null;// maze class
	
	private int miTableSizeX;// maze width
	private int miTableSizeY;// maze length
	private int miCellWidth;// cell width
	private int miCellHeight;// cell height
	
	private ArrayList<ArrayList<Integer>> m_aCellData = null;// block values
	private ArrayList<ArrayList<Integer>> m_aLevelData = new ArrayList<ArrayList<Integer>>();// level parts
	
	public int m_iStX = 0;
	public int m_iStY = 0;
	public int m_iEdX = 0;
	public int m_iEdY = 0;
	
	private Paint paintLadder = new Paint();
	private Paint paintFloor = new Paint();
	private Paint paintDoor = new Paint();
	
	private Bitmap[] bitmaps = new Bitmap[16];
	
	public LadderClass(Canvas canvas, int iWidth, int iHeight)
	{
		this.mCanvas = canvas;
		this.miCellWidth = iWidth;
		this.miCellHeight = iHeight;
					
		this.miTableSizeX = canvas.getWidth() / this.miCellWidth;
		this.miTableSizeY = canvas.getHeight() / this.miCellHeight;
		
		this.mOPMethod = new OPMethod(this.miTableSizeX, this.miTableSizeY, this.miCellWidth, this.miCellHeight);
		
		this.paintLadder.setColor(Color.GREEN);
		this.paintLadder.setStyle(Paint.Style.STROKE);
		this.paintLadder.setStrokeWidth(2);
		
		this.paintFloor.setColor(Color.BLUE);
		this.paintFloor.setStyle(Paint.Style.STROKE);
		this.paintFloor.setStrokeWidth(4);
		
		this.paintDoor.setColor(Color.WHITE);
		
		bitmaps[0] = darwLadder01(miCellWidth, miCellHeight);
		bitmaps[1] = darwLadder02(miCellWidth, miCellHeight);
		bitmaps[2] = darwLadder03(miCellWidth, miCellHeight);
		bitmaps[3] = darwLadder04(miCellWidth, miCellHeight);
		bitmaps[4] = darwLadder05(miCellWidth, miCellHeight);
		bitmaps[5] = darwLadder06(miCellWidth, miCellHeight);
		bitmaps[6] = darwLadder07(miCellWidth, miCellHeight);
		bitmaps[7] = darwLadder08(miCellWidth, miCellHeight);
		bitmaps[8] = darwLadder09(miCellWidth, miCellHeight);
		bitmaps[9] = darwLadder10(miCellWidth, miCellHeight);
		bitmaps[10] = darwLadder11(miCellWidth, miCellHeight);
		bitmaps[11] = darwLadder12(miCellWidth, miCellHeight);
		bitmaps[12] = darwLadder13(miCellWidth, miCellHeight);
		bitmaps[13] = darwLadder14(miCellWidth, miCellHeight);
		bitmaps[14] = darwLadder15(miCellWidth, miCellHeight);
		bitmaps[15] = Bitmap.createBitmap(miCellWidth, miCellHeight, Bitmap.Config.RGB_565);

		this.drawLadders();
		
//		this.m_cCanvas.addChild(drawDoor());

//		ar:ArrayCollection =  this.m_OPMethod.solveLabyrinth(this.m_iStX, this.m_iStY, this.m_iEdX, this.m_iEdY);
//		this.drawSolve(this.m_cCanvas, ar, 0, this.miHeight / 4);
	}
	
	// solve maze
	public void drawSolve(Canvas canvas, ArrayList<int[]> ar, int iOffX, int iOffY)
	{
		int[] aCell1;
		int[] aCell2;
		Paint paint = new Paint();
		 
		for(int i = 1; i < ar.size(); i++)
		{
			aCell1 = ar.get(i - 1);
			aCell2 = ar.get(i);
			canvas.drawLine(iOffX + this.miCellWidth * aCell1[0] + this.miCellWidth / 2, 
					iOffY + this.miCellHeight * aCell1[1] + this.miCellHeight / 2,  
					iOffX + this.miCellWidth * aCell2[0] + this.miCellWidth / 2, 
					iOffY + this.miCellHeight * aCell2[1] + this.miCellHeight / 2, paint);
		}
	}
		
	// draw maze
	public void drawLadders()
	{
		this.m_aCellData = this.mOPMethod.createLabyrinth();

		for(int i = 0; i < this.miTableSizeX; i++)
			for(int j = 0; j < this.miTableSizeY; j++)
				mCanvas.drawBitmap(getCell(i, j), i * miCellWidth, j * miCellHeight + miCellHeight / 2, paintLadder);
		
//		if(this.addDoors(true) == false) this.drawLadders();
//		if(this.addDoors(false) == false) this.drawLadders();
	}
	
	// add doors
	private boolean addDoors(boolean st)
	{
		Bitmap bitmap;
		
		if(st == true)
		{
			for(int j = 0; j < this.miTableSizeY; j++)
			{
				for(int i = 0; i < this.miTableSizeX; i++)
				{
					if(this.m_aLevelData.get(i).get(j) == 5 
					|| this.m_aLevelData.get(i).get(j) == 11
					|| this.m_aLevelData.get(i).get(j) == 12)
					{
						bitmap = drawDoor(this.miCellWidth, this.miCellHeight);
						this.mCanvas.drawBitmap(bitmap, i * this.miCellWidth, j * this.miCellHeight, paintDoor); 
						this.m_iStX = i;
						this.m_iStY = j;
						
						return true;
					}
				}
			}
		}
		else
		{
			for(int j = this.miTableSizeY - 1; j >= 0 ; j--)
			{
				for(int i = this.miTableSizeX - 1; i >= 0 ; i--)
				{
					if(this.m_aLevelData.get(i).get(j) == 5
					|| this.m_aLevelData.get(i).get(j) == 11
					|| this.m_aLevelData.get(i).get(j) == 12)
					{
						bitmap = drawDoor(this.miCellWidth, this.miCellHeight);
						this.mCanvas.drawBitmap(bitmap, i * this.miCellWidth, j * this.miCellHeight, paintDoor); 
						this.m_iEdX = i;
						this.m_iEdY = j;
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	// draw doors
	private Bitmap drawDoor(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		
		canvas.drawLine(w / 2 - w / 8 - 2, h / 3, w / 2 - w / 8 - 2, h, paintDoor);
		canvas.drawLine(w / 2 + w / 8 + 2, h / 3, w / 2 + w / 8 + 2, h, paintDoor);
		canvas.drawLine(w / 2 - w / 8 - 2, h / 3, w / 2 + w / 8 + 2, h / 3, paintDoor);
		
		return bitmap;
	}
	
	// from right to down, from down to right
	private Bitmap darwLadder01(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// from left to down, from down to left
	private Bitmap darwLadder02(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}

	// from up to left, from left to up
	private Bitmap darwLadder03(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// from up to right, from right to up
	private Bitmap darwLadder04(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// horizontal
	private Bitmap darwLadder05(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// vertical
	private Bitmap darwLadder06(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		
		return bitmap;
	}
	
	// from left to right and down
	private Bitmap darwLadder07(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// from up to down and right
	private Bitmap darwLadder08(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// from up to down an left
	private Bitmap darwLadder09(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// from right to left and up
	private Bitmap darwLadder10(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	} 

	// end from right
	private Bitmap darwLadder11(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawLine(0, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	} 
	
	// end from left
	private Bitmap darwLadder12(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawLine(w / 4, h / 2, w, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// end from down
	private Bitmap darwLadder13(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(w / 4, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// end from up
	private Bitmap darwLadder14(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		canvas.drawLine(w / 4, h / 2, w / 4 * 3, h / 2, paintFloor);
				
		return bitmap;
	}
	
	// all direction
	private Bitmap darwLadder15(int w, int h)
	{
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Ladder(canvas, 0, w, h);
		Ladder(canvas, h / 2, w, h);
		canvas.drawLine(0, h / 2, w, h / 2, paintFloor);
		
		return bitmap;
	}

	// half ladder
	private void Ladder(Canvas canvas, int o, int w, int h)
	{
		canvas.drawLine(w / 2 - w / 8, o, w / 2 - w / 8, o + h / 2, paintLadder);
		canvas.drawLine(w / 2 + w / 8, o, w / 2 + w / 8, o + h / 2, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o, w / 2 + w / 8, o, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o + h / 8, w / 2 + w / 8, o + h / 8, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o + h / 4, w / 2 + w / 8, o + h / 4, paintLadder);
		canvas.drawLine(w / 2 - w / 8, o + h / 4 + h / 8, w / 2 + w / 8, o + h / 4 + h / 8, paintLadder);
	}
	
	// return the type of a cell
	public Bitmap getCell(int x, int y)
	{
		 Bitmap bitmap = null;
		 boolean bLeft = false;
		 boolean bRight = false;
		 boolean bUp = false;
		 boolean bDown = false;

		// left wall
		if((this.m_aCellData.get(x).get(y) & 2) == 0) bLeft = true;
		// right wall or end
		if(x + 1 == this.miTableSizeX) bRight = true; 
		else if((this.m_aCellData.get(x + 1).get(y) & 2) == 0) bRight = true;
		// up wall
		if((this.m_aCellData.get(x).get(y) & 1) == 0) bUp = true;
		// down wall or end
		if(y + 1 == this.miTableSizeY) bDown = true; 
		else if((this.m_aCellData.get(x).get(y + 1) & 1) == 0) bDown = true;
	
		if( bLeft && !bRight &&  bUp && !bDown) bitmap = bitmaps[ 0];// from right to down, left and up wall
		if(!bLeft &&  bRight &&  bUp && !bDown) bitmap = bitmaps[ 1];// from left to down, right and up wall
		if(!bLeft &&  bRight && !bUp &&  bDown) bitmap = bitmaps[ 2];// from up to left, right and down wall
		if( bLeft && !bRight && !bUp &&  bDown) bitmap = bitmaps[ 3];// from up to right, left and down wall
		if(!bLeft && !bRight &&  bUp &&  bDown) bitmap = bitmaps[ 4];// horizontal, up and down wall
		if( bLeft &&  bRight && !bUp && !bDown) bitmap = bitmaps[ 5];// vertical, left and right wall
		if(!bLeft && !bRight &&  bUp && !bDown) bitmap = bitmaps[ 6];// from left down and right, up wall
		if( bLeft && !bRight && !bUp && !bDown) bitmap = bitmaps[ 7];// from up right and down, left wall
		if(!bLeft &&  bRight && !bUp && !bDown) bitmap = bitmaps[ 8];// from up left and down, right wall
		if(!bLeft && !bRight && !bUp &&  bDown) bitmap = bitmaps[ 9];// from up right and left, down wall
		if(!bLeft &&  bRight &&  bUp &&  bDown) bitmap = bitmaps[10];// from right end, left and up and down wall
		if( bLeft && !bRight &&  bUp &&  bDown) bitmap = bitmaps[11];// from left end, right and up and down wall
		if( bLeft &&  bRight &&  bUp && !bDown) bitmap = bitmaps[12];// from down end, left and right and up wall
		if( bLeft &&  bRight && !bUp &&  bDown) bitmap = bitmaps[13];// from up end, left and right and down wall
		if(!bLeft && !bRight && !bUp && !bDown) bitmap = bitmaps[14];// no wall
		
		return bitmap;			
	}

	// draw background image
	public static Bitmap getBackGround(float w, float h)
	{
		Bitmap bitmap = Bitmap.createBitmap((int)w, (int)h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		RectF rect = new RectF();
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		
		int[] colors = new int[2];
		colors[0] = 0xFF555555;
		colors[1] = 0xFFFF5555;
		
		float bw = w / 20;
		float bh = h / 80;

		LinearGradient grad = new LinearGradient(0, 0, bw, bh, colors, null, android.graphics.Shader.TileMode.CLAMP);
		paint.setShader(grad);
	
		for(int i = 0; i < 21; i++)
		{
			for(int j = 0; j < 80; j++)
			{
				if(j % 2 == 0)
				{
					grad = new LinearGradient(i * bw + 2, j * bh + 2, i * bw + bw - 2, j * bh + bh - 2, colors, null, android.graphics.Shader.TileMode.CLAMP);
					paint.setShader(grad);
					rect.set(i * bw + 2, j * bh + 2, i * bw + bw - 2, j * bh + bh - 2);
				}
				else
				{
					grad = new LinearGradient(i * bw + 2 - bw / 2, j * bh + 2, i * bw + bw - 2 - bw / 2, j * bh + bh - 2, colors, null, android.graphics.Shader.TileMode.CLAMP);
					paint.setShader(grad);
					rect.set(i * bw + 2 - bw / 2, j * bh + 2, i * bw + bw - 2 - bw / 2, j * bh + bh - 2);
				}
				canvas.drawRect(rect, paint);
			}
		}

		return bitmap;
	}

}
