// Force Apache shutdown by processes termination

var
  fso, pid, process, processList, q,
  pidFileName = "C:/Polarion/data/logs/apache/httpd.pid"; // Please, specify correct path!

fso = new ActiveXObject("Scripting.FileSystemObject");

if (fso.FileExists(pidFileName)) {
  pidFile = fso.OpenTextFile(pidFileName, 1);
  pid = pidFile.ReadLine();
  pidFile.Close();

  WScript.Echo("Parent Apache processID " + pid);
  q = "Select * from Win32_process where name = 'httpd.exe' and (processID = " + pid + " or ParentprocessId = " + pid + ")";

  // Get parent Apache process with given PID and all its child processes, than terminate them
  processList = new Enumerator(GetObject("winmgmts:").ExecQuery(q));
  
  for(;!processList.atEnd();processList.moveNext()) {
      process = processList.item();
      WScript.Echo( "Terminating " + process.Name + " " + process.processID);
      process.Terminate();
  }

  WScript.Echo( "Deleting file " + pidFileName);
  fso.DeleteFile(pidFileName);
} else {
  WScript.Echo( "File " + pidFileName + " not found. May be Apache is not running.");
}
