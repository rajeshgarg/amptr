/**
 * 
 */
package com.nyt.mpt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;

import com.nyt.mpt.domain.Document;

/**
 * This <code>DocumentUtil</code> contains the utility methods of {@link Document} related operations like : saving/deleting document in the file system, calculating version path, calculating file path etc. 
 *
 * @author Gurditta.Garg
 */

public final class DocumentUtil {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private String baseLocation;
	
	/**
	 * Returns the version specific path of file
	 * @param filePath
	 * @return
	 */
	public String calculateVersionPath(final String filePath) {
		final File file = new File(filePath);
		file.mkdirs();
		return filePath + FILE_SEPARATOR + (file.list() == null ? 1 : (file.list().length + 1));
	}

	/**
	 * Saves documents to the file system
	 * @param 	document
	 * 			{@link Document} contains the information which is to be used in while saving
	 * @param 	location
	 * 			Path where the file is to be saved in the File System
	 * @param 	body
	 * 			Content of the file to be saved in the File System
	 * @throws 	IOException
	 * 			Might occur while saving the document to the File System
	 */
	public void saveFileToDisk(final Document document, final String location, final byte[] body) throws IOException {
		new File(location).mkdirs();
		final File file = new File(location + FILE_SEPARATOR + document.getFileName());
		FileCopyUtils.copy(body, new FileOutputStream(file));
	}

	/**
	 * Deletes document from File System and the directory too
	 * @param 	document
	 * 			{@link Document} contains the information which is to be used in while deleting
	 * @throws 	IOException
	 * 			Might occur while saving the document to the File System
	 */
	public void deleteDocumentFromDisk(final Document document) throws IOException {
		final String root = calculateFilePath(document.getComponentId(), document.getDocumentFor(), document.getId());
		FileUtils.deleteDirectory(new File(root));
	}

	/**
	 * Calculates file path without calculating version specific path
	 * @param 	componentId
	 * 			Contains the id of the component for which the document was uploaded
	 * @param 	type
	 * 			Type of entity for which the document was saved
	 * @param 	documentId
	 * 			Contains the document id for which we need to find the path
	 * @return
	 * 			Returns the File Path
	 */
	public String calculateFilePath(final long componentId, final String type, final long documentId) {
		final StringBuffer buffer = new StringBuffer(100);
		buffer.append(getBaseLocation()).append(FILE_SEPARATOR).append(type).append(FILE_SEPARATOR);
		final String str = String.format("%05d", componentId);
		buffer.append(str.substring(0, 3)).append(FILE_SEPARATOR).append(str.substring(3)).append(FILE_SEPARATOR);
		buffer.append(documentId);
		return buffer.toString();
	}
	
	public String getBaseLocation() {
		return baseLocation;
	}

	public void setBaseLocation(String baseLocation) {
		this.baseLocation = baseLocation;
	}
}
