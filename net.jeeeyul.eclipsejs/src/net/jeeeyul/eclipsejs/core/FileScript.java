package net.jeeeyul.eclipsejs.core;

import net.jeeeyul.eclipsejs.api.IO;
import net.jeeeyul.eclipsejs.ui.IScriptProvider;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public class FileScript implements IScriptProvider {

	private IFile file;

	public FileScript(IFile file) {
		this.file = file;
	}

	@Override
	public String getScript() {
		try {
			return IO.getInstance().readInputStream(file.getContents(),
					file.getCharset());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getScriptFileName() {
		return file.getName();
	}

	@Override
	public IPath getFullPath() {
		return file.getFullPath();
	}

}