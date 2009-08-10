package org.bpelunit.framework.coverage;

import static org.bpelunit.framework.coverage.CoverageConstants.PREFIX_COPY_OF_ARCHIVEFILE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.bpelunit.framework.coverage.exceptions.ArchiveFileException;

import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;

public class ArchiveUtil {

	public static String createArchivecopy(String archive)
			throws ArchiveFileException {
		String fileName = FilenameUtils.getName(archive);
		String pfad = FilenameUtils.getFullPath(archive);
		String nameOfCopy = PREFIX_COPY_OF_ARCHIVEFILE + fileName;

		File copyFile = new File(FilenameUtils.concat(pfad, nameOfCopy));
		File file = new File(archive);
		copyFile.copyAllFrom(file);

		try {
			File.umount(true, true, true, true);
		} catch (ArchiveException e) {
			throw new ArchiveFileException(
					"Could not create copy of bpr-archive", e);
		}
		return copyFile.getAbsolutePath();
	}

	public static List<String> getBPELFileList(String archive) {
		List<String> bpelFiles = new ArrayList<String>();
		File file = new File(archive);
		searchChildrenBPEL(file, bpelFiles);
		return bpelFiles;
	}

	private static void searchChildrenBPEL(File file, List<String> bpelFiles) {
		java.io.File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				searchChildrenBPEL((File) files[i], bpelFiles);
			}
		} else {
			if (FilenameUtils.getExtension(file.getName()).equals("bpel"))
				bpelFiles.add(file.getAbsolutePath());
		}
	}

	public static void deleteArchive(String archive) {
		if (archive == null) {
			return;
		}

		if (archive.endsWith(".zip")) {
			new File(archive).deleteAll();
		} else {
			deleteDir(new File(archive));
		}
	}

	public static void closeArchives() {
		try {
			de.schlichtherle.io.File.umount(true, true, true, true);
		} catch (ArchiveException e) {
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}
