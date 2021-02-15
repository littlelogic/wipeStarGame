package com.gdx.others;

public class MovePack {
	
	public Move mMove;
	public int startColumn;
	public int endColumn;
	public int differ;
	
	/*public MovePack(Move mMove_,int  differ_){
		mMove=mMove_;
		differ=differ_;
	}*/
	
	public MovePack(Move mMove_, int startColumn_, int endColumn_, int  differ_){
		mMove=mMove_;
		startColumn=startColumn_;
		endColumn=endColumn_;
		differ=differ_;
	}
}
