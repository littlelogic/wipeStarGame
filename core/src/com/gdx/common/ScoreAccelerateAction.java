package com.gdx.common;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.gdx.screen.GameScreen;

public class ScoreAccelerateAction extends TemporalAction {

	
	
	/*public int score_now;
	public float gravity_score_now=1.0f;
	public float V0_score_now;
	public float t_score_now;
	public float Vt_score_now;*/
	
	GameScreen mGameScreen;
	
	public ScoreAccelerateAction (GameScreen mGameScreen_) {
		mGameScreen=mGameScreen_;
	}
	
	
	public int addTotal;
	public int addNow;
	public int addCount;
	public int addLeft;
	
	@Override
	public void update(float percent) {
		 addNow=(int)(percent*addTotal-addCount);
		 addCount+=addNow;
		 /*if(per>=1.0f){
			 
		 }*/
		 mGameScreen.deal_ScoreChange(addNow);
	}

	
	public void restart(int addTotal_) {
		addTotal=addTotal_;
		addNow=0;
		addCount=0;
	}
	
	
}
