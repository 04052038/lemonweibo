package com.star.weibo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.star.weibo.AsyncDataLoader.Callback;
import com.star.weibo.adapter.FaceAdapter;
import com.star.weibo.util.TextUtil;
import com.star.weibo.util.WeiboDraft;
import com.star.weibo4j.Weibo;
import com.star.weibo4j.http.ImageItem;
import com.star.weibo4j.model.Trends;
import com.star.weibo4j.model.Status;
import com.star.weibo4j.model.User;
import com.star.weibo4j.model.WeiboException;
import com.star.yytv.BackTaskConst;
import com.star.yytv.R;
import com.star.yytv.common.yytvConst;
import com.star.yytv.model.OAuthInfoManager;
import com.star.yytv.ui.lsn.MyDialogWin;

/**
 * 发布新微博 评论微博 转发微博
 * 
 * @author starry
 * 
 */
public class WeiboUpdater extends Activity {
	private Button back;
	private TextView name;
	private Button send;
	private EditText edit;
	private ImageView contentPic;
	private Button contentPic_del;
	private LinearLayout delete;
	private TextView delete_num;
	private LinearLayout rtAndComment;
	private CheckBox rtAndComment_checkBox;
	private TextView rtAndComment_text;
	private ImageButton location;
	private ImageButton pic;
	private ImageButton topic;
	private ImageButton at;
	private ImageButton face;
	private ImageButton keyboard;
	private EditText trendsList_edit;
	private EditText atList_edit;
	private LinearLayout bottomBar;
	private LinearLayout trendListLayout;
	private ListView trendList;
	private LinearLayout atListLayout;
	private ListView atList;
	private GridView faceGrid;

	private byte[] contentPicByte;
	private String draft;

	private long cid;	//comment id
	private long id;	//weibo id

	private Weibo weibo;
	private MyDialogWin mydialog;

	private int mode;
	public static final String WEIBO_ID = "weiboId";
	public static final String COMMENT_ID = "commentId";
	public static final String WEIBO_CATE = "weiboCate";
	public static final String WEIBO_REDIRECT = "weiboRedirect";
	public static final String WEIBO_ATHIM = "weiboAtHim";
	
	public static final String YYTV_TOPIC = " #摇摇电视#";
	public static final String YYTV_EMOTION = "[话筒]";
	public static final String YYTV_SPLIT_EMO_CONTENT = "：";
	public static final String YYTV_PROGRAM_PREFIX = "「";
	public static final String YYTV_PROGRAM_SUFFIX = "」";
	public static final String YYTV_CONTENT_PLAYING_PREFIX = "我正在看";
	public static final String YYTV_PLAYING_SPLIT_CHANNEL_PROGRAM = " 播放的 ";
	public static final String YYTV_CONTENT_PLAYING_SUFFIX = "，节目非常精彩，值得一看...";
	public static final String YYTV_CONTENT_WILLPLAY_PREFIX = "";
	public static final String YYTV_WILLPLAY_SPLIT_CHANNEL_PROGRAM = " 将要播放 ";
	public static final String YYTV_CONTENT_WILLPLAY_SUFFIX = "，节目内容我很期待，一起来看哦。@摇摇电视 ";
	
	public static final int PIC_CAMERA = 0;
	public static final int PIC_GALLERY = 1;
	public static final int UPDATE = 0;
	public static final int REDIRECT = 10;
	public static final int COMMENT = 100;
	public static final int COMMENT2 = 200;	//comment 'comment'
	public static final int SHARE = 300; 	//share to myfriends this app
	
	private String programComment = null;

	private int index;					//when redirect, EditText cursor bug
	private Editable myedit;			//
	// hanlder处理发布、转发、评论微博
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler.post(runnable);
		}
	};
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			String content = edit.getText().toString();
			if (mode == UPDATE) {
				//xujun 20121005：增加微博发布图片功能
				if (contentPicByte != null && contentPicByte.length > 0){
					updateWeiboWithPic(content);
				}
				else{
					updateWeibo(content);
				}	
			} else if (mode == REDIRECT) {
				redirectWeibo(id, content);
			} else if (mode == COMMENT) {
				commentWeibo(id, content);
			} else if (mode == COMMENT2) {
				commentCom(cid, id, content);
			} else if (mode == SHARE) {
				updateWeibo(content);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_updater);
		
		getViews();
		setViews();
		setListeners();
		
		draft = getDraft();
		if(draft!=null&&!"".equals(draft)){
			createMyDialog(DIALOG_READ_DRAFT);
		}

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			id = bundle.getLong(WEIBO_ID, 0);
			cid = bundle.getLong(COMMENT_ID, 1);
			mode = bundle.getInt(WEIBO_CATE, UPDATE);

			Status redirectStatus = (Status)bundle.getSerializable(WEIBO_REDIRECT);
			if (redirectStatus != null && redirectStatus.getRetweetedStatus() != null){
				StringBuffer redirectContent = new StringBuffer();
				redirectContent.append("//@").append(redirectStatus.getUser().getScreenName());
				redirectContent.append("：").append(redirectStatus.getText());
				if (redirectContent.length() > 140){
					redirectContent.delete(120, redirectContent.length());
				}
				edit.setText(redirectContent.toString());
				edit.requestFocus();
				edit.setSelection(0);
			}
			User user = (User)bundle.getSerializable(WEIBO_ATHIM);
			if (user != null){
				StringBuffer atContent = new StringBuffer();
				atContent.append("@").append(user.getScreenName()).append(" ");
				edit.setText("");
				edit.append(atContent.toString());
			}
		}
		switch (mode) {
		case UPDATE:
			name.setText(R.string.updateWeibo);
			rtAndComment.setVisibility(View.GONE);
//			location.setVisibility(View.VISIBLE);
			pic.setVisibility(View.VISIBLE);
			break;
		case REDIRECT:
			name.setText(R.string.redirectWeibo);
			rtAndComment.setVisibility(View.VISIBLE);
			rtAndComment_text.setText(R.string.rt_sametime_comment);
//			location.setVisibility(View.GONE);
			pic.setVisibility(View.GONE);
			break;
		case COMMENT:
			name.setText(R.string.commentWeibo);
			rtAndComment.setVisibility(View.VISIBLE);
			rtAndComment_text.setText(R.string.comment_sametime_update);
//			location.setVisibility(View.GONE);
			pic.setVisibility(View.GONE);
			break;
		case COMMENT2:
			name.setText(R.string.commentWeibo);
			rtAndComment.setVisibility(View.VISIBLE);
			rtAndComment_text.setText(R.string.comment_sametime_update);
//			location.setVisibility(View.GONE);
			pic.setVisibility(View.GONE);
			break;
		case SHARE:
			name.setText(R.string.updateWeibo);
			edit.setText("我正在使用“柠檬微博客户端”APP，这是一款很酷的应用");
			edit.requestFocus();
			edit.setSelection(0);
			rtAndComment.setVisibility(View.GONE);
//			location.setVisibility(View.GONE);
			pic.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		weibo = Sina.getInstance().getWeibo();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (trendListLayout.isShown()) {
			trendListLayout.setVisibility(View.GONE);
			bottomBar.setVisibility(View.VISIBLE);
			return;
		}
		if (atListLayout.isShown()) {
			atListLayout.setVisibility(View.GONE);
			bottomBar.setVisibility(View.VISIBLE);
			return;
		}
		if (faceGrid.isShown()) {
			faceGrid.setVisibility(View.GONE);
			face.setVisibility(View.VISIBLE);
			keyboard.setVisibility(View.GONE);
			return;
		}
		back();
	}

	private void getViews() {
		back = (Button) findViewById(R.id.weiboUpdater_title_back);
		name = (TextView) findViewById(R.id.weiboUpdater_title_name);
		send = (Button) findViewById(R.id.weiboUpdater_title_send);
		edit = (EditText) findViewById(R.id.weiboUpdater_edit);
		contentPic = (ImageView) findViewById(R.id.updater_content_pic);
		contentPic_del = (Button) findViewById(R.id.updater_content_pic_del);
		delete = (LinearLayout) findViewById(R.id.weiboUpdater_del);
		delete_num = (TextView) findViewById(R.id.weiboUpdater_del_num);
		rtAndComment = (LinearLayout) findViewById(R.id.weiboUpdater_rtAndComment);
		rtAndComment_checkBox = (CheckBox) findViewById(R.id.weiboUpdater_check);
		rtAndComment_text = (TextView) findViewById(R.id.weiboUpdater_rtAndComment_tv);
		location = (ImageButton) findViewById(R.id.weiboUpdater_bottom_location);
		pic = (ImageButton) findViewById(R.id.weiboUpdater_bottom_pic);
		topic = (ImageButton) findViewById(R.id.weiboUpdater_bottom_topic);
		at = (ImageButton) findViewById(R.id.weiboUpdater_bottom_at);
		face = (ImageButton) findViewById(R.id.weiboUpdater_bottom_face);
		keyboard = (ImageButton) findViewById(R.id.weiboUpdater_bottom_keyboard);
		trendsList_edit = (EditText) findViewById(R.id.updater_trendsList_edit);
		atList_edit = (EditText) findViewById(R.id.updater_atList_edit);
		bottomBar = (LinearLayout) findViewById(R.id.updater_bottomBar);
		trendListLayout = (LinearLayout) findViewById(R.id.updater_trendsList_layout);
		trendList = (ListView) findViewById(R.id.updater_trendsList);
		atListLayout = (LinearLayout) findViewById(R.id.updater_atList_layout);
		atList = (ListView) findViewById(R.id.updater_atList);
		faceGrid = (GridView) findViewById(R.id.updater_faceGrid);
	}

	private void setViews() {
		edit.requestFocus();
	}

	private void setListeners() {
		back.setOnClickListener(clickListener);
		send.setOnClickListener(clickListener);
		contentPic_del.setOnClickListener(clickListener);
		delete.setOnClickListener(clickListener);
		location.setOnClickListener(clickListener);
		pic.setOnClickListener(clickListener);
		topic.setOnClickListener(clickListener);
		at.setOnClickListener(clickListener);
		face.setOnClickListener(clickListener);
		keyboard.setOnClickListener(clickListener);
		// 监听编辑字数，实时显示可输入的字数，超过140则无法输入
		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int num = s.toString().length();
				if (num > 140) {
					s.delete(140, s.toString().length());
					edit.setText(s);
					edit.setSelection(edit.getText().length());
					return;
				}
				delete_num.setText("" + (140 - num));
			}
		});
		trendList.setOnItemClickListener(itemClickListener);
		atList.setOnItemClickListener(itemClickListener);
		faceGrid.setOnItemClickListener(itemClickListener);
		trendsList_edit.setOnKeyListener(new OnKeyListener(){			
			@Override
			   public boolean onKey(View v, int keyCode, KeyEvent event) {
			    if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			    	index = edit.getSelectionStart();
					myedit = edit.getEditableText();
					String trend = (String) trendsList_edit.getText().toString();
					if(index < 0 || index >= myedit.length()){
						myedit.append(TextUtil.formatContent(trend,
							WeiboUpdater.this));
					} else {
						myedit.insert(index, TextUtil.formatContent(trend,
							WeiboUpdater.this));
					}
					trendsList_edit.clearFocus();
					edit.requestFocus();
					bottomBar.setVisibility(View.VISIBLE);
					trendListLayout.setVisibility(View.GONE);
					InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
					inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			    	return true;
			    }
			    	return false;
			   }
		});
		trendsList_edit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				index = edit.getSelectionStart();
				myedit = edit.getEditableText();
				String trend = (String) trendsList_edit.getText().toString();
				if(index < 0 || index >= myedit.length()){
					myedit.append(TextUtil.formatContent(trend,
						WeiboUpdater.this));
				} else {
					myedit.insert(index, TextUtil.formatContent(trend,
						WeiboUpdater.this));
				}
				trendsList_edit.clearFocus();
				edit.requestFocus();
				bottomBar.setVisibility(View.VISIBLE);
				trendListLayout.setVisibility(View.GONE);
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		atList_edit.setOnKeyListener(new OnKeyListener(){			
			@Override
			   public boolean onKey(View v, int keyCode, KeyEvent event) {
			    if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			    	index = edit.getSelectionStart();
					myedit = edit.getEditableText();
					String at = (String) atList_edit.getText().toString();
					if(index < 0 || index >= myedit.length()){
						myedit.append(TextUtil.formatContent(at + " ",
								WeiboUpdater.this));
					} else {
						myedit.insert(index, TextUtil.formatContent(at + " ",
								WeiboUpdater.this));
					}
					atList_edit.clearFocus();
					edit.requestFocus();
					bottomBar.setVisibility(View.VISIBLE);
					atListLayout.setVisibility(View.GONE);
					InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
					inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			    	return true;
			    }
			    	return false;
			   }
		});
		atList_edit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				index = edit.getSelectionStart();
				myedit = edit.getEditableText();
				String at = (String) atList_edit.getText().toString();
				if(index < 0 || index >= myedit.length()){
					myedit.append(TextUtil.formatContent(at + " ",
							WeiboUpdater.this));
				} else {
					myedit.insert(index, TextUtil.formatContent(at + " ",
							WeiboUpdater.this));
				}
				atList_edit.clearFocus();
				edit.requestFocus();
				bottomBar.setVisibility(View.VISIBLE);
				atListLayout.setVisibility(View.GONE);
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.weiboUpdater_title_back:
				back();
				break;
			case R.id.weiboUpdater_title_send:
				handler.sendEmptyMessage(0);
				break;
			case R.id.updater_content_pic_del:
				delPic();
				break;
			case R.id.weiboUpdater_del:
				clean();
				break;
			case R.id.weiboUpdater_bottom_location:
				showLocation();
				break;
			case R.id.weiboUpdater_bottom_pic:
				showPic();
				break;
			case R.id.weiboUpdater_bottom_topic:
				showTopic();
				break;
			case R.id.weiboUpdater_bottom_at:
				showAt();
				break;
			case R.id.weiboUpdater_bottom_face:
				showFace();
				break;
			case R.id.weiboUpdater_bottom_keyboard:
				showKeyboard();
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			index = edit.getSelectionStart();
			myedit = edit.getEditableText();
			InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
			switch (arg0.getId()) {
			case R.id.updater_trendsList:
				String trend = (String) trendList.getAdapter().getItem(arg2);
				if(index < 0 || index >= myedit.length()){
					myedit.append(TextUtil.formatContent("#" + trend + "#",
						WeiboUpdater.this));
				} else {
					myedit.insert(index, TextUtil.formatContent("#" + trend + "#",
						WeiboUpdater.this));
				}
				trendsList_edit.clearFocus();
				edit.requestFocus();
				bottomBar.setVisibility(View.VISIBLE);
				trendListLayout.setVisibility(View.GONE);				
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case R.id.updater_atList:
				String at = (String) atList.getAdapter().getItem(arg2);
				if(index < 0 || index >= myedit.length()){
					myedit.append(TextUtil.formatContent("@" + at + " ",
							WeiboUpdater.this));
				} else {
					myedit.insert(index, TextUtil.formatContent("@" + at + " ",
							WeiboUpdater.this));
				}
				atList_edit.clearFocus();
				edit.requestFocus();
				bottomBar.setVisibility(View.VISIBLE);
				atListLayout.setVisibility(View.GONE);
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case R.id.updater_faceGrid:
				if (arg2 < Face.faceNames.length) {
					if(index < 0 || index >= myedit.length()){
						myedit.append(TextUtil.formatImage("[" + Face.faceNames[arg2]
								+ "]", WeiboUpdater.this));
					} else {
						myedit.insert(index, TextUtil.formatImage("[" + Face.faceNames[arg2]
								+ "]", WeiboUpdater.this));
					}
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 返回
	 */
	private void back() {
		String content = edit.getText().toString();
		if (!content.equals("")&&!content.equals(draft)) { // 若当前内容不为空和草稿不同内容，提示是否保存
			createMyDialog(DIALOG_SAVE);
		} else {
			finish();
		}
	}

	/**
	 * 发布微博
	 */
	private void updateWeibo(String content) {
		if ("".equals(content)) {
			return;
		}
		try {
			
			weibo.updateStatus(content);
			WeiboToast.show(getApplicationContext(), "发布成功");
			this.setResult(RESULT_OK);
			finish();
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "发布失败");
		}
		
	}
	
	/**
	 * xujun 20121005：增加微博发布图片功能
	 * 发布 带图片微博
	 */
	private void updateWeiboWithPic(String content) {
		if ("".equals(content)) {
			return;
		}
		try {
			
			ImageItem imageItem = new ImageItem("pic", contentPicByte);
			weibo.uploadStatus(content, imageItem);
			WeiboToast.show(getApplicationContext(), "发布成功");
			this.setResult(RESULT_OK);
			finish();
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "发布失败");
		}
	}
	

	/**
	 * 转发微博
	 * 
	 * @param weiboId
	 *            微博id
	 * @param content
	 *            转发内容
	 */
	private void redirectWeibo(long weiboId, String content) {
		if ("".equals(content)) {
			content = "转发微博";
		}
		if (rtAndComment_checkBox.isChecked()) {
			try {
				weibo.repost(String.valueOf(weiboId), content, 1);
				WeiboToast.show(getApplicationContext(), "转发成功");
				finish();
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(getApplicationContext(), "转发失败");
			}
		} else {
			try {
				weibo.repost(String.valueOf(weiboId), content, 0);
				WeiboToast.show(getApplicationContext(), "转发成功");
				finish();
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(getApplicationContext(), "转发失败");
			}
		}
		
	}

	/**
	 * 评论微博
	 * 
	 * @param weiboId
	 *            微博id
	 * @param content
	 *            评论内容
	 */
	private void commentWeibo(long weiboId, String content) {
		if ("".equals(content)) {
			WeiboToast.show(getApplicationContext(), "请输入评论信息");
			return;
		}
		try {
			weibo.updateComment(content, String.valueOf(weiboId));
			WeiboToast.show(getApplicationContext(), "评论成功");
			if (!rtAndComment_checkBox.isChecked()) {
				finish();
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "评论失败");
		}
		if (rtAndComment_checkBox.isChecked()) {
			updateWeibo(content);
		}
		
	}
	/**
	 * added by yunlong, 2012-11-26
	 * 
	 * comment 'comment'
	 * 
	 * @param commentId
	 * @param weiboId
	 * @param content
	 */
	private void commentCom(long commentId, long weiboId, String content) {
		if ("".equals(content)) {
			WeiboToast.show(getApplicationContext(), "请输入评论信息");
			return;
		}
		try {
			weibo.reply(String.valueOf(commentId), String.valueOf(weiboId), content);
			WeiboToast.show(getApplicationContext(), "评论成功");
			if (!rtAndComment_checkBox.isChecked()) {
				finish();
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			WeiboToast.show(getApplicationContext(), "评论失败");
		}
		if (rtAndComment_checkBox.isChecked()) {
			updateWeibo(content);
		}
		
	}
	/**
	 * 清除文字
	 */
	private void clean() {
		String content = edit.getText().toString();
		if (!content.equals("")) {
			createMyDialog(DIALOG_CLEAN);
		}
	}

	/**
	 * 显示地理信息（未完成）
	 */
	private void showLocation() {

	}

	/**
	 * 插入图片（未完成Camera）
	 */
	private void showPic() {
		createMyDialog(DIALOG_PIC_SOURCE);
	}

	private void delPic(){
		contentPic.setImageBitmap(null);
		contentPicByte = null;
		contentPic_del.setVisibility(View.INVISIBLE);
	}
	/**
	 * 显示话题列表
	 */
	private void showTopic() {
		bottomBar.setVisibility(View.GONE);
		atListLayout.setVisibility(View.GONE);
		faceGrid.setVisibility(View.GONE);
		keyboard.setVisibility(View.GONE);
		face.setVisibility(View.VISIBLE);
		trendListLayout.setVisibility(View.VISIBLE);
		edit.clearFocus();
		trendsList_edit.requestFocus();
		if(trendsList_edit.getEditableText().toString().equals("#请插入话题名称#")){
			trendsList_edit.setSelection(1, 8);
		}else{
			trendsList_edit.setText("##");
			trendsList_edit.setSelection(1);
		}
		
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

		
		if (trendList.getAdapter() == null) {
//			new AsyncDataLoader(trendListCallback).execute();
		}
	}

	/**
	 * 显示@列表
	 */
	private void showAt() {
		bottomBar.setVisibility(View.GONE);
		trendListLayout.setVisibility(View.GONE);
		faceGrid.setVisibility(View.GONE);
		keyboard.setVisibility(View.GONE);
		face.setVisibility(View.VISIBLE);
		atListLayout.setVisibility(View.VISIBLE);
		edit.clearFocus();
		atList_edit.requestFocus();
		if(atList_edit.getEditableText().toString().equals("@请输入用户名")){
			atList_edit.setSelection(1, 7);
		}else{
			atList_edit.setText("@");
			atList_edit.setSelection(1);
		}
		
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

		
		if (atList.getAdapter() == null) {
			new AsyncDataLoader(atListCallback).execute();
		}
	}

	/**
	 * 显示表情
	 */
	private void showFace() {
		trendListLayout.setVisibility(View.GONE);
		atListLayout.setVisibility(View.GONE);
		faceGrid.setVisibility(View.VISIBLE);
		face.setVisibility(View.GONE);
		keyboard.setVisibility(View.VISIBLE);
		if (faceGrid.getAdapter() == null) {
			faceGrid.setAdapter(new FaceAdapter(this, Face.faceNames));
		}
	}

	/**
	 * 显示键盘
	 */
	private void showKeyboard() {
		faceGrid.setVisibility(View.GONE);
		keyboard.setVisibility(View.GONE);
		face.setVisibility(View.VISIBLE);
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 异步加载话题列表
	 */
	private AsyncDataLoader.Callback trendListCallback = new AsyncDataLoader.Callback() {
		ArrayList<String> trendNames;
		
		@Override
		public void onStart() {
			try {
				List<Trends> trends = weibo.getTrendsWeekly(0);
				trendNames = new ArrayList<String>();
				for (int i = 0; i < trends.size(); i++) {
					Trends trend = trends.get(i);
					int len = trend.getTrends().length;
					for (int j = 0; j < len; j++) {
						trendNames.add(trend.getTrends()[j].getName());
					}
				}
			} catch (WeiboException e) {
				e.printStackTrace();
				WeiboToast.show(getApplicationContext(), "话题列表获取失败");
			}
		}

		@Override
		public void onPrepare() {

		}

		@Override
		public void onFinish() {
			ArrayAdapter<String> topicAdapter = new ArrayAdapter<String>(
					WeiboUpdater.this, R.layout.textview, R.id.textview_tv,
					trendNames);
			trendList.setAdapter(topicAdapter);
		}
	};

	private AsyncDataLoader.Callback atListCallback = new Callback() {
		ArrayList<String> names;

		@Override
		public void onStart() {
			names = new ArrayList<String>();
			List<User> users;
			try {
				users = weibo.getFriendsStatuses(OAuthInfoManager.getInstance().getWeiboUserId());
				for (int i = 0; i < users.size(); i++) {
					names.add(users.get(i).getScreenName());
				}
			} catch (WeiboException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onPrepare() {

		}

		@Override
		public void onFinish() {
			ArrayAdapter<String> atAdapter = new ArrayAdapter<String>(
					WeiboUpdater.this, R.layout.textview, R.id.textview_tv,
					names);
			atList.setAdapter(atAdapter);
		}
	};

	private static final int DIALOG_SAVE = 0; // 提示是否保存
	private static final int DIALOG_CLEAN = 1; // 清空文字
	private static final int DIALOG_PIC_SOURCE = 2; // 询问图片来源
	private static final int DIALOG_READ_DRAFT = 3; // 提示存在草稿

	private void createMyDialog(int id) {
		switch (id) {
		case DIALOG_SAVE:
			mydialog = new MyDialogWin(this);
			mydialog.show();
			mydialog.setMsgPopShow();
			mydialog.setPopText(this.getString(R.string.isSave));
			mydialog.setPopItem1Text(this.getString(R.string.ok));
			mydialog.setPopItem2Text(this.getString(R.string.cancel));
			mydialog.setPopItem1(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					saveDraft(edit.getText().toString());
					mydialog.cancel();
					finish();
				}
			});
			mydialog.setPopItem2(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mydialog.cancel();
					finish();
				}
			});
			break;
		case DIALOG_CLEAN:
			mydialog = new MyDialogWin(this);
			mydialog.show();
			mydialog.setMsgPopShow();
			mydialog.setPopText(this.getString(R.string.clean));
			mydialog.setPopItem1Text(this.getString(R.string.ok));
			mydialog.setPopItem2Text(this.getString(R.string.cancel));
			mydialog.setPopItem1(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					edit.setText("");
					mydialog.cancel();
				}
			});
			mydialog.setPopItem2(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mydialog.cancel();
				}
			});
			break;
		case DIALOG_PIC_SOURCE:
		mydialog = new MyDialogWin(this);
		mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopText(this.getString(R.string.setting));
		mydialog.setPopItem1Text(this.getString(R.string.camera));
		mydialog.setPopItem2Text(this.getString(R.string.phoneGallery));
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)){
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					startActivityForResult(intent, PIC_CAMERA);
				}else{
					Toast.makeText(WeiboUpdater.this, R.string.picNoSdcard, Toast.LENGTH_LONG).show();
				}
				mydialog.cancel();
			}
		});
		mydialog.setPopItem2(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// Type为image
				intent.setType("image/*");
				// Action:选择数据然后返回
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, PIC_GALLERY);
				mydialog.cancel();
			}
		});
		break;
		case DIALOG_READ_DRAFT:
		mydialog = new MyDialogWin(this);
		mydialog.show();
		mydialog.setMsgPopShow();
		mydialog.setPopItem3Show();
		mydialog.setPopText(this.getString(R.string.doYouWantToLoadDraft));
		mydialog.setPopItem1Text(this.getString(R.string.ok));
		mydialog.setPopItem2Text(this.getString(R.string.delete));
		mydialog.setPopItem3Text(this.getString(R.string.cancel));
		mydialog.setPopItem1(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit.setText(draft);
				edit.setSelection(draft.length());
				mydialog.cancel();
			}
		});
		mydialog.setPopItem2(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteDraft();
				mydialog.cancel();
			}
		});
		mydialog.setPopItem3(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mydialog.cancel();
			}
		});
		break;
		default:
			break;
		}
	}

	/**
	 * 保存要发布的微博信息
	 */
	private void saveDraft(String text) {
		new WeiboDraft(this).save(text);
	}

	/**
	 * 获取保存的微博信息
	 * 
	 * @return
	 */
	private String getDraft() {
		return new WeiboDraft(this).get();
	}
	/**
	 * 删除保存的微博信息
	 */
	private void deleteDraft(){
		new WeiboDraft(this).delete();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK){
			Toast.makeText(WeiboUpdater.this, R.string.getPicFail, Toast.LENGTH_LONG).show();
			return;
		}
		//xujun 20121005：增加相机拍照功能
		if (requestCode == PIC_CAMERA){
			Uri uri = data.getData();
			Bitmap photo = null;
			if (uri != null){
				photo = BitmapFactory.decodeFile(uri.getPath());
			}
			if (photo == null){
				Bundle bundle = data.getExtras();
				if (bundle != null){
					photo = (Bitmap) bundle.get("data");
				}else{
					Toast.makeText(WeiboUpdater.this, R.string.getPicCameraFail, Toast.LENGTH_LONG).show();
					return;
				}
			}
			if (photo != null){
				ByteArrayOutputStream baos = null;
				try{
					baos = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					contentPicByte = baos.toByteArray();
					contentPic.setImageBitmap(photo);
					contentPic_del.setVisibility(View.VISIBLE);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if (baos != null){
						try{
							baos.close();
						}catch(IOException e){
							e.printStackTrace();
						}
					}	
				}
			}
			
		}else if (requestCode == PIC_GALLERY){
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				try {
					InputStream is = getContentResolver().openInputStream(uri);
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
							1024);
					byte[] buffer = new byte[1024];
					int lenth;
					while ((lenth = is.read(buffer)) >= 0) {
						outputStream.write(buffer, 0, lenth);
					}
					contentPicByte = outputStream.toByteArray();
					// Drawable drawable = BitmapDrawable.createFromStream(
					// getContentResolver().openInputStream(uri), "");
					Bitmap b = BitmapFactory.decodeByteArray(contentPicByte, 0,
							contentPicByte.length);
					// contentPic.setImageDrawable(drawable);
					contentPic.setImageBitmap(b);
					contentPic_del.setVisibility(View.VISIBLE);
					b = null;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	private boolean programIsPlaying(long startTime){
		String time = timeToStr(startTime);
		if (time.length() > 0){
			try{
				SimpleDateFormat format= new SimpleDateFormat("yyyy-mm-dd hh:mm"); 
				Date d = format.parse(time);
				long stime = d.getTime();
				long curTime = System.currentTimeMillis();
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				String curDate = "" + year + "-" + month + "-" + day + " " + hour + ":" + minute;
				c.setTimeInMillis(stime);
				year = c.get(Calendar.YEAR);
			    month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				hour = c.get(Calendar.HOUR_OF_DAY);
				minute = c.get(Calendar.MINUTE);
				String startDate = "" + year + "-" + month + "-" + day + " " + hour + ":" + minute;
				
				if (stime < curTime){
					return true;
				}
			}catch(Exception e){
				Log("time error " + e.getMessage());
			}
		}
		return false;
	}
	
	private String timeToStr(long startTime){
		String time = "" + startTime;
		if (time.length() < 10)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append("20").append(time.substring(0, 2)).append("-");
		buf.append(time.substring(2, 4)).append("-");
		buf.append(time.substring(4, 6)).append(" ");
		buf.append(time.substring(6, 8)).append(":");
		buf.append(time.substring(8, 10));
		//buf.append(time.substring(10, 12));
		return buf.toString();
	}
	
	void Log(String msg) {
		com.star.yytv.Log.i("weibo", "update---" + msg);
	}

}
