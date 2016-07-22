package test.ladder;

import java.util.ArrayList;
import java.util.Stack;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class OPMethod
{
	private int m_iSizeX;					// labyrinth width
	private int m_iSizeY;					// labyrinth height
	private int m_iWidth;					// path width
	private int m_iHeight;					// path height

	private ArrayList<Integer> m_aCellStatus;			// cell state
	public ArrayList<ArrayList<Integer>> m_aCellData;	// cell value
	public ArrayList<int[]> m_aPath;					// solve path
	
	public OPMethod(int iSizeX, int iSizeY, int iWidth, int iHeight)
	{
		this.m_iSizeX = iSizeX;
		this.m_iSizeY = iSizeY;
		this.m_iWidth = iWidth;
		this.m_iHeight = iHeight;
		this.m_aCellStatus = new ArrayList<Integer>(m_iSizeX * m_iSizeY);
		this.m_aCellData = new ArrayList<ArrayList<Integer>>();
		this.m_aPath = new ArrayList<int[]>();

		for(int i = 0; i < this.m_iSizeX; i++)
		{
			ArrayList<Integer> listLine = new ArrayList<Integer>();  
			for(int j = 0; j < this.m_iSizeY; j++)
			{
				listLine.add(0);
				this.m_aCellStatus.add(-1);
			}
			m_aCellData.add(listLine);  
		}
	}
	
	// create labyrinth by optimal path method
	public ArrayList<ArrayList<Integer>> createLabyrinth()
	{
		boolean bEnd = false;
		boolean bFound = false;
		int indexSrc;
		int indexDest;
		int tDir = 0;
		ArrayList<Integer> aNewCell = null;
		ArrayList<Integer> aActCell = null;
		Stack<ArrayList<Integer>> aStack = new Stack<ArrayList<Integer>>();
		
		// first cell
		aActCell = new ArrayList<Integer>(3);
		aActCell.add((int)random(0, this.m_iSizeX, 1));
		aActCell.add((int)random(0, this.m_iSizeY, 1));
		aActCell.add(0);
		
		while(true)
		{
			if((Integer)aActCell.get(2) == 15)
			{
				while((Integer)aActCell.get(2) == 15)
				{
					if(aStack.size() == 0)
					{
						bEnd = true;
						break;
					}
					aActCell = (ArrayList<Integer>)aStack.pop();
					if(aActCell == null) 
					{
						bEnd = true;
						break;
					}
				}
				if(bEnd == true) 
					break;
			}
			else
			{
				do
				{
					tDir = (int) Math.pow(2, (int)this.random(0, 3, 1));
					bFound = false;
					if(((Integer)aActCell.get(2) & tDir) != 0)
						bFound = true;
						
				}while(bFound == true && (Integer)aActCell.get(2) != 15);

				aActCell.set(2, (Integer)aActCell.get(2) | tDir);
				
				indexSrc = (Integer)aActCell.get(0) + (Integer)aActCell.get(1) * this.m_iSizeX;
				
				// left
				if(tDir == 1 && (Integer)aActCell.get(0) > 0)
				{
					indexDest = (Integer)aActCell.get(0) - 1 + (Integer)aActCell.get(1) * this.m_iSizeX;
					if(this.baseCell(indexSrc) != this.baseCell(indexDest))
					{
						this.m_aCellStatus.set(this.baseCell(indexDest), this.baseCell(indexSrc));
						int iTmp = this.m_aCellData.get((Integer)aActCell.get(0)).get((Integer)aActCell.get(1));
						this.m_aCellData.get((Integer)aActCell.get(0)).set((Integer)aActCell.get(1), iTmp | 2);
						
						aNewCell = copyCell(aActCell);
						aStack.push(aNewCell);
						aActCell.set(0, (Integer)aActCell.get(0) - 1);
						aActCell.set(2, 0);
					}
				}
	
				// right
				if(tDir == 2 && (Integer)aActCell.get(0) < this.m_iSizeX - 1)
				{
					indexDest = (Integer)aActCell.get(0) + 1 + (Integer)aActCell.get(1) * this.m_iSizeX;
					if(this.baseCell(indexSrc) != this.baseCell(indexDest))
					{
						this.m_aCellStatus.set(this.baseCell(indexDest), this.baseCell(indexSrc));
						int iTmp = this.m_aCellData.get((Integer)aActCell.get(0) + 1).get((Integer)aActCell.get(1));
						this.m_aCellData.get((Integer)aActCell.get(0) + 1).set((Integer)aActCell.get(1), iTmp | 2);

						aNewCell = copyCell(aActCell);
						aStack.push(aNewCell);
						aActCell.set(0, (Integer)aActCell.get(0) + 1);
						aActCell.set(2, 0);
					}
				}
	
				// up
				if(tDir == 4 && (Integer)aActCell.get(1) > 0)
				{
					indexDest = (Integer)aActCell.get(0) + ((Integer)aActCell.get(1) - 1) * this.m_iSizeX;
					if(this.baseCell(indexSrc) != this.baseCell(indexDest))
					{
						this.m_aCellStatus.set(this.baseCell(indexDest), this.baseCell(indexSrc));
						int iTmp = this.m_aCellData.get((Integer)aActCell.get(0)).get((Integer)aActCell.get(1));
						this.m_aCellData.get((Integer)aActCell.get(0)).set((Integer)aActCell.get(1), iTmp | 1);

						aNewCell = copyCell(aActCell);
						aStack.push(aNewCell);
						aActCell.set(1, (Integer)aActCell.get(1) - 1);
						aActCell.set(2, 0);
					}
				}
	
				// down
				if(tDir == 8 && (Integer)aActCell.get(1) < this.m_iSizeY - 1)
				{
					indexDest = (Integer)aActCell.get(0) + ((Integer)aActCell.get(1) + 1) * this.m_iSizeX;
					if(this.baseCell(indexSrc) != this.baseCell(indexDest))
					{
						this.m_aCellStatus.set(this.baseCell(indexDest), this.baseCell(indexSrc));
						int iTmp = this.m_aCellData.get((Integer)aActCell.get(0)).get((Integer)aActCell.get(1) + 1);
						this.m_aCellData.get((Integer)aActCell.get(0)).set((Integer)aActCell.get(1) + 1, iTmp | 1);

						aNewCell = copyCell(aActCell);
						aStack.push(aNewCell);
						aActCell.set(1, (Integer)aActCell.get(1) + 1);
						aActCell.set(2, 0);
					}
				}
			} 
		}
		
		return this.m_aCellData;
	}
	
	// copy cell content into a new 
	private ArrayList<Integer> copyCell(ArrayList<Integer> aOrig)
	{
		ArrayList<Integer>  aNewCell = new ArrayList<Integer>(3);
		aNewCell.add(aOrig.get(0));
		aNewCell.add(aOrig.get(1));
		aNewCell.add(aOrig.get(2));
		return aNewCell;
	}

	// serach base cell
	private int baseCell(int pIndex)
	{
		int index = pIndex;
		while((Integer)this.m_aCellStatus.get(index) >= 0)
			index = (Integer)this.m_aCellStatus.get(index);
		
		return index;
	}
	
	// solve the DSF labyrinth	
	public Path solveLabyrinth(int x, int y)
	{
		int ix = x / this.m_iWidth;
		int iy = y / this.m_iHeight;

		return this.solveLabyrinth(ix, iy, this.m_iSizeX - 1, this.m_iSizeY - 1);
	}	

	// solve the DSF labyrinth		
	public Path solveLabyrinth(int iStartX, int iStartY, int iDestX, int iDestY)
	{
		ArrayList<ArrayList<Integer>> tMazePath = new ArrayList<ArrayList<Integer>>(this.m_iSizeX);
		boolean destReached = false;

		int[] calcNextCPos = new int[2];
		int[] calcCPos = new int[2];
		int[] cellPos = new int[2];
		cellPos[0] = iStartX;
		cellPos[1] = iStartY;			

		ArrayList<int[]> calcState = new ArrayList<int[]>();
		calcState.add(cellPos);
		
		Path retPath = new Path();
		retPath.moveTo(this.m_iWidth * iDestX + this.m_iWidth / 2, this.m_iHeight * iDestY + this.m_iHeight / 2);

		int step = 0;
		for(int i = 0; i < this.m_iSizeX; i++)
		{
			ArrayList<Integer> listLine = new ArrayList<Integer>(); 
			for(int j = 0; j < this.m_iSizeY; j++)
			{
				listLine.add(-1);
			}
			tMazePath.add(listLine);			
		}
		tMazePath.get(iStartX).set(iStartY, step);	

		if(this.m_aCellData == null) 
			return null;
		
		if(iStartX == iDestX && iStartY == iDestY)
			return null;

		while(destReached == false && calcState.size() > 0)
		{
			step++;
			ArrayList<int[]> calcNextState = new ArrayList<int[]>();

			for(int i = 0; i < calcState.size(); i++)
			{					
				calcCPos = calcState.get(i);

				// up
				if( calcCPos[1] > 0 
					&& tMazePath.get(calcCPos[0]).get(calcCPos[1] - 1) == -1
					&& (this.m_aCellData.get(calcCPos[0]).get(calcCPos[1]) & 1) != 0)
				{
					tMazePath.get(calcCPos[0]).set(calcCPos[1] - 1, step);
					calcNextCPos = new int[2];
					calcNextCPos[0] = calcCPos[0];
					calcNextCPos[1] = calcCPos[1] - 1;
					calcNextState.add(calcNextCPos); 

					if(calcNextCPos[0] == iDestX && calcNextCPos[1] == iDestY) 
						destReached = true;
				}
				// left
				if( calcCPos[0] > 0 
					&& tMazePath.get(calcCPos[0] - 1).get(calcCPos[1]) == -1 
					&& (this.m_aCellData.get(calcCPos[0]).get(calcCPos[1]) & 2) != 0)
				{
					tMazePath.get(calcCPos[0] - 1).set(calcCPos[1], step);
					calcNextCPos = new int[2];
					calcNextCPos[0] = calcCPos[0] - 1;
					calcNextCPos[1] = calcCPos[1];
					calcNextState.add(calcNextCPos); 

					if(calcNextCPos[0] == iDestX && calcNextCPos[1] == iDestY) 
						destReached = true;
				}
				// down
				if( calcCPos[1] < this.m_iSizeY - 1 
					&& tMazePath.get(calcCPos[0]).get(calcCPos[1] + 1) == -1 
					&& (this.m_aCellData.get(calcCPos[0]).get(calcCPos[1] + 1) & 1) != 0)
				{
					tMazePath.get(calcCPos[0]).set(calcCPos[1] + 1, step);
					calcNextCPos = new int[2];
					calcNextCPos[0] = calcCPos[0];
					calcNextCPos[1] = calcCPos[1] + 1;
					calcNextState.add(calcNextCPos); 

					if(calcNextCPos[0] == iDestX && calcNextCPos[1] == iDestY)
						destReached = true;
				}
				// right
				if(calcCPos[0] < this.m_iSizeX - 1 
					&& tMazePath.get(calcCPos[0] + 1).get(calcCPos[1]) == -1 
					&& (this.m_aCellData.get(calcCPos[0] + 1).get(calcCPos[1]) & 2) != 0)
				{
					tMazePath.get(calcCPos[0] + 1).set(calcCPos[1], step);
					calcNextCPos = new int[2];
					calcNextCPos[0] = calcCPos[0] + 1;
					calcNextCPos[1] = calcCPos[1];
					calcNextState.add(calcNextCPos); 

					if(calcNextCPos[0] == iDestX && calcNextCPos[1] == iDestY)
						destReached = true;
				}
			}
				 
			calcState = calcNextState;
		}
		
		int tx = iDestX;
		int ty = iDestY;
		boolean stepExists;			
		if(destReached != false) 
		{
			tMazePath.get(iDestX).set(iDestY, step);

			calcNextCPos = new int[2];
			calcNextCPos[0] = tx;
			calcNextCPos[1] = ty;

			while(tx != iStartX || ty != iStartY)
			{
				step  = tMazePath.get(tx).get(ty);
				stepExists = false;
				
				// up
				if(ty > 0 && stepExists == false 
					&& tMazePath.get(tx).get(ty - 1) == step - 1 
					&& (this.m_aCellData.get(tx).get(ty) & 1) != 0)
				{
					ty -= 1; 
					stepExists = true;
					calcNextCPos = new int[2];
					calcNextCPos[0] = tx;
					calcNextCPos[1] = ty;
					addPath(tx, ty, retPath);
				}
				// left
				if(tx > 0 && stepExists == false 
					&& tMazePath.get(tx - 1).get(ty) == step - 1 
					&& (this.m_aCellData.get(tx).get(ty) & 2) != 0)
				{
					tx -= 1;
					stepExists = true;
					calcNextCPos = new int[2];
					calcNextCPos[0] = tx;
					calcNextCPos[1] = ty;
					addPath(tx, ty, retPath);
				}
				// down
				if(ty < this.m_iSizeY - 1 && stepExists == false 
					&& tMazePath.get(tx).get(ty + 1) == step - 1 
					&& (this.m_aCellData.get(tx).get(ty + 1) & 1) != 0)
				{
					ty += 1; 
					stepExists = true;
					calcNextCPos = new int[2];
					calcNextCPos[0] = tx;
					calcNextCPos[1] = ty;
					addPath(tx, ty, retPath);
				}
				// right
				if(tx < this.m_iSizeX - 1 && stepExists == false 
					&& tMazePath.get(tx + 1).get(ty) == step - 1 
					&& (this.m_aCellData.get(tx + 1).get(ty) & 2) != 0)
				{						
					tx += 1; 
					stepExists = true;
					calcNextCPos = new int[2];
					calcNextCPos[0] = tx;
					calcNextCPos[1] = ty;
					addPath(tx, ty, retPath);
				}

				if(stepExists == false)
					return null;
			}
		}
		
		return retPath;
	}
	
	// add path
	private void addPath(int tx, int ty, Path retPath)
	{
		int x = this.m_iWidth * tx + this.m_iWidth / 2;
		int y = this.m_iHeight * ty + this.m_iHeight / 2;
		retPath.lineTo(x, y);
		int [] calcCPos = new int[2];
		calcCPos[0] = x;
		calcCPos[1] = y;
		this.m_aPath.add(calcCPos);
	}
	
	// draw the labyrinth
	public boolean drawLabyrinth(Canvas canvas, int iOffX, int iOffY)
	{
		int xSize = this.m_iWidth;
		int ySize = this.m_iHeight;
		int i = 0, j = 0, c = 0;
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.WHITE);
		
		for(i = 0; i < this.m_iSizeX; i++)
		{
			for(j = 0; j < this.m_iSizeY; j++)
			{	
				c = this.m_aCellData.get(i).get(j);
				// horizontal
				if((c & 1) == 0)
				{
					canvas.drawLine(iOffX + xSize * i, 
							iOffY + ySize * j, 
							iOffX + xSize * (i + 1), 
							iOffY + ySize * j, 
							paint);
				}
				// vertical
				if((c & 2) == 0)
				{
					canvas.drawLine(iOffX + xSize * i, 
							iOffY + ySize * j, 
							iOffX + xSize * i, 
							iOffY + ySize * (j + 1), 
							paint);
				}
			}
		}
		
		canvas.drawLine(iOffX, iOffY + ySize * j, iOffX + xSize * i, iOffY + ySize * j, paint);
		canvas.drawLine(iOffX + xSize * i, iOffY + ySize * j, iOffX + xSize * i, iOffY, paint);

		return true;
	}

	// random number (from ACS)        
	private double random(double nMinimum, double nMaximum, int nRoundToInterval) 
	{
		if(nMinimum > nMaximum) 
		{
			double nTemp = nMinimum;
			nMinimum = nMaximum;
			nMaximum = nTemp;
		}
	
		double nDeltaRange = (nMaximum - nMinimum) + (1 * nRoundToInterval);
		double nRandomNumber = (int) (Math.random() * nDeltaRange);
	
		nRandomNumber += nMinimum;
		
		double nRet = Math.floor(nRandomNumber / nRoundToInterval) * nRoundToInterval;
	
		return nRet;
	} 
}
