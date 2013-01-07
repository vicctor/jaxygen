package org.jaxygen.network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.fileupload.FileItem;
import org.jaxygen.dto.Uploadable;

/**
 * Class represents a file uploaded from network.
 *
 * @author Artur Keska
 *
 */
public class UploadedFile implements Uploadable {

    /**
     *
     */
    private static final long serialVersionUID = -3870740622065257947L;
    private File file;
    private String originalName;
    private String mimeType;
    private final FileItem item;

    public UploadedFile(FileItem item) {
        this.item = item;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    private static String[] splitFileName(String fileName) {
        String[] rc = new String[2];
        int lastDotPos = fileName.lastIndexOf('.');
        if (lastDotPos >= 0) {
            rc[0] = fileName.substring(0, lastDotPos);
            rc[1] = fileName.substring(lastDotPos + 1);
        } else {
            rc[0] = fileName;
            rc[1] = "";
        }
        return rc;
    }

    @Override
    public File getFile() throws IOException {
        if (file == null) {
            String fileName = item.getName();
            File uploadedFile = null;
            String[] fileNameParts = splitFileName(fileName);
            uploadedFile = File.createTempFile("tmp" + fileNameParts[0], "resource." + fileNameParts[1]);
            try {
                item.write(uploadedFile);
                file = uploadedFile;
            } catch (Exception ex) {
                throw new IOException("Could not sore uploaded file", ex);
            }
        }
        return file;
    }

    @Override
    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void dispose() {
        item.delete();
        if (file != null) {
            file.delete();
            file = null;
        }
    }

    public InputStream getInputStream() throws IOException {
        return item.getInputStream();
    }
}
