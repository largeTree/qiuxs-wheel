package com.qiuxs.wheel.fileupload.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet(urlPatterns = { "/fileupload" })
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -2097591768903298961L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupport Request Method [GET]");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			File tempDir = new File(req.getServletContext().getRealPath("/uploadTempDir"));
			if(!tempDir.exists()) {
				tempDir.mkdirs();
			}
			DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 500, tempDir);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(-1);
			List<FileItem> files = upload.parseRequest(req);
			for (FileItem file : files) {
				System.out.println("FileName" + file.getFieldName());
				System.out.println("Name" + file.getName());
				System.out.println("InputStream" + file.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
