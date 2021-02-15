package com.gdx.others;

import com.gdx.common.Constant;
import com.gdx.screen.GameScreen;

public class Move {
	
	//-----------------------------
	GameScreen mGameScreen;
	//-----------------------------
	int mark_move[];
	
	private int MoveMark;
	public int mColumn_num=0;
	public int mColumn=0;
	
	public float startTime;
	final float gravity_x;
	public float v_x;
	
	public int x_old;
	
	public int x_now;
	
	public int x_last;
	public int mColumn_last=0;
	
	public  Move(int mColumn_, int mColumn_num_, int x_old_, int x_last_, GameScreen mGameScreen_){
		mark_move=new int[mColumn_+1];
		mColumn=mColumn_;
		mColumn_last=mColumn;
		mColumn_num=mColumn_num_;
		x_now=x_old_;
		x_old=x_old_;
		x_last=x_last_;
		mGameScreen=mGameScreen_;
		gravity_x=mGameScreen_.gravity_x;
		startTime=0;
		setQuiet();
		//--LogW.e("zddz_002", "--Move--Move--mColumn-->"+mColumn+"--x_last-->"+x_last);
	}
	
	//========================================
	
	/**
	 * 一个移动周期--只会执行一次
	 * @param startColumn
	 * @param endColumn
	 * @param differ
	 */
	public void setReady_Data(int startColumn, int endColumn, int differ) {
		/*LogW.e("zddz_002", "--Move--setReady_Data--startColumn-->"+startColumn);
		LogW.e("zddz_002", "--Move--setReady_Data--endColumn-->"+endColumn);
		LogW.e("zddz_002", "--Move--setReady_Data--differ-->"+differ);
		LogW.e("zddz_002", "--Move--setReady_Data--x_last-->"+x_last);
		LogW.e("zddz_002", "--Move--setReady_Data--mColumn_last-->"+mColumn_last);
		LogW.e("zddz_002", "--Move--setReady_Data--mColumn-->"+mColumn);
		LogW.e("zddz_002", "--Move--setReady_Data--mark_move.length-->"+mark_move.length);*/
		
		for(int i=startColumn;i>=endColumn;i--){
			mark_move[i]=1;
		}
		MoveMark=Constant.StarState_Ready;
		startTime=0;
		v_x=0;
	}
	
	public void setPause(){
		//--LogW.e("zddz_002", "--Move--setPause--001--x_now-->"+x_now);
		MoveMark=Constant.StarState_Pause;
		startTime=0;
		v_x=0;
	}
	
	public void setMove(){
		//--LogW.e("zddz_002", "--Move--setMove--001--mColumn_last-->"+mColumn_last);
		//--LogW.e("zddz_002", "--Move--setMove--001--x_last-->"+x_last);
		
		MoveMark=Constant.StarState_Move;
		int num=0;
		for(int i=mColumn_last-1;;i--){
			if(i>=0){
				//--LogW.e("zddz_002", "--Move--setMove--001--i-->"+i);
				if(mark_move[i]==1){//--
					num++;
				}else{
					break;
				}
			}else{
				break;
			}
		}
		int totalStep=num*Constant.StarStep;
		x_last=x_last-totalStep;
		mColumn_last=mColumn_last-num;
		if(mColumn_last<0){
			mColumn_last=0;
		}
		
		/*LogW.e("zddz_002", "--Move--setMove--num-->"+num);
		LogW.e("zddz_002", "--Move--setMove--totalStep-->"+totalStep);
		LogW.e("zddz_002", "--Move--setMove--x_last-->"+x_last);
		LogW.e("zddz_002", "--Move--setMove--mColumn_last-->"+mColumn_last);*/
	}
	
	public void setQuiet(){
		MoveMark=Constant.StarState_Quiet;
	}
	
	public void set_Data(int startColumn, int endColumn, int differ) {
		for(int i=startColumn;i>=endColumn;i--){
			mark_move[i]=1;
		}
	}
	
	//---------------------------
	
	public int getMoveMark(){
		return MoveMark;
	}
	
	//========================================
	
	public void setColumn(int index){
		mColumn=index;
	}
	
	public void setColumn_num(int num){
		mColumn_num=num;
	}
	
	public void update(float deltaTime){
		//--LogW.i("zddz_001", "--Move--update--001-->>");
		startTime+=deltaTime;
		v_x   = v_x + gravity_x * deltaTime;
		x_now = (int) (x_now + v_x * deltaTime);
		
		if(mColumn>0){
			Move mMove=mGameScreen.ColumnMove[mColumn-1];
			if(mMove!=null){
				if(x_now<=(mMove.x_now+Constant.StarStep+1)){
					x_now=mMove.x_now+Constant.StarStep;
				}
			}
		}
		
		if(x_now<=x_last){
			x_old=x_last;
			x_now=(int) x_last;
			v_x=0;
			setQuiet();
		}
	}
	
}
