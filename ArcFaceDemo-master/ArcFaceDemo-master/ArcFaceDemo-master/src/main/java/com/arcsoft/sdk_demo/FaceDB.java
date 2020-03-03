package com.arcsoft.sdk_demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.sdk_demo.delete.Face;
import com.guo.android_extend.java.ExtInputStream;
import com.guo.android_extend.java.ExtOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gqj3375 on 2017/7/11.
 */

public class FaceDB {
	private final String TAG = this.getClass().toString();
	public static String appid = "Edd3xit6oKZpyo9xkAvhWreaFu94YQjKVYkm35gUHwcy";
	public static String fd_key = "BpsAKsY7uzRxDv3R58crSeiniCef2dD1upaSvdc3o7eb";
	public static String ft_key = "BpsAKsY7uzRxDv3R58crSeifYoPR7zos9AdfAZUTGgj9";
	public static String fr_key = "BpsAKsY7uzRxDv3R58crSejHMohKrpkz6uKJuWmk2Wsu";
	public static String age_key = "BpsAKsY7uzRxDv3R58crSejXgcDgQME8i22eme4jo38z";
	public static String gender_key = "BpsAKsY7uzRxDv3R58crSejer1UnAWnVmytWeddWmw1Z";

	public String mDBPath;
	public List<FaceRegist> mRegister;   //内部类，存储所有用户信息的数组
	public AFR_FSDKEngine mFREngine;
	public AFR_FSDKVersion mFRVersion;
	public boolean mUpgrade;

public 	class FaceRegist {
		String mName;            //用户姓名
		Map<String, AFR_FSDKFace> mFaceList;            //用户图片信息

		public FaceRegist(String name) {                //用户信息初始化
			mName = name;
			mFaceList = new LinkedHashMap<>();
		}
	}//存储脸的单例

	public FaceDB(String path) {                       //脸初始化库
		mDBPath = path;                                //txt路径
		mRegister = new ArrayList<>();
		mFRVersion = new AFR_FSDKVersion();
		mUpgrade = false;
		mFREngine = new AFR_FSDKEngine();
		AFR_FSDKError error = mFREngine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
		if (error.getCode() != AFR_FSDKError.MOK) {
			Log.e(TAG, "AFR_FSDK_InitialEngine fail! error code :" + error.getCode());
		} else {
			mFREngine.AFR_FSDK_GetVersion(mFRVersion);
			Log.d(TAG, "AFR_FSDK_GetVersion=" + mFRVersion.toString());
		}
	}





	public void destroy() {
		if (mFREngine != null) {
			mFREngine.AFR_FSDK_UninitialEngine();
		}
	}//库销毁

	private boolean saveInfo() {
		try {
			FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt");
			ExtOutputStream bos = new ExtOutputStream(fs);
			bos.writeString(mFRVersion.toString() + "," + mFRVersion.getFeatureLevel());
			bos.close();
			fs.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}//脸数据存入txt

	public boolean loadInfo() {
		if (!mRegister.isEmpty()) {
			return false;
		}//若程序中脸库不为空，则脸库读取失败
		try {
			FileInputStream fs = new FileInputStream(mDBPath + "/face.txt");
			ExtInputStream bos = new ExtInputStream(fs);
			//load version
			String version_saved = bos.readString();
			if (version_saved.equals(mFRVersion.toString() + "," + mFRVersion.getFeatureLevel())) {
				mUpgrade = true;
			}
			//load all regist name.
			if (version_saved != null) {
				for (String name = bos.readString(); name != null; name = bos.readString()){
					if (new File(mDBPath + "/" + name + ".data").exists()) {
						mRegister.add(new FaceRegist(new String(name)));
					}
				}
			}
			bos.close();
			fs.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}//脸库数据读取，外部不可调用

	public boolean loadFaces(){
		if (loadInfo()) {
			try {
				for (FaceRegist face : mRegister) {
					Log.d(TAG, "load name:" + face.mName + "'s face feature data.");
					FileInputStream fs = new FileInputStream(mDBPath + "/" + face.mName + ".data");
					ExtInputStream bos = new ExtInputStream(fs);
					AFR_FSDKFace afr = null;
					do {
						if (afr != null) {
							if (mUpgrade) {
								//upgrade data.
							}
							String keyFile = bos.readString();
							face.mFaceList.put(keyFile, afr);
						}
						afr = new AFR_FSDKFace();
					} while (bos.readBytes(afr.getFeatureData()));
					bos.close();
					fs.close();
					Log.d(TAG, "load name: size = " + face.mFaceList.size());
				}
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}//外部调用，实现加载脸库到

	public	void addFace(String name, AFR_FSDKFace face, Bitmap faceicon) {
		try {
			// save face
			String keyPath = mDBPath + "/" + System.nanoTime() + ".jpg";
			File keyFile = new File(keyPath);
			OutputStream stream = new FileOutputStream(keyFile);
			if (faceicon.compress(Bitmap.CompressFormat.JPEG, 80, stream)) {
				Log.d(TAG, "saved face bitmap to jpg!");
			}
			stream.close();

			//check if already registered.
			boolean add = true;
			for (FaceRegist frface : mRegister) {
				if (frface.mName.equals(name)) {
					frface.mFaceList.put(keyPath, face);
					add = false;
					break;
				}
			}
			if (add) { // not registered.
				FaceRegist frface = new FaceRegist(name);
				frface.mFaceList.put(keyPath, face);
				mRegister.add(frface);
			}

			if (saveInfo()) {
				//update all names
				FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt", true);
				ExtOutputStream bos = new ExtOutputStream(fs);
				for (FaceRegist frface : mRegister) {
					bos.writeString(frface.mName);
				}
				bos.close();
				fs.close();

				//save new feature
				fs = new FileOutputStream(mDBPath + "/" + name + ".data", true);
				bos = new ExtOutputStream(fs);
				bos.writeBytes(face.getFeatureData());
				bos.writeString(keyPath);
				bos.close();
				fs.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//实现添加一张脸

	public boolean delete(String name) {
		try {
			//check if already registered.
			boolean find = false;
			for (FaceRegist frface : mRegister) {
				if (frface.mName.equals(name)) {
					File delfile = new File(mDBPath + "/" + name + ".data");
					if (delfile.exists()) {
						delfile.delete();
					}
					mRegister.remove(frface);
					find = true;
					break;
				}
			}

			if (find) {
				if (saveInfo()) {
					//update all names
					FileOutputStream fs = new FileOutputStream(mDBPath + "/face.txt", true);
					ExtOutputStream bos = new ExtOutputStream(fs);
					for (FaceRegist frface : mRegister) {
						bos.writeString(frface.mName);
					}
					bos.close();
					fs.close();
				}
			}
			return find;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}//实现根据名字删除一张脸

	public boolean upgrade() {
		return false;
	}
}
