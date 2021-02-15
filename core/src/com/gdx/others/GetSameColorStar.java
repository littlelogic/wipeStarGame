package com.gdx.others;

import com.gdx.common.LogW;

import java.util.ArrayList;

public class GetSameColorStar {
	
	public Star[][] StarArray=new Star[10][10];
	public	int StarTotalNumTemp;
	
	int num_now = 0;
	int num_turn = 0;
	ArrayList<Star> StarListTemp=new ArrayList<Star>();

	public Object[] getSameColorStarObjectArray() {
		Object[] StarArrayTrue = {};
		num_now = 0;
		num_turn = 0;
		StarListTemp.clear();
		for (int i = 0; i < StarArray.length; i++) {
			for (int j = StarArray[i].length - 1; j >= 0; j--) {
				Star mStarTemp = StarArray[j][i];
				if (mStarTemp != null) {
					StarListTemp.clear();
					mStarTemp.select_hint = 1;
					mStarTemp.check_hint = 1;
					check_touch(mStarTemp, StarListTemp);
					num_turn = StarListTemp.size();

					if (num_turn >= 1) {
						StarArray[j][i] = null;
						StarListTemp.add(mStarTemp);
						num_turn++;
						StarTotalNumTemp -= num_turn;
						if (num_now < num_turn) {
							StarArrayTrue = StarListTemp.toArray();
							num_now = num_turn;
						}
						if (num_now >= StarTotalNumTemp) {
							return StarArrayTrue;
						}
					} else {
						//--移除孤点
						StarArray[j][i] = null;
					}
				}
			}
		}
		return StarArrayTrue;
	}
	
	public Object[] getSameColorStarObjectArray_20180912_front(){
		Object[] StarArrayTrue={};
		num_now = 0;
		num_turn = 0;
		StarListTemp.clear();

		for(int i=0;i<StarArray.length;i++){
			for(int j=StarArray[i].length-1;j>=0;j--){
				Star mStarTemp=StarArray[j][i];
				if(mStarTemp!=null){
					StarListTemp.clear();
					mStarTemp.select_hint=1;
					mStarTemp.check_hint=1;
					check_touch(mStarTemp,StarListTemp);
					num_turn=StarListTemp.size();
					
					if(num_turn>=1){
						StarArray[j][i]=null;
						StarListTemp.add(mStarTemp);
						StarTotalNumTemp-=num_turn;
						
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--00--"+mStarTemp.mRow+"-"+mStarTemp.mColumn+"-->");
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--00--StarListTemp.size()-->"+StarListTemp.size());
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--00--num_now-->"+num_now);
						
						if(num_now<num_turn){
							StarArrayTrue=StarListTemp.toArray();
							num_now=num_turn;
						}
						if(num_now>=StarTotalNumTemp){
							return StarArrayTrue;
						}
						
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--01--StarListTrue.size()-->"+StarArrayTrue.length);
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--01--num_now-->"+num_now);
					}else{
						//--移除孤点
						StarArray[j][i]=null;
					}
				}
			}
		}
		LogW.e("zddz_002", "--GetSameColorStar--GetSameColorStar_num-5-StarArrayTrue.length-->"+StarArrayTrue.length);
		return StarArrayTrue;
	}

	public Object[] getSameColorStarObjectArray__b(){
		Object[] StarArrayTrue={};
		
		ArrayList<Star> StarListTemp=new ArrayList<Star>();
		for(int i=0;i<StarArray.length;i++){
			for(int j=StarArray[i].length-1;j>=0;j--){
				//--num_now=StarListTrue.size();
				
				Star mStarTemp=StarArray[j][i];
				if(mStarTemp!=null){
					StarListTemp.clear();
					mStarTemp.select_hint=1;
					mStarTemp.check_hint=1;
					check_touch(mStarTemp,StarListTemp);
					num_turn=StarListTemp.size();
					
					if(StarListTemp.size()>=1){
						/*for(int ii=0;ii<StarListTemp.size();ii++){
							StarArray[StarListTemp.get(ii).mRow][StarListTemp.get(ii).mColumn]=null;
						}*/
						StarArray[j][i]=null;
						StarListTemp.add(mStarTemp);
						StarTotalNumTemp-=StarListTemp.size();
						
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--00--"+mStarTemp.mRow+"-"+mStarTemp.mColumn+"-->");
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--00--StarListTemp.size()-->"+StarListTemp.size());
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--00--num_now-->"+num_now);
						
						if(num_now<StarListTemp.size()){
							StarArrayTrue=StarListTemp.toArray();
							num_now=StarListTemp.size();
							//num_now=StarArrayTrue.length;
						}
						if(num_now>=StarTotalNumTemp){
							return StarArrayTrue;
						}
						
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--01--StarListTrue.size()-->"+StarArrayTrue.length);
						LogW.e("zddz_001", "--GetSameColorStar--GetSameColorStar_num--01--num_now-->"+num_now);
					}else{
						//移除孤点
						StarArray[j][i]=null;
					}
				}
			}
		}
		LogW.e("zddz_002", "--GetSameColorStar--GetSameColorStar_num-5-StarArrayTrue.length-->"+StarArrayTrue.length);
		return StarArrayTrue;
	}
	
	//===============================================================================

	int num_check_touch__=0;//记录方法执行的次数
	Star mStarTemp;
	
	public void check_touch(Star mStar,ArrayList<Star> StarSelectList_){
		num_check_touch__++;
		mStarTemp=getLeftStar(mStar);
		if(mStarTemp!=null&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				StarArray[mStar.mRow][mStar.mColumn-1]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
		
		mStarTemp=getTopStar(mStar);
		if(mStarTemp!=null&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				StarArray[mStar.mRow-1][mStar.mColumn]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
		
		mStarTemp=getRightStar(mStar);
		if(mStarTemp!=null&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				StarArray[mStar.mRow][mStar.mColumn+1]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
		
		mStarTemp=getBottomStar(mStar);
		if(mStarTemp!=null&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				StarArray[mStar.mRow+1][mStar.mColumn]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
	}
	
	public void check_touch__b(Star mStar,ArrayList<Star> StarSelectList_){
		//--LogW.i("zddz_001", "--GameScreen--check_touch--001--num_check_touch__-->>"+num_check_touch__);
		num_check_touch__++;
		//--num_partner=0;
		
		mStarTemp=getLeftStar(mStar);
		if(mStarTemp!=null&&mStarTemp.select_hint==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				//--num_partner++;
				StarArray[mStar.mRow][mStar.mColumn-1]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
		
		mStarTemp=getTopStar(mStar);
		if(mStarTemp!=null&&mStarTemp.select_hint==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				//--num_partner++;
				StarArray[mStar.mRow-1][mStar.mColumn]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
		
		mStarTemp=getRightStar(mStar);
		if(mStarTemp!=null&&mStarTemp.select_hint==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				//--num_partner++;
				StarArray[mStar.mRow][mStar.mColumn+1]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
		
		mStarTemp=getBottomStar(mStar);
		if(mStarTemp!=null&&mStarTemp.select_hint==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				//--num_partner++;
				StarArray[mStar.mRow+1][mStar.mColumn]=null;
				mStarTemp.select_hint=1;
				mStarTemp.check_hint=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,StarSelectList_);
			}else{
				mStarTemp.select_hint=0;
				mStarTemp.check_hint=1;
			}
		}
	}
	
	public Star getLeftStar(Star mStar){
		if(mStar.mColumn==0){
			return null;
		}else{
			return StarArray[mStar.mRow][mStar.mColumn-1];
		}
	}
	
	public Star getTopStar(Star mStar){
		if(mStar.mRow==0){
			return null;
		}else{
			return StarArray[mStar.mRow-1][mStar.mColumn];
		}
	}
	
	public Star getRightStar(Star mStar){
		if(mStar.mColumn==9){
			return null;
		}else{
			return StarArray[mStar.mRow][mStar.mColumn+1];
		}
	}
	
	public Star getBottomStar(Star mStar){
		if(mStar.mRow==9){
			return null;
		}else{
			return StarArray[mStar.mRow+1][mStar.mColumn];
		}
	}

}
