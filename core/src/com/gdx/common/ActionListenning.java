package com.gdx.common;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class ActionListenning extends TemporalAction {

	ToUpdate mToUpdate;

	public interface ToUpdate{
		public void done();
	}
	
	public ActionListenning(ToUpdate mToUpdate_) {
		mToUpdate=mToUpdate_;
	}
	

	@Override
	public boolean act(float delta) {
		if(mToUpdate!=null){
			mToUpdate.done();
			mToUpdate=null;
			finish();
		}
		return false;
	}
	
//	@Override
//	public void update(float percent) {
//		if(mToUpdate!=null){
//			mToUpdate.done();
//		}
//	}

	
	
	public int addTotal;
	public int addNow;
	public int addCount;
	public int addLeft;
	
	public void restart(int addTotal_) {
		addTotal=addTotal_;
		addNow=0;
		addCount=0;
	}


	@Override
	protected void update(float percent) {
		// TODO Auto-generated method stub
		
	}
	
}
