/**
 *
 */
package org.quiteoldorange.i3textutils.codemining;

import org.eclipse.handly.model.IElementChangeEvent;
import org.eclipse.handly.model.IElementChangeListener;
import org.eclipse.handly.model.IElementDelta;
import org.eclipse.handly.model.impl.support.ElementDelta;
import org.eclipse.jface.text.source.ISourceViewerExtension5;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.ui.editor.XtextEditor;

import com._1c.g5.v8.dt.core.ICoreConstants;
import com._1c.g5.v8.dt.core.handle.IV8File;

/**
 * @author ozolotarev
 *
 */
public class CodeminingsChangeListener
    implements IElementChangeListener
{

    @Override
    public void elementChanged(IElementChangeEvent event)
    {
        var sourceViewer = getSourceViewer(event);

        var a = ICoreConstants.V8_EXTENSION_NATURE;

        // TOOD: сделать что-то типа "обработчик ожидания" чтобы не спасить командами
        if (sourceViewer != null)
            sourceViewer.updateCodeMinings();
    }

    private ISourceViewerExtension5 getSourceViewer(IElementChangeEvent event)
    {
        for (IElementDelta iterator : event.getDeltas())
        {
            ElementDelta delta = (ElementDelta)iterator;

            var element = delta.getElement_();

            if (element instanceof IV8File)
            {
                IV8File v8File = (IV8File)element;

                var workbench = PlatformUI.getWorkbench();

                for (var window : workbench.getWorkbenchWindows())
                {
                    for (var page : window.getPages())
                    {
                        var editors = page.getEditorReferences();

                        for (var editorRef : editors)
                        {
                            var editor = editorRef.getEditor(false);

                            if (editor == null)
                                continue;

                            XtextEditor xeditor = editor.getAdapter(XtextEditor.class);

                            if (xeditor == null)
                                continue;

                            var sourceViewer = (ISourceViewerExtension5)xeditor.getInternalSourceViewer();

                            if (sourceViewer instanceof ISourceViewerExtension5)
                            {
                                ISourceViewerExtension5 svEX5 = sourceViewer;

                                var input = xeditor.getEditorInput();

                                if (input instanceof FileEditorInput)
                                {
                                    FileEditorInput fi = (FileEditorInput)input;

                                    var p1 = fi.getStorage().getFullPath();
                                    var p2 = v8File.getFile().getFullPath();

                                    if (p1.equals(p2))
                                        return svEX5; //.updateCodeMinings();
                                }

                            }
                        }
                    }
                }

            }

        }

        return null;
    }

}
