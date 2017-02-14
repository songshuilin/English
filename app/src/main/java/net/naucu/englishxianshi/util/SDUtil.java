package net.naucu.englishxianshi.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDUtil {

	// 判断SD是否已挂载
	public static boolean isSDMounted() {
		// data/data/
		// Environment.getDataDirectory();

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			return true;
		}

		return false;
	}

	// 获取SD卡根路径
	public static String getSDPath() {

		if (isSDMounted()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		return null;
	}

	// 计算SD的总空间大小
	public static long getSDTotalSize() {

		if (isSDMounted()) {
			// 用来计算文件系统的空间大小.
			StatFs stat = new StatFs(getSDPath());
			// 计算总空间大小
			// return stat.getTotalBytes();
			// 获得扇区的数量*每个扇区的大小=磁盘的大小.
			// int blockCount = stat.getBlockCount();
			// int blockSize = stat.getBlockSize();
			// 块的数量
			long blockCountLong = stat.getBlockCountLong();
			// 块的大小
			long blockSizeLong = stat.getBlockSizeLong();

			return blockCountLong * blockSizeLong / 1024 / 1024;
		}

		return 0;
	}

	// 计算SD卡的可用空间大小
	public static long getSDAvailableSize() {
		if (isSDMounted()) {
			StatFs stat = new StatFs(getSDPath());
			// 可用的块的数量
			long availableBlocksLong = stat.getAvailableBlocksLong();
			// 块的大小
			long blockSizeLong = stat.getBlockSizeLong();
			return availableBlocksLong * blockSizeLong / 1024 / 1024;
			// return stat.getAvailableBytes();
		}
		return 0;
	}

	// 存储数据到SD中
	public static boolean saveDataIntoSD(byte[] data, String dir,
			String fileName) {

		// 如果文件不存在,则创建出来.
		String path = SDUtil.getSDPath() + File.separator + dir;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		BufferedOutputStream bos = null;
		try {
			// 带缓冲区的输出流
			bos = new BufferedOutputStream(new FileOutputStream(new File(file,
					fileName)));
			bos.write(data, 0, data.length);
			bos.flush();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;

	}

	// 从SD卡中获取数据的方法
	public static byte[] getDataFromSD(String dir, String fileName) {

		String path = SDUtil.getSDPath() + File.separator + dir
				+ File.separator + fileName;
		File file = new File(path);
		if (file.exists()) {
			// 只有文件存在的时候,才读取出来
			BufferedInputStream bis = null;
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				bis = new BufferedInputStream(new FileInputStream(file));
				int len = 0;
				byte[] buffer = new byte[1024 * 4];
				while ((len = bis.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
					baos.flush();
				}

				return baos.toByteArray();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	// 存储数据到内部存储中--->Context
	public static boolean saveDataIntoInternal(Context context, String type,
			byte[] data, String fileName) {

		// context.getExternalCacheDir();
		// context.getDir(name, mode);

		String path = context.getExternalFilesDir(type).getAbsolutePath();

		// 如果文件不存在,则创建出来.
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		BufferedOutputStream bos = null;
		try {
			// 带缓冲区的输出流
			bos = new BufferedOutputStream(new FileOutputStream(new File(file,
					fileName)));
			bos.write(data, 0, data.length);
			bos.flush();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;

	}

}
