projectId = "elibrary"
spaceName = "Specification"
documentName = "CRS_ApplicationPortal"
renderRuns = 25
exportRuns = 5

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;


import com.polarion.platform.core.PlatformContext
import com.polarion.platform.service.repository.IRepositoryService
import com.polarion.subterra.base.location.ILocation
import com.polarion.subterra.base.location.Location
import com.polarion.alm.projects.model.IProject
import com.polarion.alm.tracker.ITrackerService;
import com.polarion.alm.tracker.exporter.IExportConfiguration;
import com.polarion.alm.tracker.exporter.IExportHelper;
import com.polarion.alm.tracker.exporter.ModuleExportConfiguration;
import com.polarion.alm.tracker.internal.exporters.DefaultExportHelper;
import com.polarion.alm.tracker.internal.exporters.WordExporter;
import com.polarion.alm.tracker.model.IModule

IProject project = projectService.getProject(projectId)
ILocation location = Location.getLocation(spaceName + "/" + documentName)
IModule document = trackerService.getModuleManager().getModule(project, location)
document.resolve()

void doRender(i, IModule document) {
    logger.info("... render #" + i)
    document.renderHomePageContent(IModule.RENDERING_TARGET_EDITOR, null, null)
    logger.info("... render #" + i + " finished")
}

void doTestWordRoundtripExport(i, IModule document) throws Exception {
    logger.info("... export #" + i)
    IExportHelper exportHelper = new DefaultExportHelper();
    IRepositoryService repoService = (IRepositoryService) PlatformContext.getPlatform().lookupService(IRepositoryService.class);
    ITrackerService trackerService = (ITrackerService) PlatformContext.getPlatform().lookupService(ITrackerService.class);
    WordExporter exporter = new WordExporter(exportHelper, repoService, trackerService, null);
    IExportConfiguration config = new ModuleExportConfiguration(document, null, null, null, new LinkedHashMap<String, Object>());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    exporter.export(config, baos);
    baos.close();
    logger.info("... export #" + i + " finished")
}

String formatEstimatedTime(long estimatedTime, int runs) {
    float timePerRun = (float)estimatedTime / (float)runs
    return estimatedTime + " ns (approx. " + ((float)estimatedTime / 1000000000) + " s)" +
    ", run time " + timePerRun + " ns (approx. " + ((float)timePerRun / 1000000000) + " s)"
}

logger.info("Document rendering test started (" + renderRuns + " runs)")
doRender(0, document)
long startTime = System.nanoTime()
for (i in 1..renderRuns) {
    doRender(i, document)
}
long estimatedTime = System.nanoTime() - startTime
logger.info("Document rendering test finished in " + formatEstimatedTime(estimatedTime, renderRuns))

logger.info("Document roundtrip export test started (" + exportRuns + " runs)")
doTestWordRoundtripExport(0, document)
startTime = System.nanoTime()
for (i in 1..exportRuns) {
    doTestWordRoundtripExport(i, document)
}
estimatedTime = System.nanoTime() - startTime
logger.info("Document roundtrip export test finished in " + formatEstimatedTime(estimatedTime, exportRuns))
