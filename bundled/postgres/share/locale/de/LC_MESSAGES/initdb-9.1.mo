��    �      �  �   �      �  R   �     <  
   Z     e  -   v  �   �  �   .    �  A   �  5     J   N     �  6   �  P   �  C   =  :   �  ]   �  4     B   O  H   �  G   �  >   #  9   b  3   �  ?   �  /     -   @  y   n  (   �  #     7   5  (   m  ,   �  '   �  3   �  D     (   d  8   �  -   �  -   �  /   "  "   R  6   u  +   �     �  0   �  ;      $   \  /   �     �  $   �  ~   �  1   s     �  /   �  6   �  &   *  J   Q  �   �     d      w  C   �  -   �  8   
  !   C  ,   e  /   �  4   �  A   �  @   9  ,   z  P   �  I   �  �   B  b   �     '     8  �   W  [   �  %   7     ]     v     �  ;   �  9   �  �   "   >   �   ;   �     .!  u   ?"  q   �"  f   '#  s   �#  &   $     )$  )   1$     [$  &   j$  0   �$  .   �$  )   �$  )   %  "   E%  #   h%  "   �%  #   �%      �%  $   �%  (   &  "   B&     e&  "   �&  !   �&  ,   �&  $   �&  *   '  %   B'     h'  !   �'     �'     �'     �'      �'     (     1(  -   L(  0   z(     �(     �(     �(  *   �(  )   %)     O)     o)     s)  &   �)  %   �)     �)  +   �)  !   *  q  3*  ]   �+  &   ,     *,     6,  6   J,  �   �,  �   -  4  �-  G   /  2   W/  I   �/  $   �/  ?   �/  [   90  J   �0  A   �0  Z   "1  4   }1  I   �1  E   �1  A   B2  >   �2  :   �2  8   �2  J   73  5   �3  .   �3  �   �3  6   �4  0   �4  >   �4  1   95  >   k5  .   �5  >   �5  K   6  7   d6  F   �6  4   �6  8   7  5   Q7  ,   �7  D   �7  5   �7     /8  :   K8  D   �8  ,   �8  6   �8  !   /9  )   Q9  �   {9  +   	:  #   5:  9   Y:  >   �:  .   �:  S   ;  �   U;     /<  "   J<  M   m<  3   �<  =   �<  %   -=  /   S=  M   �=  <   �=  N   >  9   ]>  0   �>  d   �>  N   -?  �   |?  q   @     �@  +   �@  �   �@  d   uA  5   �A  %   B  -   6B     dB  :   yB  B   �B  �   �B  B   �C  <   �C  @  D  �   YE  �   �E  �   xF  �   �F  8   �G     �G  @   �G     H  #   #H  1   GH  -   yH  '   �H  '   �H  $   �H  %   I  +   BI  /   nI  (   �I  0   �I  0   �I  ,   )J  '   VJ  *   ~J  2   �J  9   �J  /   K  H   FK  *   �K     �K  "   �K     �K     L     .L     JL     jL     �L  -   �L  @   �L  "   M     7M     TM      tM  $   �M  (   �M     �M     �M  ,   �M  +   +N     WN  .   oN  /   �N     t   Z   6   4              x          )                   !       D   ,   ~   g         C      f   -   �   b       o       F   *      .       �   m                     /   ?       @   
   �   L   $   e       l   2       �       U       +   (   1   8       ;       �      W      M      s   5      =      z      &      T   :       "   S   u      Y   p          �                   B   I   {   A       �       O      #   n   j   r       '   H   7          a   �   ^   �   G   y   %   w      �   |   J             0       �   �   <       h      `   v   V   �   [             d   k               >               }   \   _      i              	       R   Q             E   ]   3   N           q           9   K   �   c           P   X    
If the data directory is not specified, the environment variable PGDATA
is used.
 
Less commonly used options:
 
Options:
 
Other options:
 
Report bugs to <pgsql-bugs@postgresql.org>.
 
Success. You can now start the database server using:

    %s%s%spostgres%s -D %s%s%s
or
    %s%s%spg_ctl%s -D %s%s%s -l logfile start

 
WARNING: enabling "trust" authentication for local connections
You can change this by editing pg_hba.conf or using the -A option the
next time you run initdb.
       --lc-collate=, --lc-ctype=, --lc-messages=LOCALE
      --lc-monetary=, --lc-numeric=, --lc-time=LOCALE
                            set default locale in the respective category for
                            new databases (default taken from environment)
       --locale=LOCALE       set default locale for new databases
       --no-locale           equivalent to --locale=C
       --pwfile=FILE         read password for the new superuser from file
   %s [OPTION]... [DATADIR]
   -?, --help                show this help, then exit
   -A, --auth=METHOD         default authentication method for local connections
   -E, --encoding=ENCODING   set default encoding for new databases
   -L DIRECTORY              where to find the input files
   -T, --text-search-config=CFG
                            default text search configuration
   -U, --username=NAME       database superuser name
   -V, --version             output version information, then exit
   -W, --pwprompt            prompt for a password for the new superuser
   -X, --xlogdir=XLOGDIR     location for the transaction log directory
   -d, --debug               generate lots of debugging output
   -n, --noclean             do not clean up after errors
   -s, --show                show internal settings
  [-D, --pgdata=]DATADIR     location for this database cluster
 %s initializes a PostgreSQL database cluster.

 %s: "%s" is not a valid server encoding name
 %s: cannot be run as root
Please log in (using, e.g., "su") as the (unprivileged) user that will
own the server process.
 %s: could not access directory "%s": %s
 %s: could not access file "%s": %s
 %s: could not change permissions of directory "%s": %s
 %s: could not create directory "%s": %s
 %s: could not create symbolic link "%s": %s
 %s: could not execute command "%s": %s
 %s: could not find suitable encoding for locale %s
 %s: could not find suitable text search configuration for locale %s
 %s: could not get current user name: %s
 %s: could not obtain information about current user: %s
 %s: could not open file "%s" for reading: %s
 %s: could not open file "%s" for writing: %s
 %s: could not read password from file "%s": %s
 %s: could not write file "%s": %s
 %s: data directory "%s" not removed at user's request
 %s: directory "%s" exists but is not empty
 %s: encoding mismatch
 %s: failed to remove contents of data directory
 %s: failed to remove contents of transaction log directory
 %s: failed to remove data directory
 %s: failed to remove transaction log directory
 %s: file "%s" does not exist
 %s: file "%s" is not a regular file
 %s: input file "%s" does not belong to PostgreSQL %s
Check your installation or specify the correct path using the option -L.
 %s: input file location must be an absolute path
 %s: invalid locale name "%s"
 %s: locale %s requires unsupported encoding %s
 %s: locale name has non-ASCII characters, skipped: %s
 %s: locale name too long, skipped: %s
 %s: must specify a password for the superuser to enable %s authentication
 %s: no data directory specified
You must identify the directory where the data for this database system
will reside.  Do this with either the invocation option -D or the
environment variable PGDATA.
 %s: out of memory
 %s: password file "%s" is empty
 %s: password prompt and password file cannot be specified together
 %s: removing contents of data directory "%s"
 %s: removing contents of transaction log directory "%s"
 %s: removing data directory "%s"
 %s: removing transaction log directory "%s"
 %s: symlinks are not supported on this platform %s: too many command-line arguments (first is "%s")
 %s: transaction log directory "%s" not removed at user's request
 %s: transaction log directory location must be an absolute path
 %s: unrecognized authentication method "%s"
 %s: warning: specified text search configuration "%s" might not match locale %s
 %s: warning: suitable text search configuration for locale %s is unknown
 Encoding %s implied by locale is not allowed as a server-side encoding.
The default database encoding will be set to %s instead.
 Encoding %s is not allowed as a server-side encoding.
Rerun %s with a different locale selection.
 Enter it again:  Enter new superuser password:  If you want to create a new database system, either remove or empty
the directory "%s" or run %s
with an argument other than "%s".
 If you want to store the transaction log there, either
remove or empty the directory "%s".
 No usable system locales were found.
 Passwords didn't match.
 Rerun %s with the -E option.
 Running in debug mode.
 Running in noclean mode.  Mistakes will not be cleaned up.
 The database cluster will be initialized with locale %s.
 The database cluster will be initialized with locales
  COLLATE:  %s
  CTYPE:    %s
  MESSAGES: %s
  MONETARY: %s
  NUMERIC:  %s
  TIME:     %s
 The default database encoding has accordingly been set to %s.
 The default text search configuration will be set to "%s".
 The encoding you selected (%s) and the encoding that the
selected locale uses (%s) do not match.  This would lead to
misbehavior in various character string processing functions.
Rerun %s and either do not specify an encoding explicitly,
or choose a matching combination.
 The files belonging to this database system will be owned by user "%s".
This user must also own the server process.

 The program "postgres" is needed by %s but was not found in the
same directory as "%s".
Check your installation.
 The program "postgres" was found by "%s"
but was not the same version as %s.
Check your installation.
 This might mean you have a corrupted installation or identified
the wrong directory with the invocation option -L.
 Try "%s --help" for more information.
 Usage:
 Use the option "--debug" to see details.
 caught signal
 child process exited with exit code %d child process exited with unrecognized status %d child process was terminated by exception 0x%X child process was terminated by signal %d child process was terminated by signal %s copying template1 to postgres ...  copying template1 to template0 ...  could not change directory to "%s" could not close directory "%s": %s
 could not find a "%s" to execute could not get junction for "%s": %s
 could not identify current directory: %s could not open directory "%s": %s
 could not read binary "%s" could not read directory "%s": %s
 could not read symbolic link "%s" could not remove file or directory "%s": %s
 could not set junction for "%s": %s
 could not stat file or directory "%s": %s
 could not write to child process: %s
 creating collations ...  creating configuration files ...  creating conversions ...  creating dictionaries ...  creating directory %s ...  creating information schema ...  creating subdirectories ...  creating system views ...  creating template1 database in %s/base/1 ...  fixing permissions on existing directory %s ...  initializing dependencies ...  initializing pg_authid ...  invalid binary "%s" loading PL/pgSQL server-side language ...  loading system objects' descriptions ...  not supported on this platform
 ok
 out of memory
 selecting default max_connections ...  selecting default shared_buffers ...  setting password ...  setting privileges on built-in objects ...  vacuuming database template1 ...  Project-Id-Version: PostgreSQL 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2015-01-31 23:56+0000
PO-Revision-Date: 2015-01-31 23:27-0500
Last-Translator: Peter Eisentraut <peter_e@gmx.net>
Language-Team: Peter Eisentraut <peter_e@gmx.net>
Language: de
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
 
Wenn kein Datenverzeichnis angegeben ist, dann wird die Umgebungsvariable
PGDATA verwendet.
 
Weniger häufig verwendete Optionen:
 
Optionen:
 
Weitere Optionen:
 
Berichten Sie Fehler an <pgsql-bugs@postgresql.org>.
 
Erfolg. Sie können den Datenbankserver jetzt mit

    %s%s%spostgres%s -D %s%s%s
oder
    %s%s%spg_ctl%s -D %s%s%s -l logdatei start

starten.

 
WARNUNG: Authentifizierung für lokale Verbindungen auf »trust« gesetzt
Sie können dies ändern, indem Sie pg_hba.conf bearbeiten oder beim
nächsten Aufruf von initdb die Option -A verwenden.
       --lc-collate=, --lc-ctype=, --lc-messages=LOCALE
      --lc-monetary=, --lc-numeric=, --lc-time=LOCALE
                            setze Standardlocale in der jeweiligen Kategorie
                            für neue Datenbanken (Voreinstellung aus der
                            Umgebung entnommen)
       --locale=LOCALE       setze Standardlocale für neue Datenbanken
       --no-locale           entspricht --locale=C
       --pwfile=DATEI        lese Passwort des neuen Superusers aus Datei
   %s [OPTION]... [DATENVERZEICHNIS]
   -?, --help                diese Hilfe anzeigen, dann beenden
   -A, --auth=METHODE        vorgegebene Authentifizierungsmethode für lokale Verbindungen
   -E, --encoding=KODIERUNG  setze Standardkodierung für neue Datenbanken
   -L VERZEICHNIS            wo sind die Eingabedateien zu finden
   -T, --text-search-config=KFG
                            Standardtextsuchekonfiguration
   -U, --username=NAME       Datenbank-Superusername
   -V, --version             Versionsinformationen anzeigen, dann beenden
   -W, --pwprompt            frage nach Passwort für neuen Superuser
   -X, --xlogdir=XLOGVERZ    Verzeichnis für das Transaktionslog
   -d, --debug               erzeuge eine Menge Debug-Ausgaben
   -n, --noclean             nach Fehlern nicht aufräumen
   -s, --show                zeige interne Einstellungen
  [-D, --pgdata=]DATENVERZ   Datenverzeichnis für diesen Datenbankcluster
 %s initialisiert einen PostgreSQL-Datenbankcluster.

 %s: »%s« ist keine gültige Serverkodierung
 %s: kann nicht als root ausgeführt werden
Bitte loggen Sie sich (z.B. mit »su«) als der (unprivilegierte) Benutzer
ein, der Eigentümer des Serverprozesses sein soll.
 %s: konnte nicht auf Verzeichnis »%s« zugreifen: %s
 %s: konnte nicht auf Datei »%s« zugreifen: %s
 %s: konnte Rechte des Verzeichnisses »%s« nicht ändern: %s
 %s: konnte Verzeichnis »%s« nicht erzeugen: %s
 %s: konnte symbolische Verknüpfung »%s« nicht erzeugen: %s
 %s: konnte Befehl »%s« nicht ausführen: %s
 %s: konnte keine passende Kodierung für Locale »%s« finden
 %s: konnte keine passende Textsuchekonfiguration für Locale »%s« finden
 %s: konnte aktuellen Benutzernamen nicht ermitteln: %s
 %s: konnte Informationen über aktuellen Benutzer nicht ermitteln: %s
 %s: konnte Datei »%s« nicht zum Lesen öffnen: %s
 %s: konnte Datei »%s« nicht zum Schreiben öffnen: %s
 %s: konnte Passwort nicht aus Datei »%s« lesen: %s
 %s: konnte Datei »%s« nicht schreiben: %s
 %s: Datenverzeichnis »%s« wurde auf Anwenderwunsch nicht entfernt
 %s: Verzeichnis »%s« existiert aber ist nicht leer
 %s: unpassende Kodierungen
 %s: konnte Inhalt des Datenverzeichnisses nicht entfernen
 %s: konnte Inhalt des Transaktionslogverzeichnisses nicht entfernen
 %s: konnte Datenverzeichnis nicht entfernen
 %s: konnte Transaktionslogverzeichnis nicht entfernen
 %s: Datei »%s« existiert nicht
 %s: Datei »%s« ist keine normale Datei
 %s: Eingabedatei »%s« gehört nicht zu PostgreSQL %s
Prüfen Sie Ihre Installation oder geben Sie den korrekten Pfad mit der
Option -L an.
 %s: Eingabedatei muss absoluten Pfad haben
 %s: ungültiger Locale-Name »%s«
 %s: Locale %s benötigt nicht unterstützte Kodierung %s
 %s: Locale-Name hat Nicht-ASCII-Zeichen, wird ausgelassen: %s
 %s: Locale-Name zu lang, wird ausgelassen: %s
 %s: Superuser-Passwort muss angegeben werden um %s-Authentifizierung einzuschalten
 %s: kein Datenverzeichnis angegeben
Sie müssen das Verzeichnis angeben, wo dieses Datenbanksystem abgelegt
werden soll. Machen Sie dies entweder mit der Kommandozeilenoption -D
oder mit der Umgebungsvariable PGDATA.
 %s: Speicher aufgebraucht
 %s: Passwortdatei »%s« ist leer
 %s: Passwortprompt und Passwortdatei können nicht zusammen angegeben werden
 %s: entferne Inhalt des Datenverzeichnisses »%s«
 %s: entferne Inhalt des Transaktionslogverzeichnisses »%s«
 %s: entferne Datenverzeichnis »%s«
 %s: entferne Transaktionslogverzeichnis »%s«
 %s: symbolische Verknüpfungen werden auf dieser Plattform nicht unterstützt %s: zu viele Kommandozeilenargumente (das erste ist »%s«)
 %s: Transaktionslogverzeichnis »%s« wurde auf Anwenderwunsch nicht entfernt
 %s: Transaktionslogverzeichnis muss absoluten Pfad haben
 %s: unbekannte Authentifizierungsmethode »%s«
 %s: Warnung: angegebene Textsuchekonfiguration »%s« passt möglicherweise nicht zur Locale »%s«
 %s: Warnung: passende Textsuchekonfiguration für Locale »%s« ist unbekannt
 Die von der Locale gesetzte Kodierung %s ist nicht als serverseitige Kodierung erlaubt.
Die Standarddatenbankkodierung wird stattdessen auf %s gesetzt.
 Kodierung %s ist nicht als serverseitige Kodierung erlaubt.
Starten Sie %s erneut mit einer anderen Locale-Wahl.
 Geben Sie es noch einmal ein:  Geben Sie das neue Superuser-Passwort ein:  Wenn Sie ein neues Datenbanksystem erzeugen wollen, entfernen oder leeren
Sie das Verzeichnis »%s« or führen Sie %s
mit einem anderen Argument als »%s« aus.
 Wenn Sie dort den Transaktionslog ablegen wollen, entfernen oder leeren
Sie das Verzeichnis »%s«.
 Es wurden keine brauchbaren System-Locales gefunden.
 Passwörter stimmten nicht überein.
 Führen Sie %s erneut mit der Option -E aus.
 Debug-Modus ist an.
 Noclean-Modus ist an. Bei Fehlern wird nicht aufgeräumt.
 Der Datenbankcluster wird mit der Locale %s initialisiert werden.
 Der Datenbankcluster wird mit folgenden Locales initialisiert werden:
  COLLATE:  %s
  CTYPE:    %s
  MESSAGES: %s
  MONETARY: %s
  NUMERIC:  %s
  TIME:     %s
 Die Standarddatenbankkodierung wurde entsprechend auf %s gesetzt.
 Die Standardtextsuchekonfiguration wird auf »%s« gesetzt.
 Die von Ihnen gewählte Kodierung (%s) und die von der gewählten
Locale verwendete Kodierung (%s) passen nicht zu einander. Das
würde in verschiedenen Zeichenkettenfunktionen zu Fehlverhalten
führen. Starten Sie %s erneut und geben Sie entweder keine
Kodierung explizit an oder wählen Sie eine passende Kombination.
 Die Dateien, die zu diesem Datenbanksystem gehören, werden dem Benutzer
»%s« gehören. Diesem Benutzer muss auch der Serverprozess gehören.

 Das Programm »postgres« wird von %s benötigt, aber wurde nicht im
selben Verzeichnis wie »%s« gefunden.
Prüfen Sie Ihre Installation.
 Das Programm »postgres« wurde von %s gefunden,
aber es hatte nicht die gleiche Version wie %s.
Prüfen Sie Ihre Installation.
 Das könnte bedeuten, dass Ihre Installation fehlerhaft ist oder dass Sie das
falsche Verzeichnis mit der Kommandozeilenoption -L angegeben haben.
 Versuchen Sie »%s --help« für weitere Informationen.
 Aufruf:
 Verwenden Sie die Option »--debug«, um Einzelheiten zu sehen.
 Signal abgefangen
 Kindprozess hat mit Code %d beendet Kindprozess hat mit unbekanntem Status %d beendet Kindprozess wurde durch Ausnahme 0x%X beendet Kindprozess wurde von Signal %d beendet Kindprozess wurde von Signal %s beendet kopiere template1 nach postgres ...  kopiere template1 nach template0 ...  konnte nicht in Verzeichnis »%s« wechseln konnte Verzeichnis »%s« nicht schließen: %s
 konnte kein »%s« zum Ausführen finden konnte Junction für »%s« nicht ermitteln: %s
 konnte aktuelles Verzeichnis nicht ermitteln: %s konnte Verzeichnis »%s« nicht öffnen: %s
 konnte Programmdatei »%s« nicht lesen konnte Verzeichnis »%s« nicht lesen: %s
 konnte symbolische Verknüpfung »%s« nicht lesen konnte Datei oder Verzeichnis »%s« nicht entfernen: %s
 konnte Junction für »%s« nicht erzeugen: %s
 konnte »stat« für Datei oder Verzeichnis »%s« nicht ausführen: %s
 konnte nicht an Kindprozess schreiben: %s
 erzeuge Sortierfolgen ...  erzeuge Konfigurationsdateien ...  erzeuge Konversionen ...  erzeuge Wörterbücher ...  erzeuge Verzeichnis %s ...  erzeuge Informationsschema ...  erzeuge Unterverzeichnisse ...  erzeuge Systemsichten ...  erzeuge Datenbank template1 in %s/base/1 ...  berichtige Zugriffsrechte des bestehenden Verzeichnisses %s ...  initialisiere Abhängigkeiten ...  initialisiere pg_authid ...  ungültige Programmdatei »%s« lade Serversprache PL/pgSQL ...  lade Systemobjektbeschreibungen ...  auf dieser Plattform nicht unterstützt
 ok
 Speicher aufgebraucht
 wähle Vorgabewert für max_connections ...  wähle Vorgabewert für shared_buffers ...  setze das Passwort ...  setze Privilegien der eingebauten Objekte ...  führe Vacuum in Datenbank template1 durch ...  