/**
 *
 */
package org.quiteoldorange.i3textutils.modulereformatter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.quiteoldorange.i3textutils.ServicesAdapter;
import org.quiteoldorange.i3textutils.StringUtils;
import org.quiteoldorange.i3textutils.core.i3TextUtilsPlugin;
import org.quiteoldorange.i3textutils.modulereformatter.tasks.AddRegionTask;

import com._1c.g5.v8.dt.bsl.model.Module;

/**
 * @author ozolotarev
 *
 */
public class RequiredRegionsCalculator
{
    private Module mModule;
    private IProject mProject;

    public RequiredRegionsCalculator(IProject project, Module module)
    {
        mModule = module;
        mProject = project;
    }

    public List<AddRegionTask> calculateMissingRegions()
    {
        LinkedList<AddRegionTask> result = new LinkedList<>();


        var markerManager = ServicesAdapter.instance().getMarkerManager();
        String id = EcoreUtil.getURI(mModule).trimFragment().toPlatformString(true);

        HashMap<String, String> sUIDMappings = shortUIDMappings();

        var moduleMarkers = markerManager.getMarkers(mProject, id);

        StringBuilder builder = new StringBuilder();

        for (var marker : moduleMarkers)
        {
            @SuppressWarnings("unused")
            var markerId = marker.getMarkerId();
            var checkId = marker.getCheckId();
            var message = marker.getMessage();

            var v8checkid = sUIDMappings.get(checkId);

            var extraInfo = marker.getExtraInfo();
            String uriKey = "uriToProblem"; //$NON-NLS-1$

            String methodName = StringUtils.parseMethodFromURIToProblem(extraInfo.get(uriKey));
            builder.append(String.format("%s : %s (SUID: %s) -> %s\n", methodName, v8checkid, checkId, message)); //$NON-NLS-1$
        }

        String s = builder.toString();

        i3TextUtilsPlugin.getDefault().getLog().info(s);

        return result;
    }

    /**
     * @return
     */
    private HashMap<String, String> shortUIDMappings()
    {
        var checksRepo = ServicesAdapter.instance().getChecksRepository();

        HashMap<String, String> sUIDMappings = new HashMap<>();
        var checks = checksRepo.getChecksWithDescriptions();

        for (var entry : checks.entrySet())
        {
            var suid = checksRepo.getShortUid(entry.getKey(), mProject);

            sUIDMappings.put(suid, entry.getKey().toString());
        }
        return sUIDMappings;
    }


}
