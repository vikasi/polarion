Þ    A      $  Y   ,                §  !   º  -   Ü     
  5     A   R  >     6   Ó  5   
  >   @  O     L   Ï  [     D   x  ,   ½  7   ê  3   "	  5   V	  @   	  /   Í	  8   ý	  D   6
  Z   {
  P   Ö
  4   '  @   \  (     #   Æ  .   ê  (     #   B  9   f  &      /   Ç  #   ÷  &        B     a        *   ¡  +   Ì  4   ø  /   -  0   ]  %     +   ´     à  A   ý  #   ?  9   c  &     -   Ä  !   ò  (     "   =  ,   `       ,      4   Í  %     
   (  &   3     Z    b     ç        '     A   D       F     L   ß  a   ,  R     1   á  L     o   `  U   Ð     &  g   ³  @     I   \  :   ¦  @   á  [   "  7   ~  X   ¶  S     i   c  _   Í  T   -  |     F   ÿ  2   F  O   y  O   É  I     Y   c  E   ½  D     D   H  G     >   Õ  >     E   S  P     M   ê  X   8  C     O   Õ  I   %   H   o   )   ¸   n   â   '   Q!  `   y!  3   Ú!  A   "  ;   P"  G   "  ;   Ô"  M   #     ^#  >   y#  G   ¸#  $    $     %$  ?   7$     w$                /             +   )         ;          %          (                  
   2   :   @   6      "                  ,          8      <   .                       5      	               ?           #                      $                     0   *   &      1   9   =      4   !       '   3   >       A   -      7       
Connection options:
 
General options:
 
Options controlling the output:
 
Report bugs to <pgsql-bugs@postgresql.org>.
   %s [OPTION]...
   --help                   show this help, then exit
   --version                output version information, then exit
   -D, --pgdata=DIRECTORY   receive base backup into directory
   -F, --format=p|t         output format (plain, tar)
   -P, --progress           show progress information
   -U, --username=NAME      connect as specified database user
   -W, --password           force password prompt (should happen automatically)
   -Z, --compress=0-9       compress tar output with given compression level
   -c, --checkpoint=fast|spread
                           set fast or spread checkpointing
   -h, --host=HOSTNAME      database server host or socket directory
   -l, --label=LABEL        set backup label
   -p, --port=PORT          database server port number
   -v, --verbose            output verbose messages
   -w, --no-password        never prompt for password
   -x, --xlog               include required WAL files in backup
   -z, --gzip               compress tar output
 %s takes a base backup of a running PostgreSQL server.

 %s/%s kB (%d%%), %d/%d tablespace %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d tablespace %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s: COPY stream ended before last file was finished
 %s: can only write single tablespace to stdout, database has %i
 %s: could not access directory "%s": %s
 %s: could not connect to server: %s %s: could not create compressed file "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create file "%s": %s
 %s: could not create symbolic link from "%s" to "%s": %s
 %s: could not get COPY data stream: %s %s: could not get WAL end position from server
 %s: could not get backup header: %s %s: could not initiate base backup: %s %s: could not parse file mode
 %s: could not parse file size
 %s: could not read COPY data: %s %s: could not send base backup command: %s %s: could not set compression level %i: %s
 %s: could not set permissions on directory "%s": %s
 %s: could not set permissions on file "%s": %s
 %s: could not write to compressed file "%s": %s
 %s: could not write to file "%s": %s
 %s: directory "%s" exists but is not empty
 %s: final receive failed: %s %s: invalid checkpoint argument "%s", must be "fast" or "spread"
 %s: invalid compression level "%s"
 %s: invalid output format "%s", must be "plain" or "tar"
 %s: invalid tar block header size: %i
 %s: no WAL end position returned from server
 %s: no data returned from server
 %s: no start point returned from server
 %s: no target directory specified
 %s: only tar mode backups can be compressed
 %s: out of memory
 %s: this build does not support compression
 %s: too many command-line arguments (first is "%s")
 %s: unrecognized link indicator "%c"
 Password:  Try "%s --help" for more information.
 Usage:
 Project-Id-Version: PostgreSQL 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2011-08-27 16:29+0900
PO-Revision-Date: 2011-08-30 21:26+0900
Last-Translator: honda@postgresql.jp
Language-Team: Japan Postgresql User Group
Language: ja
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=2; plural=n != 1;
 
æ¥ç¶ãªãã·ã§ã³:
 
æ±ç¨ã®ãªãã·ã§ã³:
 
åºåãå¶å¾¡ãããªãã·ã§ã³:
 
ä¸å·åã¯<pgsql-bugs@postgresql.org>ã¾ã§å ±åãã ãã
   %s [OPTION]...
   --help                   ãã®ãã«ããè¡¨ç¤ºãçµäºãã¾ã
   --version                ãã¼ã¸ã§ã³æå ±ãåºåãçµäºãã¾ã
   -D, --pgdata=DIRECTORY   ãã£ã¬ã¯ããªåã«ãã¼ã¹ããã¯ã¢ãããæ ¼ç´ãã¾ã
   -F, --format=p|t         åºåãã©ã¼ãããï¼ãã¬ã¤ã³ã¾ãã¯tarï¼
   -P, --progress           é²è¡ç¶æ³ã®è¡¨ç¤º
   -U, --username=NAME      æå®ãããã¼ã¿ãã¼ã¹ã¦ã¼ã¶ã§æ¥ç¶
  -W, --password           å¼·å¶çã«ãã¹ã¯ã¼ãå¥åãä¿ãï¼èªåçã«è¡ãããã¯ãã§ãï¼
   -Z, --compress=0-9       æå®ããå§ç¸®ã¬ãã«ã§taråºåãå§ç¸®ãã¾ã
   -c, --checkpoint=fast|spread
                           é«éãã§ãã¯ãã¤ã³ãå¦çã¾ãã¯åæ£ãã§ãã¯ãã¤ã³ãå¦ç
   -h, --host=HOSTNAME      ãã¼ã¿ãã¼ã¹ãµã¼ããã¹ãã¾ãã¯ã½ã±ãããã£ã¬ã¯ããª
   -l, --label=LABEL        ããã¯ã¢ããã©ãã«ã®è¨­å®
   -p, --port=PORT          ãã¼ã¿ãã¼ã¹ãµã¼ãã®ãã¼ãçªå·
   -v, --verbose            åé·ã¡ãã»ã¼ã¸ã®åºå
   -w, --no-password        ãã¹ã¯ã¼ãå¥åãä¿ããªã
   -x, --xlog               å¿è¦ãªWALãã¡ã¤ã«ãããã¯ã¢ããåã«å«ãã¾ã
   -z, --gzip               taråºåãå§ç¸®ãã¾ã
 %sã¯å®è¡ä¸­ã®PostgreSQLãµã¼ãã®ãã¼ã¹ããã¯ã¢ãããåå¾ãã¾ãã
 %s/%s kB (%d%%), %d/%d ãã¼ãã«ç©ºé %s/%s kB (%d%%), %d/%d ãã¼ãã«ç©ºé %s/%s kB (%d%%), %d/%d ãã¼ãã«ç©ºé (%-30.30s) %s/%s kB (%d%%), %d/%d ãã¼ãã«ç©ºé (%-30.30s) %s/%s kB (100%%), %d/%d ãã¼ãã«ç©ºé %35s %s/%s kB (100%%), %d/%d ãã¼ãã«ç©ºé %35s %s: æå¾ã®ãã¡ã¤ã«ãçµããåã«COPYã¹ããªã¼ã ãå®äºãã¾ãã
 %s: ãã¼ã¿ãã¼ã¹ã«ã¯%iããã¾ããããï¼ã¤ã®ãã¼ãã«ç©ºéã®ã¿æ¨æºåºåã«æ¸ãåºããã¾ãã
 %s: "%s"ãã£ã¬ã¯ããªã«ã¢ã¯ã»ã¹ã§ãã¾ããã§ãã: %s
 %s: ãµã¼ãã«æ¥ç¶ã§ãã¾ããã§ãã: %s %s: "%s"å§ç¸®ãã¡ã¤ã«ãä½æãããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã£ã¬ã¯ããªãä½æãããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã¡ã¤ã«ãä½æãããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã"%s"ã¸ã®ã·ã³ããªãã¯ãªã³ã¯ãä½æã§ãã¾ããã§ãã: %s
 %s: COPYãã¼ã¿ã¹ããªã¼ã ãå¥æã§ãã¾ããã§ãã: %s %s: ãµã¼ãããWALçµäºä½ç½®ãå¥æã§ãã¾ããã§ãã
 %s: ããã¯ã¢ããããããå¥æã§ãã¾ããã§ãã: %s %s: ãã¼ã¹ããã¯ã¢ãããåæåã§ãã¾ããã§ãã: %s %s: ãã¡ã¤ã«ã¢ã¼ãã®è§£æãã§ãã¾ããã§ãã
 %s: ãã¡ã¤ã«ãµã¤ãºã®è§£æãã§ãã¾ããã§ãã
 %s: COPYãã¼ã¿ãèª­ã¿åããã¨ãã§ãã¾ããã§ãã: %s %s: ãã¼ã¹ããã¯ã¢ããã³ãã³ããéä¿¡ã§ãã¾ããã§ãã: %s %s: å§ç¸®ã¬ãã«ã%iã«è¨­å®ãããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã£ã¬ã¯ããªã®æ¨©éãè¨­å®ãããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã¡ã¤ã«ã®æ¨©éãè¨­å®ã§ãã¾ããã§ãã: %s
 %s: "%s"å§ç¸®ãã¡ã¤ã«ã«æ¸ãåºããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã¡ã¤ã«ã«æ¸ãåºããã¨ãã§ãã¾ããã§ãã: %s
 %s: "%s"ãã£ã¬ã¯ããªã¯å­å¨ãã¾ããç©ºã§ã¯ããã¾ãã
 %s: æçµåä¿¡ã«å¤±æãã¾ãã: %s %s: "%s"ãã§ãã¯ãã¤ã³ãå¼æ°ã¯ç¡å¹ã§ãã"fast"ã¾ãã¯"spread"ã§ãªããã°ãªãã¾ãã
 %s: "%s"å§ç¸®ã¬ãã«ã¯ç¡å¹ã§ã
 %s: "%s"åºåãã©ã¼ãããã¯ç¡å¹ã§ãã"plain"ã"tar"ã§ãªããã°ãªãã¾ãã
 %s: ç¡å¹ãªtarãã­ãã¯ããããµã¤ãº: %i
 %s: ãµã¼ãããWALçµäºä½ç½®ãè¿ããã¾ããã§ãã
 %s: ãµã¼ãããè¿ããããã¼ã¿ãããã¾ãã
 %s: ãµã¼ãããã¹ã¿ã¼ããã¤ã³ããè¿ãã¾ããã§ãã
 %s: å¯¾è±¡ãã£ã¬ã¯ããªãæå®ããã¦ãã¾ãã
 %s: tarã¢ã¼ãã®ããã¯ã¢ããã®ã¿å§ç¸®ãããã¨ãã§ãã¾ã
 %s: ã¡ã¢ãªä¸è¶³ã§ã
 %s: ãã®æ§ç¯ã§ã¯å§ç¸®ããµãã¼ããã¦ãã¾ãã
 %s: ã³ãã³ãã©ã¤ã³å¼æ°ãå¤éãã¾ã(æåã¯"%s"ã§ã)
 %s: æªç¥ã®ãªã³ã¯æç¤ºå­"%c"
 ãã¹ã¯ã¼ã:  è©³ç´°ã«ã¤ãã¦ã¯"%s --help"ãå®è¡ãã¦ãã ããã
 ä½¿ç¨æ¹æ³:
 