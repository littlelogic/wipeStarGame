package com.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gdx.common.ActionListenning;
import com.gdx.common.Constant;
import com.gdx.common.LogW;
import com.gdx.common.ScoreAccelerateAction;
import com.gdx.common.TimerActionListenning;
import com.gdx.common.Tool;
import com.gdx.others.GetSameColorStar;
import com.gdx.others.Move;
import com.gdx.others.MovePack;
import com.gdx.others.Star;
import com.gdx.others.StarPack;
import com.gdx.others.StarParticleManage;
import com.gdx.others.WaitUpdate;
import com.gdx.toupdate.ToUpdate;

import java.util.ArrayList;
import java.util.Random;


/**
 * 注意隐藏的死循环
 * @author wjw
 */
public class GameScreen extends InputAdapter implements Screen {
	
	/**
	 * 一共设置100关
	 * 每一关对应的目标分数: ???
	 * 一个区域的得分的计算
	 * TOTAL=5*N*N----最大值：50000（num--最大-100个星星）
	 * 一个区域单个星星的得分计算
	 * single=(2*n-1)*5 最大为2000-5=1995（num--最大-100个星星）
	 * 一局游戏结束对应的奖励分数：
	 * 2000(剩0个星星),1980(1个星星),1920(2个星星),1820(3个星星),1680(4个星星),1500(5个星星),
	 * 1280(6个星星),1020(7个星星),720(8个星星),380(9个星星),0(>=10个星星)
	 * 1-星星进入下落的动画
	 * 2-正常的游戏状态
	 * 3-游戏结束
	 * 4-设置奖励分数
	 * 5-设置下一关的管卡数目
	 * 6-
	 * 7-
	 */
	//==========================================================
	/** 游结束的奖励分数的暂时记录 */
	boolean singleTouchRemoveMark = false;
	boolean test_mark = false;
	int now_JiangLiFen;
	int index_JiangLiFen;
	int JiangLiFen_value[]={
			2000,1980,1920,1820,1680,1500,1280,1020,720,380,0
	};
	
	/** 现在的管卡数 */
	int now_guanKaNum=1;
	/** 现在的管卡数-对应的目标分数 */
	int now_MuBiaoScore=100;
	/** 实时的得分数 */
	int score_total=0;
	/** 实时的得分数--的中间变量，主要用于分数数字的动态增加 */
	int score_now=0;
	
	/** 是否设置了管卡数目 */
	boolean guanKaNum_mark=false;
	
	/** 是否设置了奖励分数 */
	boolean JiangLiFen_mark=false;
	String prefe_star_array;
	
	//==========================================================
	//---public final float star_v_y_initial=0f;
	public final float star_v_y_initial=500f;
	
	//------------------
	public final float differ=0.1f;
	///--public final float differ=0.00001f;
	//--float differ=0.5f;
	//--public float differ=1.6f;
	//--public float differ=3f;

	public final float GameOverGlitter_disappear_differ=0.2f;
	//------------------
	public final static float gravity_y=-5000;
	public final static float gravity_x=-5000;
	//--final float gravity_y=-3000;
	//--final float gravity_x=-3000;
	//--public final float gravity_y=-300;
	//--public final float gravity_x=-300;
	//==========================================================
	
	int state;
	Vector3 touchPoint;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	BitmapFont font;
	public ShapeRenderer renderer;
	
	Stage stage;
	//----------------------------------------------
	Group GuanKaLeftNum_Group;
	public TextureRegion GuanKaNum_left_Region;
	public Image GuanKaNum_leftText_Image;
	public Image[] GuanKaNum_left_num=new Image[3];//now--max--100
	float GuanKaNum_Group_num_width=37*Constant.perW;
	float GuanKaNum_Group_num_height=41*Constant.perH;
	//----------------------------------------------
	Group GuanKaCentreNum_Group;
	public TextureRegion GuanKaCentre_Region;
	public Image GuanKaCentre_Image;
	public Image[] GuanKaCentre_num=null;//--=new Image[3];//now--max--100
	float  GuanKaCentreNum_Group_num_width=57*Constant.perW;
	float  GuanKaCentreNum_Group_num_height=67*Constant.perH;
	//----------------------------------------------
	Group MuBiaoFen_centre_Group;
	public TextureRegion MuBiaoFen_centre_Region;
	public Image MuBiaoFen_centre_Image;
	public Image[] MuBiaoFen_centre_num=null;//--=new Image[8;//now--max--100
	float  MuBiaoFen_centre_num_width=36*Constant.perW;
	float  MuBiaoFen_centre_num_height=40*Constant.perH;
	//----------------------------------------------
	Group StarScoreHint_Group;
	public TextureRegion[] Num_Region_aa;
	public Image[] Num_Image_aa=new Image[8] ;
	public TextureRegion Region_LianXiao;
	public Image TextImage_LianXiao;
	public TextureRegion Region_Fen;
	public Image TextImage_Fen;
	
	//----------------------------------------------
	Group StarScoreNow_Group;
	public TextureRegion[] Num_Region_bb=new TextureRegion[10];
	/**
	 * 100局最大的分：10000000-8位数，美每局最还的得分50000，
	 * 最大奖励50000（现在2000），
	 */
	public Image[] Image_ScoreNow=new Image[8];
	//----------------------------------------------
	Group StarLeftNum_Group;
	public TextureRegion StarLeftNum_Region;
	public Image StarLeftNum_left;
	//--public Image[] StarLeftNum_num=new Image[3] ;
	public Image[] StarLeftNum_num=null;
	float StarLeftNum_num_width=40*Constant.perW;
	//--float StarLeftNum_num_width=46*Constant.perW;
	float StarLeftNum_num_height=44*Constant.perH;
	public Image StarLeftNum_right;
	//----------------------------------------------
	Group JiangLiFen_Group;
	public TextureRegion JiangLiFen_left_Region;
	public Image JiangLiFen_left;
	public Image[] JiangLiFen_num=null;
	float JiangLiFen_num_width=50*Constant.perW;
	//--float JiangLiFen_num_width=68*Constant.perW;
	float JiangLiFen_num_height=57*Constant.perH;
	
	//==========================================================
	public TextureRegion backgroundRegion;
	public TextureRegion StarRegion_blue;
	public TextureRegion StarRegion_green;
	public TextureRegion StarRegion_purple;
	public TextureRegion StarRegion_red;
	public TextureRegion StarRegion_yellow;
	public TextureRegion StarRegion_select;
	public TextureRegion StarParticleRegion;
	
	//==========================================================

	public GameScreen (StarGame game) {
		initPreferencesData();
		initScreenSet();
		initTexture();
		//------------------------------------------------------------------------------------
		setStage();
		setNeedActor();
		initToUpdate();
		setGuanKaNum_Left(now_guanKaNum);
		setStar_Init_Fall(false);
	}
	
	Preferences prefs;

	public void  initPreferencesData(){
		/*
        保存位置
		在Windows，Linux，OS X，preference都是以xml文件存储在用户的home目录下。
		| Windows |
		`%UserProfile%/.prefs/My Preferences`
		| Linux and OS X |
		`~/.prefs/My Preferences`
		文件名就是Gdx.app.getPreferences()的形参，你可以手动更改它，对调试很有用。
		*/
		//---------------------------------------------------------
		
		/*try {
			Class.forName("org.sqlite.JDBC");// 加载JDBC类
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
		//---------------------------------------------------------
		prefs= Gdx.app.getPreferences(Constant.prefe_name);
		now_guanKaNum=prefs.getInteger(Constant.prefe_guanKaNum,-1);
		now_MuBiaoScore=prefs.getInteger(Constant.prefe_muBiaoScore,-1);
		score_total=prefs.getInteger(Constant.prefe_score_total,-1);
		prefe_star_array=prefs.getString(Constant.prefe_star_array,"");
		singleTouchRemoveMark=prefs.getBoolean(Constant.prefe_sigleClickDismiss,true);
		LogW.singleTouchRemoveMark(singleTouchRemoveMark);

		LogW.e("zddz_001", "--GameScreen--initPreferencesData--now_guanKaNum-->>"+now_guanKaNum);
		LogW.e("zddz_001", "--GameScreen--initPreferencesData--now_MuBiaoScore-->>"+now_MuBiaoScore);
		LogW.e("zddz_001", "--GameScreen--initPreferencesData--score_total-->>"+score_total);
		LogW.e("zddz_001", "--GameScreen--initPreferencesData--prefe_star_array-->>"+prefe_star_array);
		
		if(now_guanKaNum==-1||now_MuBiaoScore==-1||score_total==-1){
			now_guanKaNum=1;
			now_MuBiaoScore=100;
			score_total=0;
		}
	}
	
	public void  initScreenSet(){
		Gdx.input.setInputProcessor(this);
		touchPoint = new Vector3();
		guiCam = new OrthographicCamera(Constant.GameWidth, Constant.GameHeight);
		guiCam.position.set(Constant.GameWidth / 2, Constant.GameHeight / 2, 0);
		//guiCam.position.set(0, 0, 0);
		batcher = new SpriteBatch();
		
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(guiCam.combined);
		
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		
		fps_min=100;
	}
	
	public void  initTexture(){
		//------------
		//--wrong---backgroundRegion = new TextureRegion(loadTexture("data/bj01_01.jpg"));
		font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
		//--font = new BitmapFont();
		
		backgroundRegion = new TextureRegion(Tool.loadTexture("data/background.png"));
		StarRegion_blue = new TextureRegion(Tool.loadTexture("data/block_blue.png"));
		
		StarRegion_green = new TextureRegion(Tool.loadTexture("data/block_green.png"));
		StarRegion_purple = new TextureRegion(Tool.loadTexture("data/block_purple.png"));
		StarRegion_red = new TextureRegion(Tool.loadTexture("data/block_red.png"));
		StarRegion_yellow = new TextureRegion(Tool.loadTexture("data/block_yellow.png"));
		StarRegion_select = new TextureRegion(Tool.loadTexture("data/block_select.png"));
		StarParticleRegion= new TextureRegion(Tool.loadTexture("data/particle.png"));
		
		StarLeftNum_Region= new TextureRegion(Tool.loadTexture("data/text_left_star_num.png"));
		JiangLiFen_left_Region= new TextureRegion(Tool.loadTexture("data/text_jiangli.png"));
		GuanKaNum_left_Region= new TextureRegion(Tool.loadTexture("data/guanka_left_num.png"));
		GuanKaCentre_Region= new TextureRegion(Tool.loadTexture("data/guanka_centre_num.png"));
		MuBiaoFen_centre_Region= new TextureRegion(Tool.loadTexture("data/mubiaofen_centre_num.png"));
	}
	
	public void setStage(){
		OrthographicCamera guiCama_stage= new OrthographicCamera(Constant.GameWidth, Constant.GameHeight);
		SpriteBatch batcher_stage = new SpriteBatch();
		/*OrthographicCamera guiCama_stage=guiCam;
		SpriteBatch batcher_stage = batcher;*/
		
		guiCama_stage.position.set(Constant.GameWidth / 2, Constant.GameHeight / 2, 0);
		Viewport mScreenViewport=new ScreenViewport(guiCama_stage);
		stage = new Stage(mScreenViewport,batcher_stage);
		//mScreenViewport.apply(true);
		//mScreenViewport.update(Constant.GameWidth, Constant.GameHeight, true);
//		mScreenViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		//--Gdx.input.setInputProcessor(stage);
		/*mScreenViewport.setScreenSize(Constant.GameWidth, Constant.GameHeight);
		mScreenViewport.setScreenPosition(Constant.GameWidth / 2, Constant.GameHeight / 2);
		mScreenViewport.update(Constant.GameWidth, Constant.GameHeight, true);*/
		//--stage.setDebugAll(true);
		//--stage.setDebugInvisible(true);
		stage.setDebugAll(false);
		stage.setDebugInvisible(false);
		
		Image sigleDismiss = new Image(Constant.TextureWhite);
		sigleDismiss.setSize(64*Constant.perW*2, 64*Constant.perH);
		sigleDismiss.setOrigin(0, 0);
		sigleDismiss.setPosition(Constant.GameWidth*Constant.perW -sigleDismiss.getWidth(),
				Constant.GameHeight*Constant.perH - sigleDismiss.getHeight());
		sigleDismiss.setColor(sigleDismiss.getColor().r,sigleDismiss.getColor().g,sigleDismiss.getColor().b,0.5f);
		stage.addActor(sigleDismiss);
		sigleDismiss.addCaptureListener(new EventListener(){
			@Override
			public boolean handle(Event event) {
				LogW.e("zddz_002","GameScreen-sigleDismiss-EventListener->");

				return true;
			}
		});

		LogW.e("zddz_002","--GameScreen--stage.getHeight()-->>"+stage.getHeight());
		LogW.e("zddz_002","--GameScreen--stage.getWidth()-->>"+stage.getWidth());
		LogW.e("zddz_002","--GameScreen--Constant.perW-->>"+Constant.perW);
		LogW.e("zddz_002","--GameScreen--Constant.perH-->>"+Constant.perH);
	}

	private void dealSigleDismiss(){
		singleTouchRemoveMark = !singleTouchRemoveMark;
		prefs.putBoolean(Constant.prefe_sigleClickDismiss,singleTouchRemoveMark);
		LogW.singleTouchRemoveMark(singleTouchRemoveMark);
	}
	
	public void  setNeedActor(){
		//---------------------------------------------------
		StarScoreHint_Group=new Group();
		//NumGroup.setSize(width, height);
		Num_Region_aa=new TextureRegion[10];
		Texture Num_Region_aa_all= Tool.loadTexture("data/num_text_blue.png");
		for(int i=0;i<10;i++){
			Num_Region_aa[i]= new TextureRegion(Num_Region_aa_all,30*i,0,30,36);
		}
		
		float width_aa=30*43f/36*Constant.perW;
		float height_aa=43f*Constant.perH;
		for(int i=0;i<Num_Image_aa.length;i++){
			//Num_Image_aa[i]=new Image(new TextureRegion(Num_Region_aa[i]));
			Num_Image_aa[i]=new Image();
			Num_Image_aa[i].setSize(width_aa,height_aa);
		}
		
		Texture LianXiao_Region_aa_all= Tool.loadTexture("data/text_lianxiao.png");
		Region_LianXiao= new TextureRegion(LianXiao_Region_aa_all,0,0,82,43);
		TextImage_LianXiao= new Image(Region_LianXiao);
		TextImage_LianXiao.setSize(82*Constant.perW, 43f*Constant.perH);
		
		Region_Fen= new TextureRegion(LianXiao_Region_aa_all,82,0,42,43);
		TextImage_Fen= new Image(Region_Fen);
		TextImage_Fen.setSize(42*Constant.perW, 43f*Constant.perH);
		//---------------------------------------------------
		Texture Num_Region_bb_all= Tool.loadTexture("data/num_text_yellow.png");
		for(int i=0;i<10;i++){
			Num_Region_bb[i]= new TextureRegion(Num_Region_bb_all,75*i,0,75,71);
		}
		width_aa=75f*Constant.perW;
		height_aa=71f*Constant.perH;
		for(int i=0;i<Image_ScoreNow.length;i++){
			Image_ScoreNow[i]=new Image();
			Image_ScoreNow[i].setSize(width_aa,height_aa);
		}
		StarScoreNow_Group=new Group();
		//-------------------------------------------
		stage.addActor(StarScoreNow_Group);
		setNowGetScore(score_total);
	}
	
	//========================================================================
	
	ToUpdate mToUpdate_now;
	ToUpdate mToUpdate_StarIntoAnima;
	ToUpdate mToUpdate_StarIntoAnima_b;
	ToUpdate mToUpdate_Gameing;
	ToUpdate mToUpdate_GameSpring;
	ToUpdate mToUpdate_GameHint;
	ToUpdate mToUpdate_GameEnd;
	
	public void initToUpdate(){
		mToUpdate_StarIntoAnima=new ToUpdate(){
			@Override
			public void doUpdate(float deltaTime) {
				deal_ToUpdate_StarIntoAnima(deltaTime);
			}};
		
		mToUpdate_StarIntoAnima_b=new ToUpdate(){
			@Override
			public void doUpdate(float deltaTime) {
				deal_ToUpdate_StarIntoAnima_b(deltaTime);
			}};
		
		//--------------------------	
		mToUpdate_Gameing=new ToUpdate(){
			@Override
			public void doUpdate(float deltaTime) {
				deal_ToUpdate_Gameing(deltaTime);
			}};
		
		//--------------------------	
		mToUpdate_GameEnd=new ToUpdate(){
		@Override
		public void doUpdate(float deltaTime) {
			deal_ToUpdate_GameEnd(deltaTime);
		}};
	}
	
	public TextureRegion getStarRegionBycolor(int colorValue) {
		switch (colorValue) {
		case Constant.value_blue:
			return StarRegion_blue;
		case Constant.value_green:
			return StarRegion_green;
		case Constant.value_purple:
			return StarRegion_purple;
		case Constant.value_red:
			return StarRegion_red;
		case Constant.value_yellow:
			return StarRegion_yellow;
		default:
			return StarRegion_yellow;//-------待
			//return null;
		}
	}
	
	public Color getColorBycolor(int colorValue) {
		switch (colorValue) {
		case Constant.value_blue:
			return Color.BLUE;
		case Constant.value_green:
			return Color.GREEN;
		case Constant.value_purple:
			return Color.PURPLE;
		case Constant.value_red:
			return Color.RED;
		case Constant.value_yellow:
			return Color.YELLOW;
		default:
			return Color.YELLOW;//-------待
			//return null;
		}
	}
	
	public String getStringBycolor(int colorValue) {
		switch (colorValue) {
		case Constant.value_blue:
			return "蓝";
		case Constant.value_green:
			return "绿";
		case Constant.value_purple:
			return "紫";
		case Constant.value_red:
			return "红";
		case Constant.value_yellow:
			return "黄";
		default:
			return "  ";//-------待
			//return null;
		}
	}
	
	//==========================================================
	
	public void setGuanKaNum_Left(int num){
		if(GuanKaLeftNum_Group==null){
			GuanKaLeftNum_Group=new Group();
			float width=3*GuanKaNum_Group_num_width+83*Constant.perW;
			GuanKaLeftNum_Group.setSize(width, 41*Constant.perH);
			GuanKaLeftNum_Group.setOrigin(0,0);
			GuanKaLeftNum_Group.setOrigin(width/2,0);
			GuanKaLeftNum_Group.setOrigin(width/2,41*Constant.perH/2);
			GuanKaLeftNum_Group.setPosition(10*Constant.perW,1066*Constant.perH);
			//--GuanKaNum_Group.setPosition(320*Constant.perW,1066*Constant.perH);
			//--GuanKaLeftNum_Group.setDebug(true);
			stage.addActor(GuanKaLeftNum_Group);
		}
		GuanKaLeftNum_Group.clearChildren();
		
		if(GuanKaNum_leftText_Image==null){
			GuanKaNum_leftText_Image= new Image(GuanKaNum_left_Region);
			GuanKaNum_leftText_Image.setSize(83*Constant.perW, 41f*Constant.perH);
			GuanKaNum_leftText_Image.setPosition(0, 0);
			GuanKaNum_leftText_Image.setColor(Color.YELLOW);
		}
		GuanKaLeftNum_Group.addActor(GuanKaNum_leftText_Image);
		
		String num_str=num+"";
		int num_temp;
		for(int i=0;i<num_str.length();i++){
			GuanKaNum_left_num[i]=new Image();
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			GuanKaNum_left_num[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			GuanKaNum_left_num[i].setSize(GuanKaNum_Group_num_width,GuanKaNum_Group_num_height);
			GuanKaNum_left_num[i].setColor(new Color(1, 1, 1, 1));
			GuanKaNum_left_num[i].setPosition(GuanKaNum_Group_num_width*i+83*Constant.perW, 0);
			GuanKaLeftNum_Group.addActor(GuanKaNum_left_num[i]);
		}
		
//		SequenceAction mSequenceAction =Actions.sequence(
//				Actions.parallel(Actions.rotateTo(360*100, 100)));
		RepeatAction mSequenceAction = Actions.repeat(12,
				Actions.moveTo(640*Constant.perW, 1066*Constant.perH,1)
				);
		mSequenceAction.restart();
		//--GuanKaNum_Group.addAction(mSequenceAction);
	}
	
	//==========================================================
	
	/**
	 * 
	 * @param into_mark--初始化--设置false，正常循环进入下一局设置true
	 */
	public void setStar_Init_Fall(boolean into_mark){
		draw_StarArray_mark=true;
		StarLeftList.clear();
		StarList_Spring.clear();
		StarList_hint.clear();
		StarSelectListTemp.clear();
		GuDianByAll=false;
		WaitUpdateList.clear();
		
		guanKaNum_mark=false;
		JiangLiFen_mark=false;
		StarLeft_over=0;
		
		boolean mark=prefs.getBoolean(Constant.prefe_pause_do, false);
		prefs.putBoolean(Constant.prefe_pause_do,false).flush();
		
		LogW.i("zddz_001", "--GameScreen--setStar_Init_Fall--mark-->>"+mark);
		LogW.i("zddz_001", "--GameScreen--setStar_Init_Fall--prefe_star_array-->>"+prefe_star_array);
		if(mark&&!into_mark){
			if(prefe_star_array!=null&&prefe_star_array.length()==100){
				LogW.i("zddz_001", "--GameScreen--setStar_Init_Fall--03-->>");
				initStarArrayData_prefs();
			}else{
				LogW.i("zddz_001", "--GameScreen--setStar_Init_Fall--02-->>");
				initStarArrayData();
				setStarIntoAnima();
			}
		}else{
			initStarArrayData();
			setStarIntoAnima();
		}
	}
	
	//==================================================================
	
	public void initStarArrayData_prefs(){
		ColumnNum=10;
		StarTotalNum=100;
		
		int NumColor;
		for(int i=0;i<10;i++){//行
			StarNum[i]=10;
			ColumnMove[i]=new Move(i,10,i*Constant.StarStep,i*Constant.StarStep,this);
			for(int j=0;j<10;j++){//列
				NumColor=Integer.parseInt(prefe_star_array.charAt(i*10+j)+"");
				if(NumColor==0){
					StarArray[i][j]=null;
				}else{
					StarArray[i][j]=new Star(i,j,NumColor,getColorBycolor(NumColor),
							getStarRegionBycolor(NumColor),StarRegion_select,this);
					StarArray[i][j].x=j*Constant.StarStep;
					StarArray[i][j].y=StarArray[i][j].y=Constant.StarArray_y+(9-i)*Constant.StarStep;
					StarArray[i][j].finalFall_y=StarArray[i][j].y;
				}
			}
		}
		
		if(isGuDianByAll(StarArray)){
			LogW.i("zddz_001", "--GameScreen--initStarArrayData_prefs--to--setStarIntoAnima-->>");
			if(!prefs.getBoolean(Constant.prefe_guanKaNum_mark,false)){
				setGuanKaNum_Left(++now_guanKaNum);
			}
			if(!prefs.getBoolean(Constant.prefe_JiangLiFen_mark,false)){
				int left_star_num=prefs.getInteger(Constant.prefe_Star_Left_num,11);
				if(left_star_num<10&&left_star_num>=0){
					setNowGetScore(score_total+JiangLiFen_value[left_star_num]);
				}
			}
			
			initStarArrayData();
			setStarIntoAnima();
			return;
		}
		
		for(int i=0;i<10;i++){//列数量,索引从下往上
			System.out.print("--");
			for(int j=0;j<10;j++){//行数量,索引从左往右
				Star mStar=StarArray[i][j];
				if(mStar!=null){
					System.out.print(getStringBycolor(mStar.color));
				}
			}
			System.out.print("\n");
		}
		System.err.println("---------->>");
		LogW.i("zddz_001", "--GameScreen--initStarArrayData_prefs()--StarArray.length-->>"+StarArray.length);
		LogW.i("zddz_001", "--GameScreen--initStarArrayData_prefs()--StarArray[0].length-->>"+StarArray[0].length);
		setToUpdate(mToUpdate_Gameing);
	}
	
	//==================================================================
	
	public Star[][] StarArray=new Star[10][10];//行,列
	int[][]  StarArrayGiven = { 
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 3, 3, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 2, 4, 3, 1, 1, 1, 1, 1, 1, 1 }, 
			{ 1, 3, 4, 3, 4, 3, 4, 3, 4, 3 },
			{ 3, 2, 5, 2, 1, 4, 1, 2, 1, 2 },
			{ 4, 3, 4, 1, 2, 1, 2, 1, 2, 1 },
			{ 1, 5, 2, 3, 4, 5, 1, 2, 1, 2 }, 
	};
	/*int[][]  StarArrayGiven = { 
			{ 2, 1, 1, 1, 1, 1, 1, 1, 3, 1 },
			{ 2, 1, 1, 2, 2, 2, 1, 1, 3, 1 }, 
			{ 2, 1, 1, 1, 1, 1, 1, 1, 3, 1 },
			{ 2, 1, 2, 2, 2, 2, 2, 2, 3, 1 },
			{ 2, 1, 1, 1, 1, 1, 1, 1, 3, 1 },
			{ 2, 1, 1, 2, 2, 1, 1, 1, 3, 1 }, 
			{ 1, 1, 1, 1, 1, 1, 1, 1, 3, 1 },
			{ 1, 1, 2, 2, 2, 2, 1, 1, 3, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 3, 1 },
			{ 1, 1, 2, 2, 1, 1, 1, 1, 3, 2 }, 
	};*/
	
	/*int[][]  StarArrayGiven = { 
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 }, 
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 }, 
			{ 1, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 2, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 2, 2, 1, 1, 3, 3, 3, 1, 3, 1 },
			{ 2, 2, 1, 1, 3, 3, 3, 1, 3, 2 }, 
	};*/

	/*int[][]  StarArrayGiven = { 
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 }, 
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 }, 
			{ 1, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 2, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 2, 2, 3, 3, 3, 3, 3, 1, 1, 1 },
			{ 2, 2, 3, 3, 3, 3, 3, 1, 1, 2 }, 
	};*/
	
	//--int StarArray_x = 0;

	public void initStarArrayData___old(){
		ColumnNum=10;
		StarTotalNum=100;
		Random random = new Random();//---待
		int NumColor;
		for(int i=0;i<10;i++){//行
			StarNum[i]=10;
			ColumnMove[i]=new Move(i,10,i*Constant.StarStep,i*Constant.StarStep,this);
			for(int j=0;j<10;j++){//列
				NumColor=random.nextInt(5)+ 1;
				//--NumColor=StarArrayGiven[i][j];
				StarArray[i][j]=new Star(i,j,NumColor,getColorBycolor(NumColor),
						getStarRegionBycolor(NumColor),StarRegion_select,this);
				StarArray[i][j].x=j*Constant.StarStep;
				StarArray[i][j].finalFall_y=StarArray[i][j].y=Constant.StarArray_y+(9-i)*Constant.StarStep;
			}
		}

		if(isGuDianByAll(StarArray)){
			LogW.i("zddz_001", "--GameScreen--initStarArrayData--isGuDianByAll--recount-->>");
			initStarArrayData();
			return;
		}
		for(int i=0;i<10;i++){//列数量,索引从下往上
			System.out.print("--");
			for(int j=0;j<10;j++){//行数量,索引从左往右
				Star mStar=StarArray[i][j];
				if(mStar!=null){
					System.out.print(getStringBycolor(mStar.color));
				}
			}
			System.out.print("\n");
		}
		System.err.println("---------->>");
		LogW.i("zddz_001", "--GameScreen--initStarArrayData()--StarArray.length-->>"+StarArray.length);
		LogW.i("zddz_001", "--GameScreen--initStarArrayData()--StarArray[0].length-->>"+StarArray[0].length);
	}

	public void initStarArrayData() {
		ColumnNum = 10;
		StarTotalNum = 100;
		Random random = new Random();
		int NumColor;
		for (int i = 0; i < 10; i++) {//行
			StarNum[i] = 10;
			ColumnMove[i] = new Move(i, 10, i * Constant.StarStep,
					i * Constant.StarStep, this);
			for (int j = 0; j < 10; j++) {//列
				NumColor = random.nextInt(5) + 1;
				StarArray[i][j] = new Star(i, j, NumColor, getColorBycolor(NumColor),
						getStarRegionBycolor(NumColor), StarRegion_select, this);
				StarArray[i][j].x = j * Constant.StarStep;
				StarArray[i][j].finalFall_y = StarArray[i][j].y =
						Constant.StarArray_y + (9 - i) * Constant.StarStep;
			}
		}
		if (isGuDianByAll(StarArray)) {
			initStarArrayData();
			return;
		}
	}

	//==================================================================

	SequenceAction StarScoreHint_Action;
	
	/***
	 * sigle=(2*n-1)*5
	 * TOTAL=5*N*N
	 * @param num--最大-100
	 */
	public void setStarScoreHint(int num){
		int score=5*num*num;//最大50000
		float xx=0;
		int index=0;
		int num_temp;
		String score_str=score+"";
		String num_str=num+"";
		int length_num=num_str.length();
		int length_score=score_str.length();
		if((length_score+length_num)>=9){
			return;//error;
		}
		float width=(num_str.length()+score_str.length())*Num_Image_aa[0].getWidth()+42*Constant.perW*3;
		
		stage.getRoot().removeActor(StarScoreHint_Group);
		StarScoreHint_Group.clearChildren();
		StarScoreHint_Group.clearActions();
		
		StarScoreHint_Group.setSize(width, Num_Image_aa[0].getHeight());
		StarScoreHint_Group.setOrigin(width/2, Num_Image_aa[0].getHeight()/2);
		StarScoreHint_Group.setPosition((Gdx.graphics.getWidth()-width)/2, (1136-300)*Constant.perH);
		
		for(int i=0;i<length_num;i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			//Image img=new Image(new TextureRegion(Num_Region_aa[num_temp]));
			Num_Image_aa[index].setDrawable(new TextureRegionDrawable(Num_Region_aa[num_temp]));
			Num_Image_aa[index].setSize(Num_Image_aa[0].getWidth(), 43f*Constant.perH);
			Num_Image_aa[index].setPosition(xx, 0);
			StarScoreHint_Group.addActor(Num_Image_aa[index]);
			xx+=Num_Image_aa[0].getWidth();
			index++;
		}
		
		TextImage_LianXiao.setPosition(xx, 0);
		StarScoreHint_Group.addActor(TextImage_LianXiao);
		xx+=TextImage_LianXiao.getWidth();
		
		for(int i=0;i<length_score;i++){
			num_temp=Integer.parseInt(score_str.charAt(i)+"");
			///Image img=new Image(new TextureRegion(Num_Region_aa[num_temp]));
			Num_Image_aa[index].setDrawable(new TextureRegionDrawable(Num_Region_aa[num_temp]));
			Num_Image_aa[index].setSize(Num_Image_aa[0].getWidth(), 43f*Constant.perH);
			Num_Image_aa[index].setPosition(xx, 0);
			xx+=Num_Image_aa[0].getWidth();
			StarScoreHint_Group.addActor(Num_Image_aa[index]);
			index++;
		}
		
		//--TextImage_Fen.setPosition(width-42*Constant.perW, 0);
		TextImage_Fen.setPosition(xx, 0);
		StarScoreHint_Group.addActor(TextImage_Fen);
		
		if(StarScoreHint_Action==null){
			/*StarScoreHint_Action =Actions.sequence(
					Actions.parallel(Actions.scaleTo(0.3f, 0.3f, 0), Actions.alpha(0.2f, 0)),
					Actions.parallel(Actions.scaleTo(1.0f, 1.0f, 0.3f), Actions.alpha(1.0f, 0.3f)),
					Actions.parallel(Actions.scaleTo(1.0f, 1.0f, 3)),
					Actions.parallel(Actions.scaleTo(1.0f, 1.0f, 0.7f), Actions.alpha(0.0f, 0.7f)));*/
		}
		StarScoreHint_Action = Actions.sequence(
				Actions.parallel(Actions.scaleTo(0.3f, 0.3f, 0), Actions.alpha(0.2f, 0)),
				Actions.parallel(Actions.scaleTo(1.0f, 1.0f, 0.3f), Actions.alpha(1.0f, 0.3f)),
				Actions.parallel(Actions.scaleTo(1.0f, 1.0f, 3)),
				Actions.parallel(Actions.scaleTo(1.0f, 1.0f, 0.7f), Actions.alpha(0.0f, 0.7f)));
	
		StarScoreHint_Action.restart();
		StarScoreHint_Group.addAction(StarScoreHint_Action);
		stage.addActor(StarScoreHint_Group);
	}
	
	public void cannelStarScoreHint(){
		stage.getRoot().removeActor(StarScoreHint_Group);
		StarScoreHint_Group.clearChildren();
		StarScoreHint_Group.clearActions();
	}
	
	//==========================================================
	public void setStarLeftNum_Hint(int num){
		if(num<0){
			num=0;
		}
		String num_str=num+"";
		float width=num_str.length()*StarLeftNum_num_width+200*Constant.perH;
		if(StarLeftNum_Group==null){
			StarLeftNum_Group=new Group();
		}
		StarLeftNum_Group.setSize(width, 44*Constant.perH);
		StarLeftNum_Group.setOrigin(width/2,22*Constant.perH);
		StarLeftNum_Group.setPosition((Gdx.graphics.getWidth()-width)/2,300*Constant.perH);
		StarLeftNum_Group.clearChildren();
		StarLeftNum_Group.clearActions();
		
		if(StarLeftNum_left==null){
			StarLeftNum_left= new Image(new TextureRegion(StarLeftNum_Region,0,0,80,44));
			StarLeftNum_left.setSize(80*Constant.perW, 44f*Constant.perH);
			StarLeftNum_left.setPosition(0, 0);
		}
		
		if(StarLeftNum_num==null){
			StarLeftNum_num=new Image[3];
			for(int i=0;i<StarLeftNum_num.length;i++){
				StarLeftNum_num[i]=new Image();
				StarLeftNum_num[i].setSize(StarLeftNum_num_width,StarLeftNum_num_height);
				StarLeftNum_num[i].setColor(new Color(1, 1, 1, 1));
			}
		}
		
		if(StarLeftNum_right==null){
			StarLeftNum_right= new Image(new TextureRegion(StarLeftNum_Region,80,0,120,44));
			StarLeftNum_right.setSize(120*Constant.perW, 44f*Constant.perH);
		}
		
		StarLeftNum_Group.addActor(StarLeftNum_left);
		
		int num_temp;
		for(int i=0;i<num_str.length();i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			StarLeftNum_num[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			StarLeftNum_num[i].setColor(Color.WHITE);
			StarLeftNum_num[i].setPosition(StarLeftNum_num_width*i+80*Constant.perW, 0);
			StarLeftNum_Group.addActor(StarLeftNum_num[i]);
		}
		
		StarLeftNum_right.setPosition(80*Constant.perW+num_str.length()*StarLeftNum_num_width, 0);
		StarLeftNum_Group.addActor(StarLeftNum_right);
		
		if(!stage.getActors().contains(StarLeftNum_Group, true)){
			//--JiangLiFen_Group.getParent();
			stage.addActor(StarLeftNum_Group);
		}
		SequenceAction m_alpha_Action = Actions.sequence(
				Actions.parallel(Actions.alpha(1.0f)));
		StarLeftNum_Group.addAction(m_alpha_Action);
	}
	
	//==========================================================

	public void setJiangLiFen(){
		if(index_JiangLiFen<0){
			index_JiangLiFen=0;
		}
		if(index_JiangLiFen>10){
			index_JiangLiFen=10;
		}
		int num=JiangLiFen_value[index_JiangLiFen];
		//--int num=JiangLiFen_value[0];
		now_JiangLiFen=num;
		
		float width=4*JiangLiFen_num_width+101*Constant.perW;
		if(JiangLiFen_Group==null){
			JiangLiFen_Group=new Group();
			JiangLiFen_Group.setSize(width, JiangLiFen_num_height);
			JiangLiFen_Group.setOrigin(width/2,JiangLiFen_num_height/2);
		}
		JiangLiFen_Group.setPosition((Gdx.graphics.getWidth()-width)/2,380*Constant.perH);
		JiangLiFen_Group.clearChildren();
		//--JiangLiFen_Group.clearActions();
		
		if(JiangLiFen_left==null){
			JiangLiFen_left= new Image(JiangLiFen_left_Region);
			JiangLiFen_left.setSize(101*Constant.perW, 57*Constant.perH);
			JiangLiFen_left.setPosition(0, 0);
		}
		
		if(JiangLiFen_num==null){
			JiangLiFen_num=new Image[4];
			for(int i=0;i<JiangLiFen_num.length;i++){
				JiangLiFen_num[i]=new Image();
				JiangLiFen_num[i].setSize(JiangLiFen_num_width,JiangLiFen_num_height);
				JiangLiFen_num[i].setColor(new Color(1, 1, 1, 1));
			}
		}
		
		JiangLiFen_Group.addActor(JiangLiFen_left);
		String num_str=num+"";
		int num_temp;
		for(int i=0;i<num_str.length();i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			JiangLiFen_num[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			JiangLiFen_num[i].setColor(Color.WHITE);
			JiangLiFen_num[i].setPosition(JiangLiFen_num_width*i+101*Constant.perW, 0);
			JiangLiFen_Group.addActor(JiangLiFen_num[i]);
		}
		
		JiangLiFen_Group.setScale(0.6f, 0.6f);
		if(!stage.getActors().contains(JiangLiFen_Group, true)){
			//--JiangLiFen_Group.getParent();
			stage.addActor(JiangLiFen_Group);
		}
		
		SequenceAction m_alpha_Action = Actions.sequence(
				Actions.parallel(Actions.alpha(1.0f)));
		JiangLiFen_Group.addAction(m_alpha_Action);
	}
	
	public void deal_JiangLiFen(){
		if(index_JiangLiFen<0){
			index_JiangLiFen=0;
		}
		if(index_JiangLiFen>10){
			index_JiangLiFen=10;
		}
		int num=JiangLiFen_value[index_JiangLiFen];
		//--int num=JiangLiFen_value[0];
		now_JiangLiFen=num;
		setJiangLiFen_num(now_JiangLiFen);
	}
	
	public void setJiangLiFen_num(int value){
		JiangLiFen_Group.clearChildren();
		//--JiangLiFen_Group.clearActions();
		JiangLiFen_Group.addActor(JiangLiFen_left);
		String num_str=value+"";
		int num_temp;
		for(int i=0;i<num_str.length();i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			JiangLiFen_num[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			JiangLiFen_num[i].setColor(Color.WHITE);
			JiangLiFen_num[i].setPosition(JiangLiFen_num_width*i+101*Constant.perW, 0);
			JiangLiFen_Group.addActor(JiangLiFen_num[i]);
		}
	}
	
	public void setJiangLiFen_Aniam_start(){
		//--stage.getRoot().removeActor(StarScoreHint_Group);
		JiangLiFen_Group.clearActions();
		ActionListenning mActionListenning=new ActionListenning(new ActionListenning.ToUpdate(){
			@Override
			public void done() {
				JiangLiFen_Group.clearActions();
				JiangLiFen_Group.setScale(0.8f, 0.8f);
			}
		});
		SequenceAction JiangLiFen_Group_Action = Actions.sequence(
				Actions.parallel(Actions.scaleTo(0.6f, 0.6f, 0)),
				Actions.parallel(Actions.scaleTo(0.8f, 0.8f, 0.8f)),
				mActionListenning);
		JiangLiFen_Group_Action.restart();
		if(JiangLiFen_Group!=null){
			JiangLiFen_Group.addAction(JiangLiFen_Group_Action);
		}
	}
	
	public void setJiangLiFen_Aniam_end(){
		//--stage.getRoot().removeActor(StarScoreHint_Group);
		JiangLiFen_Group.clearActions();
		ActionListenning mActionListenning=new ActionListenning(new ActionListenning.ToUpdate(){
			@Override
			public void done() {
				JiangLiFen_Group.clearActions();
				JiangLiFen_Group.setScale(0.6f, 0.6f);
				if(now_JiangLiFen>0){
					deal_JiangLiFen_plus(now_JiangLiFen);
				}else{
					set_next_guanka_num();
					//--------------
					prefs.putInteger(Constant.prefe_guanKaNum,now_guanKaNum);
					prefs.putInteger(Constant.prefe_muBiaoScore,now_MuBiaoScore);
					prefs.putInteger(Constant.prefe_score_total,score_total);
				}
			}
		});
		SequenceAction JiangLiFen_Group_Action = Actions.sequence(
				Actions.parallel(Actions.scaleTo(0.6f, 0.6f, 0.6f)),
				mActionListenning);
		JiangLiFen_Group_Action.restart();
		if(JiangLiFen_Group!=null){
			JiangLiFen_Group.addAction(JiangLiFen_Group_Action);
		}
	}
	
	public void deal_JiangLiFen_plus(final int value_plus){
		float width=(value_plus+"").length()*JiangLiFen_num_width+101*Constant.perW;
		//---JiangLiFen_Group.setPosition((Gdx.graphics.getWidth()-width)/2,380*Constant.perH);
		MoveToAction mMoveToAction = new MoveToAction(){
			protected void update (float percent) {
				setJiangLiFen_num((int) (value_plus-value_plus*percent));
			}
			@Override
			protected void end () {
				set_next_guanka_num();
			}
		};
		mMoveToAction.setPosition(0, 1000);
		mMoveToAction.setDuration(2f);
		mMoveToAction.setInterpolation(Interpolation.circleOut);
		
		ActionListenning mActionListenning=new ActionListenning(new ActionListenning.ToUpdate(){
			@Override
			public void done() {
				JiangLiFen_mark=true;
				setScoreChange(value_plus,2f);
			}
		});
		SequenceAction JiangLiFen_Group_Action = Actions.sequence(
				Actions.parallel(Actions.moveTo((Gdx.graphics.getWidth()-width)/2, (1136-300)*Constant.perH, 0.6f, Interpolation.circleOut),
						Actions.scaleTo(0.8f, 0.8f, 0.6f)),
				Actions.parallel(mActionListenning,mMoveToAction));
		JiangLiFen_Group_Action.restart();
		
		if(JiangLiFen_Group!=null){
			JiangLiFen_Group.addAction(JiangLiFen_Group_Action);
		}
	}
	
	//==========================================================
	
	public void setJiangLi_disappear(){
		SequenceAction JiangLiFen_Group_Action = Actions.sequence(
				Actions.parallel(Actions.fadeOut(0.7f)),
				Actions.run(new Runnable(){
					 public void run(){
						 JiangLiFen_Group.clearActions();
						 StarLeftNum_Group.clearActions();
						 JiangLiFen_Group.remove();
						 StarLeftNum_Group.remove();
//						 stage.getActors().removeValue(JiangLiFen_Group, false);
//						 stage.getActors().removeValue(StarLeftNum_Group, false);
					 }
				}));
		
		SequenceAction m_Group_Action = Actions.sequence(
				Actions.parallel(Actions.fadeOut(0.7f)));
		
		JiangLiFen_Group_Action.restart();
		m_Group_Action.restart();
		JiangLiFen_Group.addAction(JiangLiFen_Group_Action);
		StarLeftNum_Group.addAction(m_Group_Action);
	}
	
	//==========================================================
	
	public void set_next_guanka_num(){
		guanKaNum_mark=true;
		++now_guanKaNum;
		set_next_guanKa(now_guanKaNum);
		setGuanKaNum_Left(now_guanKaNum);
	}
	
	public void set_next_guanKa(final int value_num){
		setJiangLi_disappear();
		
		if(GuanKaCentre_Image==null){
			GuanKaCentre_Image= new Image(GuanKaCentre_Region);
			GuanKaCentre_Image.setSize(119*Constant.perW, 67*Constant.perH);
			GuanKaCentre_Image.setPosition(0, 0);
		}
		String num_str=value_num+"";
		int num_length=num_str.length();
		float num_start=GuanKaCentre_Image.getWidth();
		float width=num_length*GuanKaCentreNum_Group_num_width+num_start;
		
		if(GuanKaCentreNum_Group==null){
			GuanKaCentreNum_Group=new Group();
			GuanKaCentreNum_Group.setY(580f*Constant.perH);//119-67
			GuanKaCentreNum_Group.setHeight(67f*Constant.perH);//119-67
		}
		if(GuanKaCentre_num==null){
			GuanKaCentre_num=new Image[3];
			for(int i=0;i<GuanKaCentre_num.length;i++){
				GuanKaCentre_num[i]=new Image();
				GuanKaCentre_num[i].setSize(GuanKaCentreNum_Group_num_width, GuanKaCentreNum_Group_num_height);
				GuanKaCentre_num[i].setPosition(num_start+i*GuanKaCentreNum_Group_num_width, 0);
			}
		}
		
		GuanKaCentreNum_Group.setWidth(width);
		GuanKaCentreNum_Group.setOrigin(width/2,GuanKaCentreNum_Group.getHeight()/2);
		//--GuanKaCentreNum_Group.setX(Gdx.graphics.getWidth()+GuanKaCentreNum_Group.getWidth());
		GuanKaCentreNum_Group.setX(Gdx.graphics.getWidth());
		
		GuanKaCentreNum_Group.clearChildren();
		GuanKaCentreNum_Group.clearActions();
		
		GuanKaCentreNum_Group.addActor(GuanKaCentre_Image);
		
		int num_temp;
		for(int i=0;i<num_length;i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			GuanKaCentre_num[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			GuanKaCentreNum_Group.addActor(GuanKaCentre_num[i]);
		}
		//------------------------------------------
		TimerActionListenning mActionListenning=new TimerActionListenning(0.6f){
			@Override
			protected void end () {
				set_next_MuBiaoFen_centre(now_MuBiaoScore);
			}
		};
		SequenceAction mSequenceAction = Actions.sequence(
				//--Actions.parallel(Actions.moveTo(Gdx.graphics.getWidth()+GuanKaCentreNum_Group.getWidth(),GuanKaCentreNum_Group.getY(), 0)),
				Actions.parallel(Actions.moveTo((Gdx.graphics.getWidth()-width)/2,GuanKaCentreNum_Group.getY(), 0.8f, Interpolation.circleOut)),
				mActionListenning);
	
		mSequenceAction.restart();
		GuanKaCentreNum_Group.addAction(mSequenceAction);
		if(!stage.getActors().contains(GuanKaCentreNum_Group, true)){
			stage.addActor(GuanKaCentreNum_Group);
		}
	}
	
	//==========================================================
	public void set_next_MuBiaoFen_centre(final int value_num){
		if(MuBiaoFen_centre_Image==null){
			MuBiaoFen_centre_Image= new Image(MuBiaoFen_centre_Region);
			MuBiaoFen_centre_Image.setSize(151*Constant.perW, 40*Constant.perH);
			MuBiaoFen_centre_Image.setPosition(0, 0);
		}
		String num_str=value_num+"";
		int num_length=num_str.length();
		float num_start=MuBiaoFen_centre_Image.getWidth();
		float width=num_length*MuBiaoFen_centre_num_width+num_start;
		
		if(MuBiaoFen_centre_Group==null){
			MuBiaoFen_centre_Group=new Group();
			MuBiaoFen_centre_Group.setY(480f*Constant.perH);//119-67
			MuBiaoFen_centre_Group.setHeight(40f*Constant.perH);//119-67
		}
		MuBiaoFen_centre_Group.setWidth(width);
		MuBiaoFen_centre_Group.setOrigin(width/2,MuBiaoFen_centre_Group.getHeight()/2);
		MuBiaoFen_centre_Group.setX(Gdx.graphics.getWidth());
		
		if(MuBiaoFen_centre_num==null){
			MuBiaoFen_centre_num=new Image[8];
			for(int i=0;i<MuBiaoFen_centre_num.length;i++){
				MuBiaoFen_centre_num[i]=new Image();
				MuBiaoFen_centre_num[i].setSize(MuBiaoFen_centre_num_width, MuBiaoFen_centre_num_height);
				MuBiaoFen_centre_num[i].setPosition(num_start+i*MuBiaoFen_centre_num_width, 0);
			}
		}
		
		MuBiaoFen_centre_Group.clearChildren();
		MuBiaoFen_centre_Group.clearActions();
		MuBiaoFen_centre_Group.addActor(MuBiaoFen_centre_Image);
		
		int num_temp;
		for(int i=0;i<num_length;i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			MuBiaoFen_centre_num[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			MuBiaoFen_centre_Group.addActor(MuBiaoFen_centre_num[i]);
		}
		//------------------------------------------
		TimerActionListenning mActionListenning=new TimerActionListenning(1){
			@Override
			protected void end () {
				setGuanka_leave();
			}
		};
		SequenceAction mSequenceAction = Actions.sequence(
				Actions.parallel(Actions.moveTo((Gdx.graphics.getWidth()-width)/2,MuBiaoFen_centre_Group.getY(), 0.8f, Interpolation.circleOut)),
				mActionListenning);
		
		mSequenceAction.restart();
		MuBiaoFen_centre_Group.addAction(mSequenceAction);
		if(!stage.getActors().contains(MuBiaoFen_centre_Group, true)){
			stage.addActor(MuBiaoFen_centre_Group);
		}
	}
	
	//==========================================================
	public void setGuanka_leave(){
		SequenceAction Guanka_Action = Actions.sequence(
				Actions.parallel(Actions.moveBy(-Gdx.graphics.getWidth(),0,0.7f, Interpolation.circleOut)),
				Actions.run(new Runnable(){
					 public void run(){
						 GuanKaCentreNum_Group.clearActions();
						 MuBiaoFen_centre_Group.clearActions();
						 GuanKaCentreNum_Group.remove();
						 MuBiaoFen_centre_Group.remove();
						 setStar_Init_Fall(true);
					 }
				}));
		
		SequenceAction MuBiaoFen_Action = Actions.sequence(
				Actions.parallel(Actions.moveBy(-Gdx.graphics.getWidth(),0,0.7f, Interpolation.circleOut)));
		Guanka_Action.restart();
		MuBiaoFen_Action.restart();
		GuanKaCentreNum_Group.addAction(Guanka_Action);
		MuBiaoFen_centre_Group.addAction(MuBiaoFen_Action);
	} 
	
	//==========================================================
	
	public float gravity_score_now;
	public float Vt_score_now;
	public float V0_score_now;

	ScoreAccelerateAction mScoreAccelerateAction;
	
	public void setNowGetScore(int Value){
		score_total=Value;
		score_now=0;
		deal_ScoreChange(Value);
	}
	
	public void setScoreChange(int addValue,float duration){
		score_total+=addValue;
		StarScoreNow_Group.clearActions();
		
		/*if(mScoreAccelerateAction==null){
			mScoreAccelerateAction=new ScoreAccelerateAction(this);
		}*/
		mScoreAccelerateAction=new ScoreAccelerateAction(this);
//		mScoreAccelerateAction.setInterpolation(Interpolation.circleOut);
//		mScoreAccelerateAction.setInterpolation(Interpolation.sineOut);
//		mScoreAccelerateAction.setInterpolation(Interpolation.exp5Out);
		mScoreAccelerateAction.setInterpolation(Interpolation.pow2Out);
		float duration_true;
		if(mScoreAccelerateAction.getTime()>duration){
			duration_true=mScoreAccelerateAction.getTime();
		}else{
			duration_true=duration;
		}
		mScoreAccelerateAction.setDuration(duration_true);
		mScoreAccelerateAction.restart(score_total-score_now);
		StarScoreNow_Group.addAction(mScoreAccelerateAction);
	}
	
	public void deal_ScoreChange(int addValue){
		LogW.e("zddz_004", "--GameScreen--deal_ScoreChange--addValue-->>"+addValue);
		score_now+=addValue;
		String num_str=score_now+"";
		int length_num=num_str.length();
		if(length_num>8){
			return;
		}
		
		float width=length_num*75f*Constant.perW;
		//StarScoreHint_Group.clearChildren();
		//StarScoreNow_Group.clearActions();
		StarScoreNow_Group.setSize(width, Image_ScoreNow[0].getHeight());
		StarScoreNow_Group.setOrigin(width/2, Image_ScoreNow[0].getHeight()/2);
		StarScoreNow_Group.setPosition((Gdx.graphics.getWidth()-width)/2, (1136-200)*Constant.perH);
		
		int old_size=StarScoreNow_Group.getChildren().size;
		int differ=length_num-old_size;
		if(differ<0){
			return;
		}
		float xx=0;
		for(int i=0;i<differ;i++){
			StarScoreNow_Group.addActor(Image_ScoreNow[old_size+i]);
		}

		SnapshotArray<Actor> mChildren=StarScoreNow_Group.getChildren();
		int num_temp;
		for(int i=0;i<mChildren.size;i++){
			num_temp=Integer.parseInt(num_str.charAt(i)+"");
			Image_ScoreNow[i].setDrawable(new TextureRegionDrawable(Num_Region_bb[num_temp]));
			Image_ScoreNow[i].setPosition(xx, 0);
			xx+=Image_ScoreNow[0].getWidth();
		}
	}
	
	//==========================================================mmm
	
	float SingleScorePicWidth=42*Constant.perW;
	float SingleScorePicheight=43f*Constant.perH;

	/**
	 * single=(2*n-1)*5 最大为2000-5=1995
	 * @param score
	 * @param Pos_x
	 * @param Pos_y
	 */
	public void setSingleScoreAnima(int score,float Pos_x,float Pos_y){
		if(score>1995){
			return;
		}
		String score_str=score+"";
		int length_num=score_str.length();
		float width=score_str.length()*SingleScorePicWidth;
		final Group mGroup=new Group();
		mGroup.setSize(width, SingleScorePicheight);
		mGroup.setOrigin(width/2, SingleScorePicheight/2);
		mGroup.setPosition(Pos_x*Constant.perW,Pos_y*Constant.perH);
		int num_temp;
		float xx=0;
		for(int i=0;i<length_num;i++){
			num_temp=Integer.parseInt(score_str.charAt(i)+"");
			Image mImage=new Image(new TextureRegion(Num_Region_aa[num_temp]));
			mImage.setSize(SingleScorePicWidth, SingleScorePicheight);
			mImage.setPosition(xx, 0);
			xx+=SingleScorePicWidth;
			mGroup.addActor(mImage);
		}
		ActionListenning mActionListenning=new ActionListenning(new ActionListenning.ToUpdate(){
			@Override
			public void done() {
				mGroup.clearActions();
				stage.getActors().removeValue(mGroup, false);
			}
		});
		SequenceAction mAction = Actions.sequence(
				Actions.parallel(Actions.scaleTo(0.6f, 0.6f, 0.8f),
						Actions.moveTo((Gdx.graphics.getWidth()-width)/2f,(1136-200)*Constant.perH,0.8f, Interpolation.circleOut)),
				Actions.parallel(Actions.alpha(0f, 0.3f)),
				mActionListenning);
		mAction.restart();
		mGroup.addAction(mAction);
		stage.getRoot().addActorAt(0,mGroup);
	}
	
	//==========================================================
	
	float StarRegionScorePicWidth=64*Constant.perW;
	float StarRegionScorePicheight=65f*Constant.perH;
	
	/***
	 * TOTAL=5*N*N--50000
	 * num--最大-100
	 */
	public void setStarRegionScoreAnima(int score,float Pos_x,float Pos_y){
		if(score>50000){
			return;
		}
		String score_str=score+"";
		int length_num=score_str.length();
		float width=score_str.length()*StarRegionScorePicWidth;
		final Group mGroup=new Group();
		mGroup.setSize(width, StarRegionScorePicheight);
		mGroup.setOrigin(width/2, StarRegionScorePicheight/2);
		float Position_x=Pos_x*Constant.perW-mGroup.getOriginX();
		if(Position_x<0){
			Position_x=0;
		}
		if((Position_x+width)> Gdx.graphics.getWidth()){
			Position_x= Gdx.graphics.getWidth()-width;
		}
		mGroup.setPosition(Position_x,Pos_y-Constant.perH+mGroup.getOriginY());
		int num_temp;
		float xx=0;
		for(int i=0;i<length_num;i++){
			num_temp=Integer.parseInt(score_str.charAt(i)+"");
			//--Image mImage=new Image(new TextureRegion(Num_Region_aa[num_temp]));
			Image mImage=new Image(Num_Region_aa[num_temp]);
			mImage.setSize(StarRegionScorePicWidth, StarRegionScorePicheight);
			mImage.setPosition(xx, 0);
			xx+=StarRegionScorePicWidth;
			mGroup.addActor(mImage);
		}
		
		ActionListenning mActionListenning=new ActionListenning(new ActionListenning.ToUpdate(){
			@Override
			public void done() {
				mGroup.clearActions();
				stage.getActors().removeValue(mGroup, false);
			}
		});
		SequenceAction mAction = Actions.sequence(
				Actions.parallel(Actions.parallel(Actions.alpha(0f, 0f))),
				Actions.parallel(Actions.parallel(Actions.alpha(1f, 0.3f))),
				Actions.parallel(Actions.moveTo(mGroup.getX(),mGroup.getY()+64*Constant.perH,2f, Interpolation.circleOut),
						Actions.parallel(Actions.alpha(0, 2f))),
				mActionListenning);
		mAction.restart();
		mGroup.addAction(mAction);
		stage.getRoot().addActorAt(0,mGroup);
	}
	
	//==========================================================
	
	ArrayList<StarParticleManage> StarParticleManage_List=new ArrayList<StarParticleManage>();
	StarParticleManage myStarParticleManage;
	
	public void deal_StarParticleManage(Color mColor, float xCentre, float yCentre, int star_num){
		myStarParticleManage=new StarParticleManage(StarParticleRegion,mColor,xCentre,yCentre,star_num);
		StarParticleManage_List.add(myStarParticleManage);
	}
	
	//==========================================================
	
	public boolean deal_touchDown_____old_ok(float x,float y){
		LogW.i("zddz_001", "--GameScreen--deal_touchDown--001-->");
		//LogW.i("zddz_001", "--GameScreen--deal_touchDown--x-->"+x);
		//LogW.i("zddz_001", "--GameScreen--deal_touchDown--y-->"+y);
		
		if(y>Constant.StarArray_y_top||Constant.StarArray_y_top<Constant.StarArray_y){
		//--if(y>StarArray_y_top||StarArray_y_top<StarArray_y||AnimaMove){
			cannel_LastToUpdate_GameHint_Start();
			return false;
		}
		int column=(int)(x/Constant.StarStep);
		if(column<0){
			column=0;
		}
		if(column>9){
			column=9;
		}
		
		float row_y=y-Constant.StarArray_y;
		int row=(int)(row_y/Constant.StarStep);
		//-LogW.i("zddz_001", "--GameScreen--deal_touchDown--01--row-->"+row);
		if(row<0){
			row=0;
		}
		if(row>9){
			row=9;
		}
		row=9-row;
		
		//LogW.i("zddz_001", "--GameScreen--deal_touchDown--row-->"+row);
		//LogW.i("zddz_001", "--GameScreen--deal_touchDown--column-->"+column);
		
		Star StarDown=StarArray[row][column];
		//--StarDown=null;
		if(StarDown==null){
			cannel_LastToUpdate_GameHint_Start();
			cannelStarScoreHint();
			return false;
		}
		int yy=ColumnMove.length;
		
		if(StarDown.AnimaMove&&yy>0){
			cannel_LastToUpdate_GameHint_Start();
			cannelStarScoreHint();
			return false;
		}
		
		LogW.i("zddz_001", "--GameScreen--deal_touchDown--002-->");
		if(StarDown.select==1){
			//触发消除
			startRemove(x,y);
			return true;
		}else{
			//清除之前的选中的标记
			for(int i=0;i<StarArray.length;i++){//
				for(int j=0;j<StarArray[i].length;j++){//
					Star mStar=StarArray[i][j];
					if(mStar!=null){
						mStar.select=0;
						mStar.check=0;
						mStar.select_hint=0;
						mStar.check_hint=0;
					}
				}
			}
		}
		
		LogW.i("zddz_001", "--GameScreen--deal_touchDown--003-->");
		StarDown.select=1;
		StarDown.check=1;
		StarSelectListTemp.clear();
		
		StarSelectListTemp.add(StarDown);
		int count[]=new int[1];
		count[0]++;
		check_touch(StarDown,count,StarSelectListTemp);
		if(StarSelectListTemp.size()<2){
			StarSelectListTemp.clear();
			StarDown.select=0;
			StarDown.check=0;
			cannel_LastToUpdate_GameHint_Start();
			cannelStarScoreHint();
			return true;
		}else{
			if(singleTouchRemoveMark){
				//触发消除
				startRemove(x,y);
				return true;
			}
		}
		
		cannel_LastToUpdate_GameHint_Start();
		setToUpdate_GameSpring(StarSelectListTemp);
		setStarScoreHint(StarSelectListTemp.size());
		LogW.i("zddz_001", "--GameScreen--deal_touchDown--last-->");
		return true;
	}

	public boolean deal_touchDown(float x, float y) {
		if (y > Constant.StarArray_y_top ||
				Constant.StarArray_y_top < Constant.StarArray_y) {
			cannel_LastToUpdate_GameHint_Start();
			return false;
		}
		int column = (int) (x / Constant.StarStep);
		if (column < 0) {
			column = 0;
		}
		if (column > 9) {
			column = 9;
		}
		float row_y = y - Constant.StarArray_y;
		int row = (int) (row_y / Constant.StarStep);
		if (row < 0) {
			row = 0;
		}
		if (row > 9) {
			row = 9;
		}
		row = 9 - row;
		Star StarDown = StarArray[row][column];
		if (StarDown == null) {
			cannel_LastToUpdate_GameHint_Start();
			cannelStarScoreHint();
			return false;
		}
		int yy = ColumnMove.length;
		if (StarDown.AnimaMove && yy > 0) {
			cannel_LastToUpdate_GameHint_Start();
			cannelStarScoreHint();
			return false;
		}

		if (StarDown.select == 1) {
			//触发消除
			startRemove(x, y);
			return true;
		} else {
			//清除之前的选中的标记
			for (int i = 0; i < StarArray.length; i++) {//
				for (int j = 0; j < StarArray[i].length; j++) {
					Star mStar = StarArray[i][j];
					if (mStar != null) {
						mStar.select = 0;
						mStar.check = 0;
						mStar.select_hint = 0;
						mStar.check_hint = 0;
					}
				}
			}
		}
		StarDown.select = 1;
		StarDown.check = 1;
		StarSelectListTemp.clear();

		StarSelectListTemp.add(StarDown);
		int count[] = new int[1];
		count[0]++;
		check_touch(StarDown, count, StarSelectListTemp);
		if (StarSelectListTemp.size() < 2) {
			StarSelectListTemp.clear();
			StarDown.select = 0;
			StarDown.check = 0;
			cannel_LastToUpdate_GameHint_Start();
			cannelStarScoreHint();
			return true;
		} else {
			if (singleTouchRemoveMark) {
				//触发消除
				startRemove(x, y);
				return true;
			}
		}
		cannel_LastToUpdate_GameHint_Start();
		setToUpdate_GameSpring(StarSelectListTemp);
		setStarScoreHint(StarSelectListTemp.size());
		return true;
	}

	private void startRemove(float x,float y){
		deal_AnimaMove();
		int num=StarSelectListTemp.size();
		int score=5*num*num;
		setScoreChange(score,num*differ+0.5f);
		cannel_LastToUpdate_GameHint_Waiting();
		setStarScoreHint(StarSelectListTemp.size());
		setStarRegionScoreAnima(num*num*5,x,y);
	}
	
	//---------------
	
	ArrayList<WaitUpdate> WaitUpdateList=new ArrayList<WaitUpdate>();
	boolean GuDianByAll=false;
	public boolean AnimaMove=false;
	
	//本轮所选中的星星列表
	ArrayList<Star> StarSelectListTemp=new ArrayList<Star>();

	int[]  StarNum=new int[10];//列--每一列的数量
	public Move[] ColumnMove=new Move[10];//列--的横移状态
	public int   ColumnNum=10;
	public int   StarTotalNum=100;
	
	public void deal_AnimaMove(){
		LogW.i("zddz_001", "--GameScreen--deal_AnimaMove--001-->");
		//本轮所选中的星星列表
		ArrayList<Star> StarSelectList_Now=new ArrayList<Star>();
		//列--最下面的选择状态的星星，记录有移动动画的列
		Star[] SelectColumnBottomArray=new Star[10];
		
		//所有需要下落的星星
		ArrayList<Star> StarFallList_Now=new ArrayList<Star>();
		ArrayList<StarPack> StarFallList_Now_already=new ArrayList<StarPack>();
		
		//列--需要移动的列
		ArrayList<Move> MoveList_x=new ArrayList<Move>();
		ArrayList<MovePack> MoveList_x_already=new ArrayList<MovePack>();
		
		/*String Log_str="";
		for(int i=0;i<StarSelectList.size();i++){
			Log_str+="("+StarSelectList.get(i).mRow+","+StarSelectList.get(i).mColumn+")-";
		}
		LogW.e("zddz", "-GameScreen-deal_AnimaMove-Log_str-00->"+Log_str);*/
		
	
		//确定哪些列下落-列最下面的星星存入
		SelectColumnBottomArray=new Star[10];
		for(int i=0;i<StarSelectListTemp.size();i++){
			Star StarTemp=StarSelectListTemp.get(i);
			if(SelectColumnBottomArray[StarTemp.mColumn]==null){
				SelectColumnBottomArray[StarTemp.mColumn]=StarTemp;
			}else{
				if(SelectColumnBottomArray[StarTemp.mColumn].mRow<StarTemp.mRow){
					SelectColumnBottomArray[StarTemp.mColumn]=StarTemp;
				}
			}
		}
		/*Log_str="";
		for(int i=0;i<StarSelectColumnArray.length;i++){
			if(StarSelectColumnArray[i]==null){
				Log_str+="null,";
			}else{
				Log_str+="("+StarSelectColumnArray[i].row+","+StarSelectColumnArray[i].column+"),";
			}
		}
		LogW.e("zddz", "-GameScreen-deal_AnimaMove-Log_str-01->"+Log_str);*/
		
	
		//--确定哪些"星星-移动"-计算移动的坐标
		//--移除没有移动动画的列  "null"
		int num = 0;
		int move_mark = 0;
		for(int i=0;i<SelectColumnBottomArray.length;i++){
			num = 0;
			move_mark = 0;
			Star mStarTemp=SelectColumnBottomArray[i];
			if(mStarTemp!=null){
				for(int j=mStarTemp.mRow;j>=0;j--){
					if(StarArray[j][i]==null){
						//--continue;
						break;
					}
					if(StarArray[j][i].select==1){
						num++;
					}else{
						move_mark++;
						if(StarArray[j][i].move_state_y==Constant.StarState_Quiet){
							StarArray[j][i].setFallReady_differ(num*Constant.StarStep);
							StarFallList_Now.add(StarArray[j][i]);
						}else{
							StarPack mStarPack=new StarPack(StarArray[j][i],num*Constant.StarStep);
							StarFallList_Now_already.add(mStarPack);
						}
					}
				}
				//移除没有移动动画的列  "null"--从下面到头顶都是选中的星星
				if(move_mark==0){
					SelectColumnBottomArray[i]=null;
				}
			}
		}
		/*Log_str="";
		for(int i=0;i<StarSelectColumnArray.length;i++){
			if(StarSelectColumnArray[i]==null){
				Log_str+="null,";
			}else{
				Log_str+="("+StarSelectColumnArray[i].row+","+StarSelectColumnArray[i].column+"),";
			}
		}
		LogW.e("zddz", "-GameScreen-deal_AnimaMove-Log_str-02->"+Log_str);*/
		
		//移除已经选中的星星-  "null"
		for(int i=0;i<StarSelectListTemp.size();i++){
			StarArray[StarSelectListTemp.get(i).mRow][StarSelectListTemp.get(i).mColumn]=null;
			StarSelectList_Now.add(StarSelectListTemp.get(i));
		}
		
		//--完成数据上面的-行的-“横向抖落”
		//--完成数据上面的-列的-“纵向抖落”
		//--完成每列剩余星星数量的计算-列的顺序是之前的矩阵的顺序
		Star[][] StarArrayTemp=new Star[10][10];//行,列
		int row_num;
		int Column_num=0;;
		for(int i=0;i<10;i++){
			row_num=9;
			Star  mStar_=SelectColumnBottomArray[i];
			for(int j=9;j>=0;j--){
				if(StarArray[j][i]!=null){
					StarArrayTemp[row_num][Column_num]=StarArray[j][i];
					StarArrayTemp[row_num][Column_num].setStarData(row_num,Column_num);
					
					//星星现在所在的列 "不等" 原来所在的列-有x移动
					if(Column_num!=i){
						StarArrayTemp[row_num][Column_num].setColumnMove();
					}
					
					if(mStar_!=null){
						if(SelectColumnBottomArray[i].mRow==j&&SelectColumnBottomArray[i].mColumn==i){
							SelectColumnBottomArray[i]=StarArrayTemp[row_num][Column_num];
						}
					}
					row_num--;
				}else{
					//--
				}
			}
			StarNum[i]=9-row_num;
			if(StarNum[i]>0){
				Column_num++;
			}
		}
		StarArray=StarArrayTemp;
		/*Log_str="";//------
		for(int i=0;i<10;i++){//列数量,索引从下往上
			Log_str="--";
			for(int j=0;j<10;j++){//行数量,索引从左往右
				Star mStar=StarArray[i][j];
				if(mStar!=null){
					Log_str+=getStringBycolor(mStar.color);
				}else{
					//Log_str+="空";
					Log_str+="  ";
				}
			}
			LogW.i("zddz_10",Log_str);
		}
		LogW.e("zddz_10","--------------------------------");*/
		
		//计算哪些列需要移动
		//SelectColumnBottomArray-横向抖落”
		MoveList_x.clear();
		Star[] SelectColumnBottomArray_temp=new Star[10];
		for(int i=0,index_num=0,column_null_num=0;i<StarNum.length;i++){
			if(StarNum[i]!=0){
				ColumnMove[i].setColumn_num(StarNum[i]);
				SelectColumnBottomArray_temp[index_num]=SelectColumnBottomArray[i];
				if(column_null_num>0){
					Move mMove=ColumnMove[i];
					if(mMove.getMoveMark()==Constant.StarState_Quiet){
						mMove.setReady_Data(i,i-column_null_num,column_null_num*Constant.StarStep);
						MoveList_x.add(mMove);
					}else{
						MovePack mMovePack=new MovePack(mMove,i,i-column_null_num,column_null_num*Constant.StarStep);
						MoveList_x_already.add(mMovePack);
					}
				}
				index_num++;
			}else{
				column_null_num++;
			}
		}
		SelectColumnBottomArray=SelectColumnBottomArray_temp;
		
		//StarNum-横向抖落”
		Move[] ColumnMove_temp=new Move[10];
		int[]  StarNum_temp=new int[10];
		StarTotalNum=0;
		int index_num=0;
		for(int i=0;i<StarNum.length;i++){
			if(StarNum[i]!=0){
				StarNum_temp[index_num]=StarNum[i];
				StarTotalNum+=StarNum[i];
				
				ColumnMove_temp[index_num]=ColumnMove[i];
				ColumnMove_temp[index_num].setColumn(index_num);
				index_num++;
			}
		}
		ColumnNum=index_num;
		StarNum=StarNum_temp;
		ColumnMove=ColumnMove_temp;
		
		//--判断是否都是“孤点”
		//--得到剩余星星数量
		GuDianByAll=isGuDianByAll(StarArray);
		if(GuDianByAll){
			GameOverGlitter=Constant.GameOverGlitter_wait;
			GameOverGlitter_time =0;
			StarLeftList.clear();
			if(StarTotalNum>0){
				for(int i=0;i<StarArray.length;i++){//列数量,索引从下往上
					for(int j=0;j<StarArray[i].length;j++){//行数量,索引从左往右
						Star mStar=StarArray[i][j];
						if(mStar!=null){
							StarLeftList.add(mStar);
						}
					}
				}
			}
			StarLeft_over=StarLeftList.size();
		}
		LogW.e("zddz_10", "-GameScreen-deal_AnimaMove-GuDianByAll->"+GuDianByAll);
		LogW.e("zddz_10", "-GameScreen-deal_AnimaMove-StarTotalNum->"+StarTotalNum);
		WaitUpdateList.add(new WaitUpdate(SelectColumnBottomArray,StarFallList_Now,StarFallList_Now_already
				,MoveList_x,MoveList_x_already,StarSelectList_Now,this));
		AnimaMove=true;
		setToUpdate(mToUpdate_Gameing);
		LogW.i("zddz_001", "--GameScreen--deal_AnimaMove--last-->");
	}
	
	//==========================================================

	public void update(float deltaTime) {
		if(mToUpdate_now!=null){
			mToUpdate_now.doUpdate(deltaTime);
		}
		if(star_hint==1){
			touch_free+=deltaTime;
			if(touch_free>touch_free_period){
				touch_free=0;
				star_hint=3;
				GetSameColorStar_num();
			}
		}
	}
	
	//-------------------------

	public void setToUpdate(ToUpdate ToUpdate_) {
		mToUpdate_now=ToUpdate_;
	}
	
	//-------------------------
	
	public void setStarIntoAnima_b(){
		int differ=Constant.StarStep;
		int top=Constant.GameHeight+differ;
		for(int i=0;i<StarArray.length;i++){//列数量,索引从下往上
			for(int j=StarArray[i].length-1;j>=0;j--){//行数量,索引从左往右
				Star mStarTemp=StarArray[j][i];//
				if(mStarTemp!=null){
					mStarTemp.setFinalFall_y_Ready(mStarTemp.finalFall_y);
					mStarTemp.setY(mStarTemp.y+top);
					mStarTemp.move_state_y=Constant.StarState_Move;
				}
			}
		}
		
		mColumn=0;
		setToUpdate(mToUpdate_StarIntoAnima_b);
	}
	
	int mColumn=0;
	 
	public void deal_ToUpdate_StarIntoAnima_b(float deltaTime){
		int num=0;
		for(int i=StarArray[0].length-1;i>=0;i--){//列数量,
			Star mStarTemp=StarArray[i][mColumn];//
			if(mStarTemp!=null){
				if(mStarTemp.move_state_y==Constant.StarState_Move){
					mStarTemp.update_Y(deltaTime);
					num++;
				}
			}
		}
		if(num==0){
			if(mColumn>=9){
				setToUpdate(mToUpdate_Gameing);
			}else{
				mColumn++;
			}
		}
		//--LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_StarIntoAnima--num-->"+num);
	}
	
	public void setStarIntoAnima___old(){
		int differ=Constant.StarStep*2;
		int top=Constant.GameHeight-differ;
		int value;
		Random random = new Random(System.currentTimeMillis());
		for(int i=0;i<StarArray.length;i++){//列数量,索引从下往上
			value=random.nextInt(differ)+top;
			for(int j=StarArray[i].length-1;j>=0;j--){//行数量,索引从左往右
				Star mStarTemp=StarArray[j][i];//
				if(mStarTemp!=null){
					value=value+differ+random.nextInt(differ);
					mStarTemp.setFinalFall_y_Ready(mStarTemp.finalFall_y);
					mStarTemp.setY(value);
					mStarTemp.move_state_y=Constant.StarState_Move;
					//--LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_StarIntoAnima--value-->"+value);
				}
			}
		}
		setToUpdate(mToUpdate_StarIntoAnima);
	}

	public void setStarIntoAnima() {
		int differ = Constant.StarStep * 2;
		int top = Constant.GameHeight - differ;
		int value;
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < StarArray.length; i++) {//列数量,索引从下往上
			value = random.nextInt(differ) + top;
			for (int j = StarArray[i].length - 1; j >= 0; j--) {//行数量,索引从左往右
				Star mStarTemp = StarArray[j][i];//
				if (mStarTemp != null) {
					value = value + differ + random.nextInt(differ);
					mStarTemp.setFinalFall_y_Ready(mStarTemp.finalFall_y);
					mStarTemp.setY(value);
					mStarTemp.move_state_y = Constant.StarState_Move;
				}
			}
		}
		setToUpdate(mToUpdate_StarIntoAnima);
	}

	public void deal_ToUpdate_StarIntoAnima(float deltaTime) {
		int num = 0;
		for (int i = 0; i < StarArray.length; i++) {//列数量,索引从下往上
			for (int j = StarArray[i].length - 1; j >= 0; j--) {//行数量,索引从左往右
				Star mStarTemp = StarArray[j][i];//
				if (mStarTemp != null) {
					if (mStarTemp.move_state_y == Constant.StarState_Move) {
						mStarTemp.update_Y(deltaTime);
						num++;
					}
				}
			}
		}
		if (num == 0) {
			setToUpdate(mToUpdate_Gameing);
		}
	}
	
	//==========================================================
	
	public void deal_ToUpdate_Gameing(float deltaTime){
		for(int i=0;i<WaitUpdateList.size();){
			WaitUpdate mWaitUpdate = WaitUpdateList.get(i);
			if(mWaitUpdate.state_mark==Constant.WaitUpdate_State_X_Finish){
				WaitUpdateList.remove(i);
			}else{
				mWaitUpdate.update(deltaTime);
				i++;
			}
		}
		if(WaitUpdateList.size()<=0){
			AnimaMove=false;
			if(GuDianByAll){//游戏结束--
				setToUpdate(mToUpdate_GameEnd);
				//添加星星剩余个数文字
				setStarLeftNum_Hint(StarLeftList.size());
				//添加奖励文字-分数
				index_JiangLiFen=0;
				setJiangLiFen();
			}else{
				set_ToUpdate_GameHint_listenning();
				setToUpdate(null);
			}
		}
	}
	
	//==========================================================
	
	public void setToUpdate_GameHint(){
		if(mToUpdate_GameHint==null){
			mToUpdate_GameHint=new ToUpdate(){
				@Override
				public void doUpdate(float deltaTime) {
					deal_ToUpdate_GameHint(deltaTime);
				}};
				
			scale_per=(scale_start-scale_end)/scale_period;
		}
		
		setToUpdate(mToUpdate_GameHint);
		scale_now=1;
		touch_free=0;
		star_hint=3;
	}
	
	public void cannel_LastToUpdate_GameHint_Start(){
		if(star_hint==1||star_hint==3){
			setToUpdate(mToUpdate_Gameing);
			touch_free=0;
			star_hint=1;
			for(int i=0;i<StarList_hint.size();i++){
				StarList_hint.get(i).scale_time=scale_start;
			}
		}
	}
	
	public void cannel_LastToUpdate_GameHint_Waiting(){
		if(star_hint==1||star_hint==3){
			touch_free=0;
			star_hint=2;
			setToUpdate(mToUpdate_Gameing);
			for(int i=0;i<StarList_hint.size();i++){
				StarList_hint.get(i).scale_time=scale_start;
			}
		}
	}
	
	public void set_ToUpdate_GameHint_listenning(){
		touch_free=0;
		star_hint=1;
	}
	
	//--final float touch_free_period=60*60*24;
	final float touch_free_period=6;
	float touch_free=0;
	/**
	 * star_hint==1,开始监听，2--等待，3--正在动画
	 */
	int star_hint=0;
	
	public ArrayList<Star> StarList_hint=new ArrayList<Star>();
	float scale_start=1f;
	float scale_end=0.7f;
	float scale_period=0.7f;
	float scale_per;
	
	float scale_per_now;
	float scale_now;
	
	public void deal_ToUpdate_GameHint(float deltaTime){
		scale_per_now=scale_per*deltaTime;
		scale_now+=scale_per_now;
		
		if(scale_now>=scale_start){
			scale_now=scale_start;
			scale_per=-scale_per;
		}else if(scale_now<=scale_end){
			scale_now=scale_end;
			scale_per=-scale_per;
		}
		for(int i=0;i<StarList_hint.size();i++){
			StarList_hint.get(i).scale_time=scale_now;
		}
	}
	
	//==========================================================
	
	public void setToUpdate_GameSpring(ArrayList<Star> StarList_Spring_){
		if(mToUpdate_GameSpring==null){
			mToUpdate_GameSpring=new ToUpdate(){
				@Override
				public void doUpdate(float deltaTime) {
					deal_ToUpdate_GameSpring(deltaTime);
				}};
		}
		startTime=0;
		y_differ=0;
		v_y_Spring=v_y_initial_Spring;
		StarList_Spring.clear();
		StarList_Spring.addAll(StarList_Spring_);
		setToUpdate(mToUpdate_GameSpring);
	}
	
	public ArrayList<Star> StarList_Spring=new ArrayList<Star>();
	final  float gravity_y_Spring=gravity_y;
	//--public float v_y_initial_Spring=star_v_y_initial;
	public float v_y_initial_Spring=400;
	public float startTime;
	public float v_y_Spring;
	public float y_differ;
	public float y_Spring;
	
	public void deal_ToUpdate_GameSpring(float deltaTime){
		startTime+=deltaTime;
		v_y_Spring = v_y_Spring + gravity_y_Spring * deltaTime;
		LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_GameSpring--v_y_Spring-->>"+v_y_Spring);
		y_differ   = (int) (v_y_Spring * deltaTime);
		
		LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_GameSpring--y_differ-->>"+y_differ);
		for(int i=0;i<StarList_Spring.size();){
			Star mStar=StarList_Spring.get(i);
			y_Spring=mStar.y+y_differ;
			//LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_GameSpring--mStar.y-->>"+mStar.y);
			if(y_Spring<=mStar.finalFall_y){
				mStar.y= (int) mStar.finalFall_y;
				StarList_Spring.remove(i);
			}else{
				mStar.y=(int) y_Spring;
				i++;
			}
		}
		
		if(StarList_Spring.size()<=0){
			setToUpdate(null);
		}
	}
	
	//==========================================================
	
	int   GameOverGlitter=1;
	float GameOverGlitter_time =0;
	float GameOverGlitter_small_time =0;
	float GameOverGlitter_star_num =0;
	
	
	//一局游戏结束剩余的星星数目
	int StarLeft_over=0;
	//本轮所剩余的星星列表
	public ArrayList<Star> StarLeftList=new ArrayList<Star>();
	public void deal_ToUpdate_GameEnd(float deltaTime){
		if(GameOverGlitter==Constant.GameOverGlitter_wait){
			//等待
			GameOverGlitter_time+=deltaTime;
			if(GameOverGlitter_time>=Constant.Glitter_BeforeTime){
				GameOverGlitter=Constant.GameOverGlitter_glitter;
				GameOverGlitter_time =0;
				GameOverGlitter_small_time =Constant.Glitter_Time+1;
			}
		}else if(GameOverGlitter==Constant.GameOverGlitter_glitter){
			//星星闪烁
			GameOverGlitter_time+=deltaTime;
			GameOverGlitter_small_time+=deltaTime;
			if(GameOverGlitter_time>=Constant.Glitter_TotalTime){
				GameOverGlitter=Constant.GameOverGlitter_disappear;
				GameOverGlitter_small_time =0;
				GameOverGlitter_time=0;
				draw_StarArray_mark=true;
				StarLeftNum_Group.setVisible(draw_StarArray_mark);
				setJiangLiFen_Aniam_start();
			}else{
				if(GameOverGlitter_small_time>=Constant.Glitter_Time){
					GameOverGlitter_small_time=0;
					draw_StarArray_mark=!draw_StarArray_mark;
					StarLeftNum_Group.setVisible(draw_StarArray_mark);
					GameOverGlitter_star_num =0;
				}
			}
		}else if(GameOverGlitter==Constant.GameOverGlitter_disappear){
			//10--一个一个消失--冒星星
			if(StarLeftList.size()>0&&GameOverGlitter_star_num<10){
				GameOverGlitter_small_time+=deltaTime;
				if(GameOverGlitter_small_time>=GameOverGlitter_disappear_differ){
					GameOverGlitter_star_num++;
					GameOverGlitter_small_time =0;
					Star mStar=StarLeftList.get(0);
					StarLeftList.remove(mStar);
					StarArray[mStar.mRow][mStar.mColumn]=null;
					index_JiangLiFen++;
					deal_JiangLiFen();
					deal_StarParticleManage(mStar.mColor,mStar.x+Constant.StarStep/2,mStar.y+Constant.StarStep/2,Constant.StarEmitterNumGame);
				}
			}else{
				GameOverGlitter=Constant.GameOverGlitter_over;
				GameOverGlitter_small_time =0;
				GameOverGlitter_star_num=0;
			}
		}else if(GameOverGlitter==Constant.GameOverGlitter_over){
			//--LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_GameEnd--StarLeftList.size()-01->"+StarLeftList.size());
			//剩余的一起消失--有个最大数量--一起冒星星
			setJiangLiFen_Aniam_end();
			int num=0;
			for(;StarLeftList.size()>0;){
				num++;
				Star mStar=StarLeftList.get(0);
				StarLeftList.remove(mStar);
				StarArray[mStar.mRow][mStar.mColumn]=null;
				deal_StarParticleManage(mStar.mColor,mStar.x+Constant.StarStep/2,mStar.y+Constant.StarStep/2,Constant.StarEmitterNumGame);
			    if(num>=Constant.GameOverGlitter_max_num){
			    	break;
			    }
			}
			//--LogW.i("zddz_002", "--GameScreen--deal_ToUpdate_GameEnd--StarLeftList.size()-02->"+StarLeftList.size());
			for(;StarLeftList.size()>0;){
				Star mStar=StarLeftList.get(0);
				StarLeftList.remove(mStar);
				StarArray[mStar.mRow][mStar.mColumn]=null;
			}
			
			GameOverGlitter=-999;
		}
	}
	
	//==========================================================

	int fps_min=100;
	final Color color = new Color(1, 1, 1, 1);
	
	boolean draw_StarArray_mark=true;
	
	public void draw (float deltaTime) {
		//LogW.i("zddz", "--GameScreen--draw--001-->>");
		//--Gdx.gl.glClearColor(1, 0, 0, 1);
		//--Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		
		batcher.enableBlending();
		batcher.begin();
		
		//--batcher.draw(backgroundRegion, 0, 0, Constant.GameWidth, Constant.GameHeight);
		batcher.draw(backgroundRegion,0, 0,
				55, 55, 
				Constant.GameWidth, Constant.GameHeight, 
				1, 1, 0);

		if(draw_StarArray_mark){
			for(int i=0;i<StarArray.length;i++){//列数量,索引从下往上
				for(int j=0;j<StarArray[i].length;j++){//行数量,索引从左往右
					Star mStar=StarArray[i][j];
					if(mStar!=null){
						batcher.draw(mStar.mRegion, mStar.x,mStar.y, 
								Constant.StarStep/2,Constant.StarStep/2, 
								Constant.StarStep, Constant.StarStep,
								mStar.scale_time, mStar.scale_time, 0);
						if(mStar.select==1){
							batcher.draw(mStar.mSelectRegion, mStar.x,mStar.y, 
									Constant.StarStep/2,Constant.StarStep/2, 
									Constant.StarStep, Constant.StarStep,
									mStar.scale_time, mStar.scale_time, 0);
						}
					}
				}
			}
		}

		//--画-执行动画时，被选中的星星(主数组中已经置空)
		for(int i=0;i<WaitUpdateList.size();i++){
			WaitUpdateList.get(i).draw(batcher);
		}
		
		//--画-粒子小小星星
		for(int i=0;i<StarParticleManage_List.size();){
			StarParticleManage myStarParticleManage = StarParticleManage_List.get(i);
			if(myStarParticleManage.draw(batcher, deltaTime)<=0){
				StarParticleManage_List.remove(myStarParticleManage);
			}else{
				i++;
			}
			batcher.setColor(1, 1, 1, 1);
		}
		
		//--画帧率
		int fps= Gdx.graphics.getFramesPerSecond();
		if(fps<fps_min&&fps>5){
			fps_min=fps;
		}
		font.draw(batcher, "3_fps:"+fps, 0, Constant.GameHeight);
		font.draw(batcher, touch_free+"--"+"fpsMin:"+fps_min, 0, Constant.GameHeight-20);
		
		batcher.end();
	}
	
	//==========================================================

	@Override
	public void render(float delta) {
		//---LogW.i("zddz_001", "--GameScreen--render--001-->");
		/**
		 * 刷新位子，相同颜色的提示
		 */
		update(delta);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		draw(delta);
		
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void pause() {
		LogW.i("zddz_001", "--GameScreen--pause--first-->>");
		String StarArray_str="";
		for(int i=0;i<StarArray.length;i++){
			for(int j=0;j<StarArray[i].length;j++){
				if(StarArray[i][j]==null){
					StarArray_str+="0";
				}else{
					StarArray_str+=StarArray[i][j].color;
				}
			}
		}
		//--------------
		prefs.putBoolean(Constant.prefe_pause_do,true);
		prefs.putInteger(Constant.prefe_guanKaNum,now_guanKaNum);
		prefs.putInteger(Constant.prefe_muBiaoScore,now_MuBiaoScore);
		prefs.putInteger(Constant.prefe_score_total,score_total);
		prefs.putString(Constant.prefe_star_array,StarArray_str);

		prefs.putBoolean(Constant.prefe_guanKaNum_mark,guanKaNum_mark);
		prefs.putBoolean(Constant.prefe_JiangLiFen_mark,JiangLiFen_mark);
		prefs.putInteger(Constant.prefe_Star_Left_num,StarLeft_over);

		prefs.flush();
		//--------------
		LogW.i("zddz_001", "--GameScreen--pause--now_guanKaNum-->>"+now_guanKaNum);
		LogW.i("zddz_001", "--GameScreen--pause--now_MuBiaoScore-->>"+now_MuBiaoScore);
		LogW.i("zddz_001", "--GameScreen--pause--score_total-->>"+score_total);
		LogW.i("zddz_001", "--GameScreen--pause--StarArray_str-->>"+StarArray_str);
		LogW.i("zddz_001", "--GameScreen--pause--last-->>");
	}

	@Override
	public void resume() {
		LogW.e("zddz_001", "--GameScreen--resume-->>");



	}

	@Override
	public void resize(int width, int height) {
		LogW.e("zddz_001", "--GameScreen--resize--width-"+width+"--height--"+height+"-->>");
	}

	@Override
	public void show() {
		LogW.e("zddz_001", "--GameScreen--show-->>");
	}

	@Override
	public void hide() {
		LogW.e("zddz_001", "--GameScreen--hide-->>");
	}

	@Override
	public void dispose() {
		LogW.e("zddz_001", "--GameScreen--dispose-->>");
		font.dispose();
		stage.dispose();
	}
	
	//==========================================================
	
	boolean touch_mark=false;
	float touch_x;
	float touch_y;
	int touch_num=0;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/*if(mToUpdate_Gameing != mToUpdate_now){
			return false;
		}*/

		touch_num+=1;
		guiCam.unproject(touchPoint.set(screenX, screenY, 0));
		touch_x=touchPoint.x;
		touch_y=touchPoint.y;
		touch_mark=true;
		deal_touchDown(touch_x,touch_y);
		//-------
		if(touch_x>=Constant.GameWidth-64*2&&touch_y>=Constant.GameHeight-64){
			LogW.e("zddz_002","GameScreen-touchDown-sigleDismiss-EventListener->");
			dealSigleDismiss();
		}
		//-------

		if(test_mark){
			deal_StarParticleManage(Color.WHITE,touch_x,touch_y,30);
			if(touch_x<=100&&touch_y>=1036){
				LogW.e("zddz_002", "--GameScreen--touchDown--003-->");
				fps_min=100;
				//--setStarIntoAnima();
				setStarIntoAnima_b();
				//--GetSameColorStar_num();
			}
		}
		LogW.e("zddz_002", "--GameScreen--touchDown--last-->");
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//--LogW.i("Thread", "--GameScreen--touchUp--pointer-->"+pointer);
		touch_num-=1;
		guiCam.unproject(touchPoint.set(screenX, screenY, 0));
		touch_x=touchPoint.x;
		touch_y=touchPoint.y;
		//--mPaoTai.deal_touch(touchPoint.x,touchPoint.y);
		if(touch_num<=0){
			touch_mark=false;
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//==LogW.i("Thread", "--GameScreen--touchDragged-->");
		guiCam.unproject(touchPoint.set(screenX, screenY, 0));
		touch_x=touchPoint.x;
		touch_y=touchPoint.y;
		//--mPaoTai.deal_touch(touchPoint.x,touchPoint.y);
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode==4){
			return true;
		}
		LogW.i("zddz_002", "--GameScreen--touchDown--keyDown-->"+keycode);
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode==4){
			return true;
		}
		LogW.i("zddz_002", "--GameScreen--keyUp--keycode-->"+keycode);
		return super.keyUp(keycode);
	}
	
	//==========================================================

	private GetSameColorStar mGetSameColorStar;

	public void GetSameColorStar_num(){
		LogW.e("zddz_002", "--GameScreen--GetSameColorStar_num--004--time->"+System.currentTimeMillis());
		if(StarTotalNum<=2){
			return;
		}
		if(mGetSameColorStar==null){
			mGetSameColorStar=new GetSameColorStar();
		}
		Star[][] StarArrayTemp=new Star[10][10];//
		for(int i=0;i<StarArray.length;i++){
			for(int j=0;j<StarArray[i].length;j++){
				StarArrayTemp[i][j]=StarArray[i][j];
			}
		}
		mGetSameColorStar.StarArray=StarArrayTemp;
		mGetSameColorStar.StarTotalNumTemp=StarTotalNum;
		Object[] StarArrayTrue= mGetSameColorStar.getSameColorStarObjectArray();
		LogW.e("zddz_002", "--GameScreen--GetSameColorStar_num--StarArrayTrue.length->"+StarArrayTrue.length);
		if(StarArrayTrue==null||StarArrayTrue.length<2){
			return;
		}
		StarList_hint.clear();
		for(int i=0;i<StarArrayTrue.length;i++){
			StarList_hint.add((Star) StarArrayTrue[i]);
		}
		setToUpdate_GameHint();
		LogW.e("zddz_002", "--GameScreen--GetSameColorStar_num--last-time->"+System.currentTimeMillis());
	}

	//--------------------

	int num_check_touch__=0;//记录方法执行的次数
	/**
	 * 得到所有选中的相同颜色的星星数量
	 * @param mStar--点中的星星
	 * @param count--与中的星星，相同颜色的星星的数量
	 */
	public void check_touch(Star mStar,int count[],ArrayList<Star> StarSelectList_){
		LogW.i("zddz_001", "--GameScreen--check_touch--001--num_check_touch__-->>"+num_check_touch__);
		num_check_touch__++;
		Star mStarTemp=getLeftStar(mStar);
		if(mStarTemp!=null&&mStarTemp.check==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				count[0]++;
				mStarTemp.select=1;
				mStarTemp.check=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,count,StarSelectList_);
			}else{
				mStarTemp.select=0;
				mStarTemp.check=1;
			}
		}

		mStarTemp=getTopStar(mStar);
		if(mStarTemp!=null&&mStarTemp.check==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				count[0]++;
				mStarTemp.select=1;
				mStarTemp.check=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,count,StarSelectList_);
			}else{
				mStarTemp.select=0;
				mStarTemp.check=1;
			}
		}

		mStarTemp=getRightStar(mStar);
		if(mStarTemp!=null&&mStarTemp.check==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				count[0]++;
				mStarTemp.select=1;
				mStarTemp.check=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,count,StarSelectList_);
			}else{
				mStarTemp.select=0;
				mStarTemp.check=1;
			}
		}

		mStarTemp=getBottomStar(mStar);
		if(mStarTemp!=null&&mStarTemp.check==0&&!mStarTemp.AnimaMove){
			if(mStar.color==mStarTemp.color){
				count[0]++;
				mStarTemp.select=1;
				mStarTemp.check=1;
				StarSelectList_.add(mStarTemp);
				check_touch(mStarTemp,count,StarSelectList_);
			}else{
				mStarTemp.select=0;
				mStarTemp.check=1;
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

	//---------------


	public boolean isGuDianByAll(Star[][] mStarArray) {
		for (int i = 0; i < 10; i++) {
			for (int j = 9; j >= 0; j--) {
				if (mStarArray[j][i] != null) {
					if (isGuDianBy_Top_Right(mStarArray[j][i])) {
						return false;
					}
				} else {
					break;
				}
			}
		}
		return true;
	}

	public boolean isGuDianBy_Top_Right(Star mStar) {
		Star Star_Top = getTopStar(mStar);
		if (Star_Top != null && Star_Top.color == mStar.color) {
			return true;
		}
		Star Star_Right = getRightStar(mStar);
		if (Star_Right != null && Star_Right.color == mStar.color) {
			return true;
		}
		return false;
	}

	//---------------
	
}
