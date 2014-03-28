// Copyright 2012 Jeeeyul Lee, Seoul, Korea
// https://github.com/jeeeyul/pde-tools
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
// EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
// LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
// GPL, GNU General Public License, V2 or later, http://www.gnu.org/licenses/gpl.html
// AL, Apache License, V2.0 or later, http://www.apache.org/licenses
// BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
// MIT, MIT License, http://www.opensource.org/licenses/MIT
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.
package net.jeeeyul.eclipsejs.ui;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/*
 * Generated by PDE Tools.
 */
public class SharedImages{
	
	/**
	 * <img src="data:image/gif;base64,R0lGODlhEAAQAMZOACZOckpTKkpUMy5Vf0pZfDZii4p7NYp7OIp9PYp9P4p/UZGFTnmGmlOVvomPj4mQj1mawFqawpeXhqKaUWufw2yly6KffnOpzKujea6ld4KsyLKodYKuzIOvzZi50JC82JG815jB283ITc3IU6nK4dXTUdXUWrTR5dfbk9fblNjcltjcmNXcp9zdidzei+HeftzejdXdseHff+HfgeHfg9Xet+bgdOfidebjd9zhqtzhrNzhrezjbOzjbezlaOzlavDlX9zjtdzjtvjqXvjqZefotefptubpvubqvvDvvfDvvvDww/j1w//8/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEKAH8ALAAAAAAQABAAQAeegH+Cghsbg4eHFigtMjc8TSQFAAUkTYRMSUY6CoidD0RDAQRNBRQXpxQFlpdHQhmdggs1MQcGKjA0DLCHDz5ApBohwhqqgwQOg5CSlKuDG0tIr7t/G0xKQTvSnRsJCEU5Biu6uxgsEykuMzg9Te3tiBIvNj9NHwMeJx4DH80OAiMimgCoEKEBhAoAVhEwUQJZvQEdQHDY16yTu3d/AgEAOw==">
	 * Image constant for icons/eclipse-js.gif
	 */
	public static final String ECLIPSE_JS = "icons/eclipse-js.gif";
	private static final ImageRegistry REGISTRY = new ImageRegistry(Display.getDefault());
	
	public static Image getImage(String key){
		Image result = REGISTRY.get(key);
		if(result == null){
			result = loadImage(key);
			REGISTRY.put(key, result);
		}
		return result;
	}
	
	public static ImageDescriptor getImageDescriptor(String key){
		ImageDescriptor result = REGISTRY.getDescriptor(key);
		if(result == null){
			result = loadImageDescriptor(key);
			REGISTRY.put(key, result);
		}
		return result;
	}
	
	private static Image loadImage(String key) {
		try {
			Bundle bundle = Platform.getBundle("net.jeeeyul.eclipsejs");
			URL resource = null;
			
			if(bundle != null){
				resource = Platform.getBundle("net.jeeeyul.eclipsejs").getResource(key);
			}else{
				resource = new File(key).toURI().toURL();	
			}
			
			Image image = new Image(null, resource.openStream());
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		}
	}
	
	private static ImageDescriptor loadImageDescriptor(String key) {
		try {
			Bundle bundle = Platform.getBundle("net.jeeeyul.eclipsejs");
			URL resource = null;
			
			if(bundle != null){
				resource = Platform.getBundle("net.jeeeyul.eclipsejs").getResource(key);
			}else{
				resource = new File(key).toURI().toURL();	
			}
			
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(resource);
			return descriptor;
		} catch (Exception e) {
			e.printStackTrace();
			return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_ERROR_TSK);
		}
	}
}