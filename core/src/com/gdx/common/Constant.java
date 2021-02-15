package com.gdx.common;

import com.badlogic.gdx.graphics.Texture;

public class Constant {

    public  static String prefe_sigleClickDismiss= "prefe_sigleClickDismiss";
	public  static String prefe_name             = "StarPreferences_aa";
	public  static String prefe_muBiaoScore      = "muBiaoScore_aa";
	public  static String prefe_score_total      = "score_total_aa";
	
	public  static String prefe_star_array       = "star_array_aa";
	public  static String prefe_Star_Left_num   = "Star_Left_num_aa";//---剩余星星的数目
	public  static String prefe_guanKaNum_mark   = "guanKaNum_mark_aa";//---是否设置了管卡数目
	public  static String prefe_guanKaNum        = "guanKaNum_aa";
	public  static String prefe_JiangLiFen_mark  = "JiangLiFen_mark_aa";//---是否设置了奖励分数
	
	public  static String prefe_pause_do    = "pause_do_aa";
	//------------------------------------------
	
	public  static float perW;
	public  static float perH;

	public  static Texture TextureBlack;
	public  static Texture TextureBlue;
	public  static Texture TextureGreen;
	public  static Texture TextureRed;
	public  static Texture TextureWhite;
	public  static Texture TextureYellow;

	//----------------
	public  static final int StarEmitterNumGame = 15;
	public  static final int StarEmitterNumEnd  = 15;

	//ipone5--------------
	public static final int GameWidth  = 640;
	public static final int GameHeight = 1136;
	//--public static final float GameWidth  = 640;
	//--public static final float GameHeight = 1136;

	public static final int value_blue    =  1;
	public static final int value_green   =  2;
	public static final int value_purple  =  3;
	public static final int value_red     =  4;
	public static final int value_yellow  =  5;

	public static final int StarState_Quiet =  0;
	public static final int StarState_Ready =  1;
	public static final int StarState_Move  =  2;
	public static final int StarState_Pause =  3;
	
	public static final int StarArray_y     = 20;
	public static final int StarArray_y_top = 20+64*10;
	public static final int StarStep        = 64;
	
	public static final int WaitUpdate_State_Ready     = 0;
	public static final int WaitUpdate_State_Dismiss   = 1;
	public static final int WaitUpdate_State_Y_Fall    = 2;
	public static final int WaitUpdate_State_X_Move    = 3;
	public static final int WaitUpdate_State_X_Finish  = 4;

	public  static final float Glitter_BeforeTime = 0.1f;
	public  static final float Glitter_Time       = 0.05f;
	public  static final float Glitter_TotalTime  = 1f;
	
	public static final int GameOverGlitter_wait      = 1;
	public static final int GameOverGlitter_glitter   = 2;
	public static final int GameOverGlitter_disappear = 3;
	public static final int GameOverGlitter_over      = 4;
	
	public static final int GameOverGlitter_max_num   = 30;
	

}
