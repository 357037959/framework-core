package com.ctvit.framework.core.utils;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {
	public static File saveFile(MultipartFile file, String destDir){
		String filename = extractFilename(file);
		File dest = getAbsoluteFile(destDir, filename);
		try {
			file.transferTo(dest);
		} catch (Exception e) {
			throw new RuntimeException("save multipart file error:",e);
		}
		return dest;
	}

	private static final String extractFilename(MultipartFile file) {
		String filename = file.getOriginalFilename();
		int slashIndex = filename.indexOf("/");
		if (slashIndex >= 0) {
			filename = filename.substring(slashIndex + 1);
		}
		return filename;
	}

	private static final File getAbsoluteFile(String uploadDir, String filename){
		File desc = new File(uploadDir,filename);
		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		return desc;
	}
}
