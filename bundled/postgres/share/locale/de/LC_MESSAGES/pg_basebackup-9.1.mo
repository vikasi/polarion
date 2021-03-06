��    E      D  a   l      �     �       !     -   <     j  5   |  A   �  >   �  6   3  5   j  >   �  O   �  L   /  [   |  D   �  ,   	  7   J	  3   �	  5   �	  @   �	  /   -
  8   ]
  D   �
  Z   �
  P   6  4   �  @   �  (   �  -   &  "   T  #   w  .   �  (   �  #   �  9     &   Q  2   x  #   �  &   �     �           4  *   U  +   �  4   �  /   �  0     %   B  +   h     �  A   �  #   �  9     &   Q  -   x  !   �  (   �  "   �  ,        A  .   T  ,   �  4   �  %   �  "     
   .  &   9     `  �  h            *   3  6   ^     �  >   �  H   �  J   /  6   z  <   �  1   �  O      K   p  m   �  M   *  /   x  ;   �  -   �  8     K   K  4   �  D   �  D     Z   V  P   �  5     \   8  6   �  :   �  -     )   5  8   _  1   �  +   �  N   �  .   E  9   t  1   �  +   �  '     )   4  %   ^  1   �  4   �  E   �  4   1  <   f  /   �  5   �  (   	  O   2  ,   �  G   �  *   �  0   "   *   S   .   ~   #   �   <   �      !  4   )!  8   ^!  <   �!  .   �!  )   "  
   -"  8   8"     q"             
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
 Project-Id-Version: PostgreSQL 9.1
Report-Msgid-Bugs-To: pgsql-bugs@postgresql.org
POT-Creation-Date: 2013-10-07 03:44+0000
PO-Revision-Date: 2013-10-07 12:25-0400
Last-Translator: Peter Eisentraut <peter_e@gmx.net>
Language-Team: Peter Eisentraut <peter_e@gmx.net>
Language: de
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=2; plural=(n != 1);
 
Verbindungsoptionen:
 
Allgemeine Optionen:
 
Optionen, die die Ausgabe kontrollieren:
 
Berichten Sie Fehler an <pgsql-bugs@postgresql.org>.
   %s [OPTION]...
   --help                   diese Hilfe anzeigen, dann beenden
   --version                Versionsinformationen anzeigen, dann beenden
   -D, --pgdata=VERZEICHNIS Basissicherung in dieses Verzeichnis empfangen
   -F, --format=p|t         Ausgabeformat (plain, tar)
   -P, --progress           Fortschrittsinformationen zeigen
   -U, --username=NAME      Datenbankbenutzername
   -W, --password           nach Passwort fragen (sollte automatisch geschehen)
   -Z, --compress=0-9       Tar-Ausgabe mit angegebenem Niveau komprimieren
   -c, --checkpoint=fast|spread
                           schnelles oder verteiltes Checkpointing einstellen
   -h, --host=HOSTNAME      Name des Datenbankservers oder Socket-Verzeichnis
   -l, --label=LABEL        Backup-Label setzen
   -p, --port=PORT          Portnummer des Datenbankservers
   -v, --verbose            »Verbose«-Modus
   -w, --no-password        niemals nach Passwort fragen
   -x, --xlog               benötigte WAL-Dateien in Sicherung einbeziehen
   -z, --gzip               Tar-Ausgabe komprimieren
 %s erzeugt eine Basissicherung eines laufenden PostgreSQL-Servers.

 %s/%s kB (%d%%), %d/%d Tablespace %s/%s kB (%d%%), %d/%d Tablespaces %s/%s kB (%d%%), %d/%d Tablespace (%-30.30s) %s/%s kB (%d%%), %d/%d Tablespaces (%-30.30s) %s/%s kB (100%%), %d/%d Tablespace %35s %s/%s kB (100%%), %d/%d Tablespaces %35s %s: COPY-Strom endete vor dem Ende der letzten Datei
 %s: kann nur einen einzelnen Tablespace auf die Standardausgabe schreiben, Datenbank hat %i
 %s: konnte nicht auf Verzeichnis »%s« zugreifen: %s
 %s: konnte komprimierte Datei »%s« nicht schließen: %s
 %s: konnte Datei »%s« nicht schließen: %s
 %s: konnte nicht mit Server verbinden: %s %s: konnte komprimierte Datei »%s« nicht erzeugen: %s
 %s: konnte Verzeichnis »%s« nicht erzeugen: %s
 %s: konnte Datei »%s« nicht erzeugen: %s
 %s: konnte symbolische Verknüpfung von »%s« nach »%s« nicht erzeugen: %s
 %s: konnte COPY-Datenstrom nicht empfangen: %s %s: konnte WAL-Endposition nicht vom Server empfangen: %s %s: konnte Kopf der Sicherung nicht empfangen: %s %s: konnte Basissicherung nicht starten: %s %s: konnte Dateimodus nicht entziffern
 %s: konnte Dateigröße nicht entziffern
 %s: konnte COPY-Daten nicht lesen: %s %s: konnte Basissicherungsbefehl nicht senden: %s %s: konnte Komprimierungsniveau %i nicht setzen: %s
 %s: konnte Zugriffsrechte des Verzeichnisses »%s« nicht setzen: %s
 %s: konnte Rechte der Datei »%s« nicht setzen: %s
 %s: konnte nicht in komprimierte Datei »%s« schreiben: %s
 %s: konnte nicht in Datei »%s« schreiben: %s
 %s: Verzeichnis »%s« existiert aber ist nicht leer
 %s: letztes Empfangen fehlgeschlagen: %s %s: ungültiges Checkpoint-Argument »%s«, muss »fast« oder »spread« sein
 %s: ungültiges Komprimierungsniveau »%s«
 %s: ungültiges Ausgabeformat »%s«, muss »plain« oder »tar« sein
 %s: ungültige Tar-Block-Kopf-Größe: %i
 %s: kein WAL-Endpunkt vom Server zurückgegeben
 %s: keine Daten vom Server zurückgegeben
 %s: kein Startpunkt vom Server zurückgegeben
 %s: kein Zielverzeichnis angegeben
 %s: nur Sicherungen im Tar-Modus können komprimiert werden
 %s: Speicher aufgebraucht
 %s: ungültiges Verzeichnis empfangen (zu lang): %s
 %s: diese Installation unterstützt keine Komprimierung
 %s: zu viele Kommandozeilenargumente (das erste ist »%s«)
 %s: unbekannter Verknüpfungsindikator »%c«
 %s: nicht unterstützte Serverversion %s
 Passwort:  Versuchen Sie »%s --help« für weitere Informationen.
 Aufruf:
 