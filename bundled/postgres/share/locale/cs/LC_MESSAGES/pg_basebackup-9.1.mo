��    E      D  a   l      �     �       !     -   <     j  5   |  A   �  >   �  6   3  5   j  >   �  O   �  L   /  [   |  D   �  ,   	  7   J	  3   �	  5   �	  @   �	  /   -
  8   ]
  D   �
  Z   �
  P   6  4   �  @   �  (   �  -   &  "   T  #   w  .   �  (   �  #   �  9     &   Q  2   x  #   �  &   �     �           4  *   U  +   �  4   �  /   �  0     %   B  +   h     �  A   �  #   �  9     &   Q  -   x  !   �  (   �  "   �  ,        A  .   T  ,   �  4   �  %   �  "     
   .  &   9     `  �  h     %     7     H  .   h     �  A   �  C   �  ;   .  ;   j  :   �  @   �  M   "  M   p  b   �  Q   !  6   s  7   �  ;   �  4     D   S  2   �  ;   �  g     �   o  y   �  @   r  R   �  ,     2   3  $   f  $   �  2   �  '   �  $     9   0  &   j  4   �  '   �  '   �        $   7     \  )   {  ,   �  ?   �  <     5   O  %   �  0   �  $   �  F     #   H  A   l  /   �  <   �  -      =   I   $   �   /   �      �   9   �   %   /!  H   U!  )   �!  $   �!     �!  *   �!      "             
   =   4   D            	                   A   5       !   '   ;   )   8      %   (             B                      ?                    C                 :                    -       .                  <       0   $      9       &                1                 @            2   ,   6   7      "   *      >   E       +       3   /   #       
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
 %s: could not close compressed file "%s": %s
 %s: could not close file "%s": %s
 %s: could not connect to server: %s %s: could not create compressed file "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create file "%s": %s
 %s: could not create symbolic link from "%s" to "%s": %s
 %s: could not get COPY data stream: %s %s: could not get WAL end position from server: %s %s: could not get backup header: %s %s: could not initiate base backup: %s %s: could not parse file mode
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
 %s: received invalid directory (too long): %s
 %s: this build does not support compression
 %s: too many command-line arguments (first is "%s")
 %s: unrecognized link indicator "%c"
 %s: unsupported server version %s
 Password:  Try "%s --help" for more information.
 Usage:
 Project-Id-Version: pg_basebackup-cs (PostgreSQL 9.1)
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2013-10-09 13:44+0000
PO-Revision-Date: 2013-10-12 00:41+0200
Last-Translator: 
Language-Team: Czech <kde-i18n-doc@kde.org>
Language: cs
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=3; plural=(n==1) ? 0 : (n>=2 && n<=4) ? 1 : 2;
X-Generator: Lokalize 1.5
 
Volby spojení:
 
Obecné volby:
 
Volby ovlivňující výstup:
 
Chyby hlaste na <pgsql-bugs@postgresql.org>.
   %s [VOLBA]...
   --help                   zobraz tuto nápovědu, poté skonči
   --version                zobraz informaci o verzi, poté skonči
   -D, --pgdata=ADRESÁŘ   ulož base backup do adresáře
   -F, --format=p|t         výstupní formát (plain, tar)
   -P, --progress           zobrazuj informace o průběhu
   -U, --username=JMÉNO      připoj se jako uvedený uživatel
   -W, --password           zeptej se na heslo (mělo by se dít automaticky)
   -Z, --compress=0-9       komprimuj výstup taru zvolenou úrovní komprese
   -c, --checkpoint=fast|spread
                           nastavte fast nebo spread checkpointing
   -h, --host=HOSTNAME      host databázového serveru nebo adresář se sockety
   -l, --label=NÁZEV        nastavte jmenovku zálohy
   -p, --port=PORT          port databázového serveru
   -v, --verbose            zobrazuj podrobnější zprávy
   -w, --no-password        nikdy se neptej na heslo
   -x, --xlog               zahrň do zálohy potřebné WAL soubory
   -z, --gzip               komprimuj výstup taru
 %s vytvoří base backup běžícího PostgreSQL serveru.

 %s/%s kB (%d%%), %d/%d tablespace %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespaces %s/%s kB (%d%%), %d/%d tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (%d%%), %d/%d tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d tablespace %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s/%s kB (100%%), %d/%d tablespaces %35s %s: COPY stream skončil před dokončením posledního souboru
 %s: na standardní výstup lze vypsat jen jeden tablespace, databáze jich má %i
 %s: nelze přistoupit k adresáři "%s": %s
 %s: nelze uzavřít komprimovaný soubor "%s": %s
 %s: nelze uzavřít soubor "%s": %s
 %s: nelze se připojit k serveru: %s %s: nelze vytvořit komprimovaný soubor "%s": %s
 %s: nelze vytvořit adresář "%s": %s
 %s: nelze vytvořit soubor "%s": %s
 %s: nelze vytvořit symbolický odkaz z "%s" na "%s": %s
 %s: nelze získat COPY data stream: %s %s: ze serveru nelze získat koncovou WAL pozici: %s %s: nelze získat hlavičku zálohy: %s %s: nelze inicializovat base backup: %s %s: nelze načíst mód souboru
 %s: nelze načíst velikost souboru
 %s: nelze číst COPY data: %s %s: nelze poslat base backup příkaz: %s %s: nelze nastavit úroveň komprese %i: %s
 %s: nelze nastavit přístupová práva na adresáři "%s": %s
 %s: nelze nastavit přístupová práva na souboru "%s": %s
 %s: nelze zapsat do komprimovaného souboru "%s": %s
 %s: nelze zapsat do souboru "%s": %s
 %s: adresář "%s" existuje ale není prázdný
 %s: závěrečný receive selhal: %s %s: chybný checkpoint argument "%s", musí být "fast" nebo "spread"
 %s: chybná úroveň komprese "%s"
 %s: chybný formát výstupu "%s", musí být "plain" nebo "tar"
 %s: neplatná velikost hlavičky tar bloku: %i
 %s: ze serveru nebyla vrácena žádná koncová WAL pozice
 %s: ze serveru nebyla vrácena žádná data
 %s: server nevráti žádný počáteční bod (start point)
 %s: nebyl zadán cílový adresář
 %s: pouze tar zálohy mohou být komprimované
 %s: nedostatek pamětí
 %s: získán neplatný adresář (příliš dlouhý): %s
 %s: tento build nepodporuje kompresi
 %s: příliš mnoho parametrů na příkazové řádce (první je "%s")
 %s: nerozpoznaný indikátor odkazu "%c"
 %s: nepodporovaná verze serveru %s
 Heslo:  Zadejte "%s --help" pro více informací.
 Použití:
 