function alert(title, msg) {
	if (arguments.length == 1) {
		msg = title;
		title = "Eclipse.JS";
	}
	
	runInUI(function(){
		var shell = org.eclipse.swt.widgets.Display.getDefault().getActiveShell();
		org.eclipse.jface.dialogs.MessageDialog.openInformation(shell, title, msg);		
	});
}

function confirm(title, msg) {
	if (arguments.length == 1) {
		msg = title;
		title = "Eclipse.JS";
	}

	return runInUI(function(){
		var shell = org.eclipse.swt.widgets.Display.getDefault().getActiveShell();
		return org.eclipse.jface.dialogs.MessageDialog.openConfirm(shell, title, msg);
	});
}

/**
 * Runs given function in UI Thread.
 * @param {Function} fn
 * @returns
 */
function runInUI(fn) {
	var result = undefined;
	org.eclipse.swt.widgets.Display.getDefault().syncExec({
		run : function(){
			result = fn();
		}
	});
	return result;
}

function isUIThread(){
	return org.eclipse.swt.widgets.Display.getDefault().getThread() == java.lang.Thread.currentThread(); 
}