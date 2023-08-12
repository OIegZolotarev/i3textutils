package org.quiteoldorange.i3textutils.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = i3TextUtilsPlugin.getDefault().getPreferenceStore();

        store.setDefault(PreferenceConstants.FORCE_CONTENT_ASSIST_COLOR_HACK, false);

	}

}
