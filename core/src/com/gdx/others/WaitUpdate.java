package com.gdx.others;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.common.Constant;
import com.gdx.common.LogW;
import com.gdx.screen.GameScreen;
import com.gdx.toupdate.ToUpdate;

import java.util.ArrayList;
import java.util.Random;

public class WaitUpdate {
	
	GameScreen myGameScreen;
	
	//本轮所选中的星星列表
	public ArrayList<Star> StarSelectList=new ArrayList<Star>();
	
	//列--最下面的选择状态的星星，记录有移动动画的列
	public Star[] SelectColumnBottomArray______=new Star[10];
	
	//所有需要下落的星星
	ArrayList<Star> StarFallList_Now=new ArrayList<Star>();
	ArrayList<StarPack> StarFallList_Now_already=new ArrayList<StarPack>();
	
	//列--需要移动的列
	public ArrayList<Move> MoveList_x=new ArrayList<Move>();
	ArrayList<MovePack> MoveList_x_already=new ArrayList<MovePack>();
	
	public int state_mark=0;
	
	float startTime;
	float differ=0.1f;
	
	//---------------
	ToUpdate mToUpdate_Dismiss;
	ToUpdate mToUpdate_Y_Fall;
	ToUpdate mToUpdate_X_Move;
	//---
	ToUpdate mToUpdate_now;
	
	public WaitUpdate(Star[] SelectColumnBottomArray_,
			ArrayList<Star> StarFallList_Now_,ArrayList<StarPack> StarFallList_Now_already_,
			ArrayList<Move> MoveList_x_,ArrayList<MovePack> MoveList_x_already_,
			ArrayList<Star> StarSelectLis_,
			GameScreen myGameScreen_){
		SelectColumnBottomArray______=SelectColumnBottomArray_;
		
		StarFallList_Now=StarFallList_Now_;
		StarFallList_Now_already=StarFallList_Now_already_;
		MoveList_x=MoveList_x_;
		MoveList_x_already=MoveList_x_already_;
		
		StarSelectList=StarSelectLis_;
		myGameScreen=myGameScreen_;
		
		differ=myGameScreen_.differ;
		startTime=differ+1;;
		state_mark=Constant.WaitUpdate_State_Dismiss;
		initToUpdate();
		setToUpdate(mToUpdate_Dismiss);
		StarDismissNumIndex=0;
	}
	
	//----------------------------------
	//----------------------------------
	
	public void initToUpdate(){
		mToUpdate_Dismiss=new ToUpdate(){
			@Override
			public void doUpdate(float deltaTime) {
				deal_ToUpdate_Dismiss(deltaTime);
			}};
		
		//--------------------------	

		mToUpdate_Y_Fall=new ToUpdate(){
			@Override
			public void doUpdate(float deltaTime) {
				deal_ToUpdate_Y_Fall(deltaTime);
			}};
			
		//--------------------------	
		
		mToUpdate_X_Move=new ToUpdate(){
			@Override
			public void doUpdate(float deltaTime) {
				deal_ToUpdate_X_Move(deltaTime);
			}};
	}
	
	int StarDismissNumIndex=0;
	
	public void deal_ToUpdate_Dismiss(float deltaTime){
		if(StarSelectList.size()>0){
		
			startTime+=deltaTime;
			if(startTime>=differ){
				Random random = new Random(System.currentTimeMillis());//---待
				int index=random.nextInt(StarSelectList.size());
				startTime=0;
				StarDismissNumIndex++;
				Star mStar=StarSelectList.get(index);
				StarSelectList.remove(mStar);
				myGameScreen.deal_StarParticleManage(mStar.mColor,mStar.x+Constant.StarStep/2,mStar.y+Constant.StarStep/2,Constant.StarEmitterNumGame);
		
				myGameScreen.setSingleScoreAnima((StarDismissNumIndex*2-1)*5,mStar.x+Constant.StarStep/2,mStar.y+Constant.StarStep/2);
			}
		}else{
			for(int i=0;i<StarFallList_Now.size();i++){
				Star mStarTemp=StarFallList_Now.get(i);
				/*if(mStarTemp==null){
					continue;//理论上不用
				}*/
				mStarTemp.move_state_y=Constant.StarState_Move;
			}
			
			for(int i=0;i<StarFallList_Now_already.size();i++){
				StarPack mStarPack=StarFallList_Now_already.get(i);
				if(mStarPack.mStar.move_state_y==Constant.StarState_Quiet){
					mStarPack.mStar.setFallReady_differ(mStarPack.differ);
					mStarPack.mStar.move_state_y=Constant.StarState_Move;
					StarFallList_Now.add(mStarPack.mStar);
				}else{
					mStarPack.mStar.setFinalFall_y_differ(mStarPack.differ);
				}
			}
			
			startTime=0;
			state_mark=Constant.WaitUpdate_State_Y_Fall;
			setToUpdate(mToUpdate_Y_Fall);
		}
	}
	
	public void deal_ToUpdate_Y_Fall(float deltaTime){
		LogW.e("zddz_10", "--WaitUpdate--deal_ToUpdate_Y_Fall--001-->>");
		int num=0;
		for(int i=0;i<StarFallList_Now.size();i++){
			Star mStarTemp=StarFallList_Now.get(i);
			/*if(mStarTemp==null){
				continue;//理论上不用
			}*/
			if(mStarTemp.move_state_y==Constant.StarState_Move){
				mStarTemp.update_Y(deltaTime);
				num++;
			}
		}
		if(num==0){
			startTime=0;
			state_mark=Constant.WaitUpdate_State_X_Move;
			for(int i=0;i<MoveList_x.size();i++){
				MoveList_x.get(i).setMove();
			}
			setToUpdate(mToUpdate_X_Move);
			
			for(int i=0;i<MoveList_x_already.size();i++){
				MovePack mMovePack=MoveList_x_already.get(i);
				if(mMovePack.mMove.getMoveMark()==Constant.StarState_Quiet){
					mMovePack.mMove.set_Data(mMovePack.startColumn,mMovePack.endColumn,mMovePack.differ);
					mMovePack.mMove.setMove();
					MoveList_x.add(mMovePack.mMove);
				}else if(mMovePack.mMove.getMoveMark()==Constant.StarState_Pause){
					mMovePack.mMove.set_Data(mMovePack.startColumn,mMovePack.endColumn,mMovePack.differ);
					mMovePack.mMove.setMove();
				}else if(mMovePack.mMove.getMoveMark()==Constant.StarState_Ready){
					mMovePack.mMove.set_Data(mMovePack.startColumn,mMovePack.endColumn,mMovePack.differ);
				}else{//移动中
					mMovePack.mMove.set_Data(mMovePack.startColumn,mMovePack.endColumn,mMovePack.differ);
					mMovePack.mMove.setMove();
				}
			}
		}
	}
	
	public void deal_ToUpdate_X_Move(float deltaTime){
		if(MoveList_x.size()<=0){
			state_mark=Constant.WaitUpdate_State_X_Finish;
		}else{
			for(int i=0;i<MoveList_x.size();){
				Move mMove=MoveList_x.get(i);
				if(mMove.getMoveMark()==Constant.StarState_Move){
					mMove.update(deltaTime);
					for(int j=9;j>=0;j--){
						Star mStar=myGameScreen.StarArray[j][mMove.mColumn];
						if(mStar!=null){
							mStar.x=mMove.x_now;
						}else{
							break;
						}
					}
					i++;
				}else if(mMove.getMoveMark()==Constant.StarState_Pause){
					i++;
				}else{
					mMove.update(deltaTime);
					boolean remove_mark=false;
					if((mMove.mColumn*Constant.StarStep)<(mMove.x_now-1)){//还有没出发的X移动
						mMove.setPause();
						remove_mark=false;
						i++;
					}else{
						remove_mark=true;
					}
					for(int j=9;j>=0;j--){
						Star mStar=myGameScreen.StarArray[j][mMove.mColumn];
						if(mStar!=null){
							mStar.x=mMove.x_now;
							if(remove_mark){
								mStar.move_state_x=Constant.StarState_Quiet;
								mStar.AnimaMove=false;
							}else{
								mStar.move_state_x=Constant.StarState_Ready;
								i++;
							}
						}else{
							break;
						}
					}
					if(remove_mark){
						MoveList_x.remove(i);
					}
				}
			}
		}
	}
	
	//===============================================================
	
	public void update (float deltaTime) {
		mToUpdate_now.doUpdate(deltaTime);
	}
	
	public void draw(SpriteBatch batcher){
		int num=StarSelectList.size();
		for(int i=0;i<num;i++){
			Star mStar=StarSelectList.get(i);
			batcher.draw(mStar.mRegion, mStar.x,mStar.y, 64, 64);
			batcher.draw(mStar.mSelectRegion, mStar.x,mStar.y, 64, 64);
		}
	}
			
	//===============================================================
	
//	interface ToUpdate{
//		public void doUpdate (float deltaTime);
//	}
	
	public void setToUpdate (ToUpdate ToUpdate_) {
		mToUpdate_now=ToUpdate_;
	}
	
}
