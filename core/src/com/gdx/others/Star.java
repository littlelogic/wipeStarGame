package com.gdx.others;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.common.Constant;
import com.gdx.screen.GameScreen;

public class Star {
	
	public int select_hint=0;
	public int check_hint=0;
	
	//==================================
	
	public int num_others=0;
	
	public int mColumn=0;
	public int mRow=0;
	public int color=1;
	public Color mColor;
	
	public int x;
	public int y;
	public int y_old;

	public int fall_type;
	public final int fall_type_differ=1;
	public final int fall_follow=1;
	public final int fall_free=2;
	
	//-------------
	public int select=0;//是否被选中
	public int check=0;//点击时，检查是否有过核查过
	//---------------
	public TextureRegion mRegion;
	public TextureRegion mSelectRegion;
	GameScreen mGameScreen;
	//---------------
	public float startTime;
	
	public float finalFall_y;
	
	/**
	 * 0--静止状态--非移动，或准备移动状态
	 * 1--准备移动状态
	 * 2--移动状态
	 */
	public int move_state_x=Constant.StarState_Quiet;
	public int move_state_y=Constant.StarState_Quiet;
	public boolean AnimaMove=false;
	
	//=======================================================================
	
	public Star(int row_, int column_, int color_, Color mColor_, TextureRegion mRegion_,
                TextureRegion mSelectRegion_, GameScreen mGameScreen_){
		mRow=row_;
		mColumn=column_;
		color=color_;
		mColor=mColor_;
		mRegion=mRegion_;
		mSelectRegion=mSelectRegion_;
		mGameScreen=mGameScreen_;
		gravity_y=mGameScreen_.gravity_y;
		star_v_y_initial=mGameScreen_.star_v_y_initial;
		//--initToUpdate();
		//--setToUpdate(mToDraw_self);
	}

	//=======================================================================
	
	public float scale_time=1f;

	//=======================================================================
	
	public void setStarData(int row_,int column_){
		mRow=row_;
		mColumn=column_;
	}
	
	public void setY(int value){
		y=y_old=value;
	}
	//---------------
	
	public void setFinalFall_y_Ready(float value){
		finalFall_y=value;
		move_state_y=Constant.StarState_Ready;
		AnimaMove=true;
		startTime=0;
		v_y=0;
		y_old=y;
		fall_type=fall_free;
		v_y=star_v_y_initial;
	}
	
	public void setFallReady_differ(int value){
		finalFall_y=y-value;
		move_state_y=Constant.StarState_Ready;
		AnimaMove=true;
		startTime=0;
		v_y=0;
		y_old=y;
		fall_type=fall_free;
		
		v_y=star_v_y_initial;
	}
	
	public void setColumnMove(){
		move_state_x=Constant.StarState_Ready;
		AnimaMove=true;
	}
	
	public void setFinalFall_y_differ(int value){
		finalFall_y=finalFall_y-value;
	}
	
	final  float gravity_y;
	public float v_y;
	float star_v_y_initial;
	
	public void update_Y__old(float deltaTime){
		startTime+=deltaTime;
		v_y = v_y + gravity_y * deltaTime;
		y   = (int) (y + v_y * deltaTime);
		if(mRow<9){
			Star mStar=mGameScreen.StarArray[mRow+1][mColumn];
			if(mStar!=null){
				if(y<=(mStar.y+Constant.StarStep+fall_type_differ)){
					y=mStar.y+Constant.StarStep;
				}
			}
		}
		
		if(y<=finalFall_y){
			y=(int) finalFall_y;
			y_old=y;
			v_y=0;
			move_state_y=Constant.StarState_Quiet;
			if(move_state_x==Constant.StarState_Quiet){
				AnimaMove=false;
			}
		}
	}

	public void update_Y(float deltaTime) {
		startTime += deltaTime;
		//------
		float v_y_now = v_y + gravity_y * deltaTime;
		float v_y_a = (v_y + v_y_now) / 2;
		y = (int) (y + v_y_a * deltaTime);
		v_y = v_y_now;
		//------
		if (mRow < 9) {
			Star mStar = mGameScreen.StarArray[mRow + 1][mColumn];
			if (mStar != null) {
				if (y <= (mStar.y + Constant.StarStep + fall_type_differ)) {
					y = mStar.y + Constant.StarStep;
				}
			}
		}
		if (y <= finalFall_y) {
			y = (int) finalFall_y;
			y_old = y;
			v_y = 0;
			move_state_y = Constant.StarState_Quiet;
			if (move_state_x == Constant.StarState_Quiet) {
				AnimaMove = false;
			}
		}
	}



	
	
	
	
	
	
	
	
	
	
}
