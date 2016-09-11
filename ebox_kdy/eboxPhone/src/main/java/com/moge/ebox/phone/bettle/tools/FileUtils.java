package com.moge.ebox.phone.bettle.tools;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;

public class FileUtils {
	public static final String[][] MIME_MapTable = { { ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" },
			{ ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" },
			{ ".h", "text/plain" }, { ".htm", "text/html" },
			{ ".html", "text/html" }, { ".jar", "application/java-archive" },
			{ ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" },
			{ ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" }, { ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".prop", "text/plain" },
			{ ".rar", "application/x-rar-compressed" },
			{ ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" },
			{ ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" }, { ".zip", "application/zip" },
			{ "", "*/*" } };

	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName, 0);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String readInStream(FileInputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	public static boolean writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				"mounted");

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return writeSucc;
	}

	public static String getFileName(String filePath) {
		if (StringUtils.empty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.empty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	public static String getFileFormat(String fileName) {
		if (StringUtils.empty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point).toLowerCase();
	}

	public static String getFileType(String format) {
		String type = "*/*";
		if (StringUtils.empty(format))
			return type;
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (format.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	public static long getFileSize(String filePath) {
		long size = 0L;

		File file = new File(filePath);
		if ((file != null) && (file.exists())) {
			size = file.length();
		}
		return size;
	}

	public static String getFileSize(long size) {
		if (size <= 0L)
			return "0";
		DecimalFormat df = new DecimalFormat("##.##");
		float temp = (float) size / 1024.0F;
		if (temp >= 1024.0F) {
			return df.format(temp / 1024.0F) + "M";
		}

		return df.format(temp) + "K";
	}

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte[] buffer = new byte[1024];
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			int len;
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		int len;
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024L)
			fileSizeString = df.format(fileS) + "B";
		else if (fileS < 1048576L)
			fileSizeString = df.format(fileS / 1024.0D) + "KB";
		else if (fileS < 1073741824L)
			fileSizeString = df.format(fileS / 1048576.0D) + "MB";
		else {
			fileSizeString = df.format(fileS / 1073741824.0D) + "G";
		}
		return fileSizeString;
	}

	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0L;
		}
		if (!dir.isDirectory()) {
			return 0L;
		}
		long dirSize = 0L;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file);
			}
		}
		return dirSize;
	}

	public long getFileList(File dir) {
		long count = 0L;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count += getFileList(file);
				count -= 1L;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte[] buffer = out.toByteArray();
		out.close();
		return buffer;
	}

	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;
	}

	public static boolean checkFileExistsInSdcard(File file) {
		return file.exists();
	}

	public static boolean checkSdcardIsPlugIn() {
		if (!Environment.getExternalStorageState().equals("mounted")) {
			return false;
		}
		return true;
	}

	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0L;
		if (status.equals("mounted"))
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024L;
			} catch (Exception e) {
				e.printStackTrace();
			}
		else {
			return -1L;
		}
		return freeSpace;
	}

	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
		} else {
			status = false;
		}
		return status;
	}

	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals("mounted"))
			status = true;
		else
			status = false;
		return status;
	}

	public static boolean deleteDirectory(String fileName) {
		SecurityManager checker = new SecurityManager();
		boolean status;
		if (!fileName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					Log.i("DirectoryManager deleteDirectory", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}
			} else {
				status = false;
			}
		} else {
			status = false;
		}
		return status;
	}

	public static boolean deleteFile(String fileName) {
		SecurityManager checker = new SecurityManager();
		boolean status;
		if (!fileName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					Log.i("DirectoryManager deleteFile", fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else {
				status = false;
			}
		} else {
			status = false;
		}
		return status;
	}

	public static boolean checkSDCard() {
		return Environment.getExternalStorageState().equals("mounted");
	}

	public static String getSDCardRoot() {
		if (checkSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		return null;
	}
}