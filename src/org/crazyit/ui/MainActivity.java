package org.crazyit.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.crazyit.utils.HttpUtil;
import org.crazyit.utils.MyDatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	MyDatabaseHelper dbHelper;
	SQLiteDatabase db;
	Cursor cursor;
	ArrayList<Map<String, String>> lis;
	Map<String, String> listItem;
	String user;
	int imageIds[] = new int[] { R.drawable.pri_1, R.drawable.pri_2,
			R.drawable.pri_3 };
	int showall;
	long last_sync;
	boolean offline;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		dbHelper = new MyDatabaseHelper(this, "myDb.db3", 1);
		db = dbHelper.getReadableDatabase();
		Intent in = getIntent();
		Bundle bu = in.getExtras();
		user = bu.getString("email");
		offline = bu.getBoolean("offline");
		if (offline) {
			Toast.makeText(this, "user: " + user + " ����״̬ͬ���ѹر�",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "user: " + user, Toast.LENGTH_SHORT).show();
		}
		// TABLE user (_id , email , pass , status , last_sync, show_all_tag)
		cursor = db.rawQuery("select * from user where email=?",
				new String[] { user });
		last_sync = new Date().getTime();
		if (!cursor.moveToNext()) {
			showall = 1;
			ContentValues con = new ContentValues();
			con.put("email", user);
			con.put("pass", bu.getString("pass"));
			con.put("status", bu.getString("status"));
			con.put("last_sync", last_sync);
			con.put("show_all_tag", showall);
			db.insert("user", null, con);
		} else {
			showall = cursor.getInt(5);
			// ContentValues con2 = new ContentValues();
			// con2.put("last_sync", last_sync);
			// db.update("user", con2, "email=?", new String[]{user});
		}
		cursor.close();

		sync();
	}

	private void sync() {
		// TODO Auto-generated method stub

		if (!offline) {
			JSONObject json_syn = new JSONObject();
			// �ϴ�ͬ�������ӵļ�¼
			Cursor cur1 = db
					.rawQuery(
							"select name, pri, finish_time, edit_time, create_time, ischeckoff from task where status=? and email=?",
							new String[] { "1", user });
			JSONArray jsonarr1 = new JSONArray();
			while (cur1.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", cur1.getString(0));
				map.put("pri", cur1.getString(1));
				map.put("finish_time", cur1.getString(2));
				map.put("edit_time", cur1.getString(3));
				map.put("create_time", cur1.getString(4)); // �����������û���¼
				map.put("ischeckoff", cur1.getString(5));
				JSONObject json1 = new JSONObject(map);
				jsonarr1.put(json1);
			}
			cur1.close();
			// �ϴ�ͬ����ɾ���ļ�¼
			Cursor cur2 = db.rawQuery(
					"select create_time from task where status=? and email=?",
					new String[] { "2", user });
			JSONArray jsonarr2 = new JSONArray();
			while (cur2.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("create_time", cur2.getString(0)); // �����������û���¼
				JSONObject json2 = new JSONObject(map);
				jsonarr2.put(json2);
			}
			cur2.close();
			// �ϴ�ͬ�����޸ĵļ�¼
			Cursor cur3 = db
					.rawQuery(
							"select name, pri, finish_time, edit_time, create_time, ischeckoff from task where status=? and email=?",
							new String[] { "3", user });
			JSONArray jsonarr3 = new JSONArray();
			while (cur3.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", cur3.getString(0));
				map.put("pri", cur3.getString(1));
				map.put("finish_time", cur3.getString(2));
				map.put("edit_time", cur3.getString(3));
				map.put("create_time", cur3.getString(4)); // �����������û���¼
				map.put("ischeckoff", cur3.getString(5));
				JSONObject json3 = new JSONObject(map);
				jsonarr3.put(json3);

			}
			cur3.close();
			try {
				json_syn.put("email", user);
				json_syn.put("add", jsonarr1);
				json_syn.put("delete", jsonarr2);
				json_syn.put("update", jsonarr3);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Log.i("aaaaa", json_syn.toString());
			// Toast.makeText(this, "ͬ����������"+json_syn.toString(),
			// Toast.LENGTH_LONG).show();

			try {
				String result = HttpUtil.syncRequest(json_syn.toString());

				Log.i("syn", result);

				JSONObject json = new JSONObject(result);
				JSONArray jsonarr_add = json.getJSONArray("add");
				// if(jsonarr_add.length()!=0)
				for (int i = 0; i < jsonarr_add.length(); i++) {
					JSONObject json_add = jsonarr_add.getJSONObject(i);
					ContentValues cv = new ContentValues();
					cv.put("name", json_add.getString("name"));
					cv.put("pri", json_add.getString("pri"));
					cv.put("finish_time", json_add.getString("finish_time"));
					cv.put("create_time", json_add.getString("create_time"));
					cv.put("ischeckoff", json_add.getString("ischeckoff"));
					cv.put("edit_time", json_add.getString("edit_time"));
					cv.put("status", 0);
					cv.put("email", user);
					db.insert("task", null, cv);
				}
				JSONArray jsonarr_delete = json.getJSONArray("delete");
				for (int i = 0; i < jsonarr_delete.length(); i++) {
					JSONObject json_delete = jsonarr_delete.getJSONObject(i);
					db.delete(
							"task",
							"create_time=? and email=?",
							new String[] {
									json_delete.getString("create_time"), user });
				}
				JSONArray jsonarr_update = json.getJSONArray("update");

				Log.i("update", jsonarr_update.toString());

				for (int i = 0; i < jsonarr_update.length(); i++) {

					JSONObject json_update = jsonarr_update.getJSONObject(i);
					ContentValues cv = new ContentValues();
					cv.put("name", json_update.getString("name"));
					cv.put("pri", json_update.getString("pri"));
					cv.put("finish_time", json_update.getString("finish_time"));
					cv.put("ischeckoff", json_update.getString("ischeckoff"));
					cv.put("edit_time", json_update.getString("edit_time"));
					cv.put("status", 0);

					db.update(
							"task",
							cv,
							"create_time-?<5 and ?-create_time<5 and email=?",
							new String[] {
									json_update.getString("create_time"),
									json_update.getString("create_time"), user });
				}
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "ExecutionException" + e,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "ͬ��Exception" + e, Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
			// ��ͬ��״̬Ϊ0
			db.delete("task", "status=? and email=?",
					new String[] { "2", user });
			ContentValues cva = new ContentValues();
			cva.put("status", 0);
			db.update("task", cva, "(status=? or status=?) and email=?",
					new String[] { "1", "3", user });
			// ����ͬ��ʱ��
			last_sync = new Date().getTime();
			ContentValues cva1 = new ContentValues();
			cva1.put("last_sync", last_sync);
			db.update("user", cva1, "email=?", new String[] { user });

			showList();
			Toast.makeText(MainActivity.this, "ͬ�����", Toast.LENGTH_SHORT)
					.show();

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		// ��requestCode��resultCodeͬʱΪ0��Ҳ���Ǵ����ض��Ľ��
		Bundle bundle;
		if (requestCode == 0 && resultCode == 0) {
			// ȡ��Intent���Extras����
			bundle = intent.getExtras();
			String name = bundle.getString("name");
			if (!name.equals("")) {
				String pri = bundle.getString("pri");
				String finish_time = bundle.getString("finish_time");
				ContentValues va = new ContentValues();
				// ��ȡ��ǰʱ���
				long d = new Date().getTime();
				va.put("name", name);
				va.put("pri", pri);
				va.put("finish_time", finish_time);
				va.put("edit_time", d);
				va.put("create_time", d);
				va.put("ischeckoff", "0");
				va.put("status", 1);
				va.put("email", user);

				db.insert("task", null, va);
			}
		}
		if (requestCode == 0 && resultCode == 1) {
			// ȡ��Intent���Extras����

			bundle = intent.getExtras();
			String name = bundle.getString("nname");
			if (!name.equals("")) {
				String _id = bundle.getString("_id");
				int pri = bundle.getInt("npri");
				String finish_time = bundle.getString("nfinish_time");
				ContentValues va = new ContentValues();
				va.put("name", name);
				va.put("pri", pri);
				va.put("finish_time", finish_time);
				va.put("edit_time", new Date().getTime());
				if (Long.parseLong(bundle.getString("create_time")) < last_sync) {
					va.put("status", 3);
				}
				db.update("task", va, "_id=?", new String[] { "" + _id });
				showList();
			}
		}
	}

	public void showList() {
		// ִ�в�ѯ
		if (showall == 1) {
			cursor = db
					.rawQuery(
							"select * from task where email=? and status!=2 order by finish_time,pri",
							new String[] { user });
		} else {
			cursor = db
					.rawQuery(
							"select * from task where ischeckoff=? and email=? and status!=2 order by finish_time,pri",
							new String[] { "0", user });
		}

		// ����һ��List���ϣ�List���ϵ�Ԫ����Map
		lis = new ArrayList<Map<String, String>>();
		String now = String.format("%tF", new Date());
		// ����Cursor�����
		while (cursor.moveToNext()) {
			// ��������е����ݴ���ArrayList��
			listItem = new HashMap<String, String>();
			// ȡ����ѯ��¼�е�2�С���3�е�ֵ
			listItem.put("_id", cursor.getString(0));
			listItem.put("name", cursor.getString(1));
			listItem.put("pri", "" + imageIds[(cursor.getInt(2) - 1)]);
			listItem.put("create_time", "" + cursor.getLong(5));

			// 0 _id 1 name 2 pri 3 finish_time 4 edit_time 5 create_time 6
			// ischeckoff 7 status 8 email
			String fin = cursor.getString(3);
			if (now.compareTo(fin) > 0) {
				listItem.put("finish_time", "�ѹ���");
			} else {
				listItem.put("finish_time", fin);
			}
			// if(cursor.getInt(6)==1){
			// listItem.put("ischeckoff", "��ע��");
			// }else{
			// listItem.put("ischeckoff", "  ");
			// }
			lis.add(listItem);
		}
		cursor.close();
		// ����һ��SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, lis,
				R.layout.simple_item, new String[] { "name", "pri",
						"finish_time", }, new int[] { R.id.task, R.id.pri,
						R.id.date });
		ListView list = (ListView) findViewById(R.id.mylist);
		// ΪListView����Adapter
		list.setAdapter(simpleAdapter);
		registerForContextMenu(list);
		// ΪListView���б�����¼����¼�������
		list.setOnItemClickListener(new OnItemClickListener() {
			// ��position�����ʱ�����÷�����
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						EditActivity.class);
				intent.putExtra("_id", lis.get(position).get("_id"));
				intent.putExtra("name", lis.get(position).get("name"));
				intent.putExtra("pri", lis.get(position).get("pri"));
				intent.putExtra("finish_time",
						lis.get(position).get("finish_time"));
				intent.putExtra("create_time",
						lis.get(position).get("create_time"));
				// ���ø�SelectActivity�������룬�����ý���֮���˻ص�Activity
				startActivityForResult(intent, 0);
			}
		});

	}

	public void onDestroy() {
		super.onDestroy();
		ContentValues cv = new ContentValues();
		cv.put("show_all_tag", showall);
		db.update("user", cv, "email=?", new String[] { user });
		// �˳�����ʱ�ر�MyDatabaseHelper���SQLiteDatabase
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflator = new MenuInflater(this);
		// ״̬R.menu.context��Ӧ�Ĳ˵�������ӵ�menu��
		inflator.inflate(R.menu.my_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// �жϵ��������ĸ��˵��������Ե�������Ӧ��
		switch (item.getItemId()) {
		case R.id.syn:
			// setProgressBarIndeterminateVisibility(true);
			sync();
			// setProgressBarIndeterminateVisibility(false);
			break;

		case R.id.add:
			Intent intent_add = new Intent(MainActivity.this, AddActivity.class);
			// ���ø�SelectActivity�Ľ���룬�����ý���֮���˻ص�Activity
			startActivityForResult(intent_add, 0);
			break;

		case R.id.showcheckedoff:
			if (showall == 0) {
				showall = 1;
				showList();
			} else {
				Toast.makeText(this, "����ʾע������", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.hidecheckedoff:
			if (showall == 1) {
				showall = 0;
				showList();
			} else {
				Toast.makeText(this, "������ע������", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.logout:
			ContentValues va = new ContentValues();
			va.put("status", "0");
			db.update("user", va, "email=?", new String[] { user });
			Intent intent_logout = new Intent(MainActivity.this,
					LoginActivity.class);
			startActivity(intent_logout);
			finish();
			break;

		case R.id.about:
			Intent intent_about = new Intent(MainActivity.this,
					AboutActivity.class);
			startActivity(intent_about);
			break;
		}

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflator = new MenuInflater(this);
		// ״̬R.menu.context��Ӧ�Ĳ˵�������ӵ�menu��
		inflator.inflate(R.menu.context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int pos = menuInfo.position;
		switch (item.getItemId()) {
		case R.id.delete:
			if (Long.parseLong(lis.get(pos).get("create_time")) > last_sync) {
				db.delete("task", "_id=? and email=?",
						new String[] { lis.get(pos).get("_id"), user });
			} else {
				ContentValues con3 = new ContentValues();
				con3.put("status", 2);
				db.update("task", con3, "_id=? and email=?", new String[] {
						lis.get(pos).get("_id"), user });
			}
			showList();
			break;
		case R.id.checkoff:
			ContentValues va = new ContentValues();
			va.put("ischeckoff", true);
			db.update("task", va, "_id=?",
					new String[] { lis.get(pos).get("_id") });
			if (showall == 0) {
				showList();
			}
			break;
		}
		return true;
	}

}