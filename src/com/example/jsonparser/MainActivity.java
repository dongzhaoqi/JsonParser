package com.example.jsonparser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dong.model.UserBean;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn1, btn2, btn3, btn4;
	private TextView textView;
	private List<UserBean> userBeans;
	private JSONObject jsonObject;
	private JSONObject jsonObject2;
	private JSONArray jsonArray;
	private String jsonString;
	private String jsonString2;
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
		initListener();
	}

	private void initListener() {
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
	}

	private void initData() {
		userBeans = new ArrayList<UserBean>();
		UserBean userBean1 = new UserBean();
		userBean1.setId(1);
		userBean1.setUserName("dong");
		userBeans.add(userBean1);

		UserBean userBean2 = new UserBean();
		userBean2.setId(2);
		userBean2.setUserName("zhao");
		userBeans.add(userBean2);

	}

	private void initView() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		textView = (TextView) findViewById(R.id.txt);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn1:
			changeNotArrayDataToJson(); // 点击第一个按钮，把集合转换成json数据格式的string
			break;
		case R.id.btn2:
			changeArrayDataToJson(); // 点击第2个按钮，把普通数据换成json数据格式的string
			break;
		case R.id.btn3: // 解析不带集合的json字符串
			if (jsonString2 == null || "".equals(jsonString2)) {
				Toast.makeText(MainActivity.this, "请先点击上面第1个按钮转把数据换成json字符串",
						Toast.LENGTH_LONG).show();
				return;
			}
			changeJsonToData2();
			break;
		case R.id.btn4: // 解析带集合的json字符串
			if (jsonString == null || "".equals(jsonString)) {
				Toast.makeText(MainActivity.this, "请先点击第2按钮把数据换成json字符串",
						Toast.LENGTH_LONG).show();
				return;
			}
			changeJsonToData1();
			break;

		default:
			break;
		}
	}

	private void changeArrayDataToJson() {
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
		for (int i = 0; i < userBeans.size(); i++) {
			jsonObject2 = new JSONObject();
			try {
				jsonObject2.put("userId", userBeans.get(i).getId());
				jsonObject2.put("userName", userBeans.get(i).getUserName());
				jsonArray.put(jsonObject2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			jsonObject.put("userDate", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		jsonString = jsonObject.toString();
		textView.setText(jsonString);
		Log.i(TAG, "转换成json字符串:" + jsonString);
	}

	private void changeNotArrayDataToJson() {
		jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", "1");
			jsonObject.put("userName", "Jack");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		jsonString2 = jsonObject.toString();
		Log.i(TAG, "转换成json字符串:" + jsonString2);
		textView.setText(jsonString2);
	}

	private void changeJsonToData1() {
		StringBuilder stringBuilder = new StringBuilder(); // 用来保存解析出来的额数据，显示在textview
		UserBean userBean;
		List<UserBean> bList = new ArrayList<UserBean>();
		try {
			jsonObject = new JSONObject(jsonString); // 用json格式的字符串获取一个JSONObject对象
			jsonArray = jsonObject.getJSONArray("userDate"); // 通过key，获取JSONObject里面的一个JSONArray数组
			for (int i = 0; i < jsonArray.length(); i++) { // 遍历数据
				jsonObject = jsonArray.getJSONObject(i); // 从JSONArray里面获取一个JSONObject对象
				userBean = new UserBean();
				userBean.setId(jsonObject.getInt("userId")); // 通过key，获取里面的数据
				userBean.setUserName(jsonObject.getString("userName"));
				bList.add(userBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < bList.size(); i++) {
			stringBuilder.append(" 用户id:" + bList.get(i).getId())
					.append("\t用户名字:" + bList.get(i).getUserName())
					.append("\n");
		}
		textView.setText(stringBuilder.toString().replace("null", ""));
	}

	private void changeJsonToData2() {
		try {
			jsonObject = new JSONObject(jsonString2);
			String userName = jsonObject.getString("userName");
			String userIdString = jsonObject.getString("userId");
			textView.setText("用户id" + userIdString + "\t用户名字:" + userName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
