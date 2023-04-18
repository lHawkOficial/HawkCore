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
		String arquivoYmlDentroDoJar = pathFile;
		String destinoNoSistemaDeArquivos = pathCopy;
		InputStream inputStream = classpath.getResourceAsStream(arquivoYmlDentroDoJar);
		File destino = new File(destinoNoSistemaDeArquivos);
		destino.getParentFile().mkdirs();
		try {
			OutputStream outputStream = new FileOutputStream(destino);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFileToPath(File file, String pathCopy) {
		String origem = file.getPath();
		String destino = pathCopy;
		try {
			FileInputStream fileInputStream = new FileInputStream(origem);
			FileOutputStream fileOutputStream = new FileOutputStream(destino);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, bytesRead);
			}
			fileInputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
