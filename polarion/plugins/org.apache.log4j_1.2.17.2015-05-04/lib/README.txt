log4j-1.2.17.jar, http://www.apache.org/dyn/closer.cgi/logging/log4j/1.2.17/log4j-1.2.17.zip, Apache License 2.0
log4j-1.2.17-src.zip, http://www.apache.org/dyn/closer.cgi/logging/log4j/1.2.17/log4j-1.2.17.zip, Apache License 2.0
slf4j-api-1.7.12.jar, http://www.slf4j.org/dist/slf4j-1.7.12.zip, MIT license
slf4j-api-1.7.12-src.zip, http://www.slf4j.org/dist/slf4j-1.7.12.zip, MIT license
slf4j-log4j12-1.7.12.jar, http://www.slf4j.org/dist/slf4j-1.7.12.zip, MIT license
slf4j-log4j12-1.7.12-src.zip, http://www.slf4j.org/dist/slf4j-1.7.12.zip, MIT license
commons-logging.jar
commons-logging-src.zip

log4j-1.2.17.jar is patched by applying modifications
to class org.apache.log4j.NDC according to rev 310391 from 
Apache SVN repository (http://svn.apache.org/repos/asf), the
changes are described also in
http://nagoya.apache.org/bugzilla/show_bug.cgi?id=25890.

The reason for this change is, that the original implementation
caused memory leaks under situation, when many threads are 
created and terminated and at the same tim, the code in the
threads uses the NDC class without calling NDC.remove() method.

The fix to NDC was submitted to Apache repository in March 2004,
but it is still not part of the newest (1.2.17) Log4J release,
therefore I am including it as patch.
____________________
2005-11-03 dobisekm