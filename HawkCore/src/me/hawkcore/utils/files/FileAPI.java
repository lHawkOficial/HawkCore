package me.hawkcore.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import me.hawkcore.Core;

public class FileAPI {

	public static void deleteFolder(File folder) {
		if (folder == null || !folder.isDirectory())
			return;
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
			Core.getInstance().sendConsole(
					Core.getInstance().getTag() + " &cNão foi possível deletar o diretorio " + folder.getName() + "!");
		}
	}

	public static void copyFileToPath(Class<?> classpath, String pathFile, String pathCopy) {
		if (classpath == null || pathFile == null || pathCopy == null) {
	        throw new IllegalArgumentException("Arguments cannot be null");
	    }

	    InputStream inputStream = classpath.getResourceAsStream(pathFile);
	    if (inputStream == null) {
	        throw new IllegalArgumentException("File not found: " + pathFile);
	    }

	    File destino = new File(pathCopy);
	    destino.getParentFile().mkdirs();

	    try (OutputStream outputStream = new FileOutputStream(destino)) {
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void copyFileToPath(File file, String pathCopy) {
		if (file == null || pathCopy == null) {
	        throw new IllegalArgumentException("Arguments cannot be null");
	    }

	    try (InputStream inputStream = new FileInputStream(file)) {
	        File destino = new File(pathCopy);
	        destino.getParentFile().mkdirs();

	        try (OutputStream outputStream = new FileOutputStream(destino)) {
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
